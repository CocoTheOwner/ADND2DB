package nl.codevs.dndinventory.data;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Item {
    public final Type category;
    public final String name;
    public final Money worth;
    public final Double weight;
    public final String details;

    /**
     * Get the item name.
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new item.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     */
    private Item(
            final Type categoryName,
            final String itemName,
            final Money itemWorth,
            final Double itemWeight,
            final String itemStats) {
        this.category = categoryName;
        this.name = itemName;
        this.worth = itemWorth;
        this.weight = itemWeight;
        this.details = itemStats;
    }

    /**
     * Create a new or get an existing (exactly equivalent) item.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     */
    public static Item makeGetItem(
            final Type categoryName,
            final String itemName,
            final Money itemWorth,
            final double itemWeight,
            final String itemStats
    ) {
        return makeGetItem(categoryName, itemName, itemWorth, itemWeight, itemStats, true, true);
    }

    /**
     * Create a new or get an existing (exactly equivalent) item.
     * @param categoryName item category
     * @param itemName item name
     * @param itemWorth item worth {@link Money}
     * @param itemWeight item weight
     * @param itemStats item stats
     * @param saveToDatabase false to not save to database
     * @param checkExists returns an existing item if one exists in the database (recommended)
     */
    public static Item makeGetItem(
            final Type categoryName,
            final String itemName,
            final Money itemWorth,
            final Double itemWeight,
            final String itemStats,
            final boolean saveToDatabase,
            final boolean checkExists
    ) {
        int hash = hashCode(categoryName, itemName, itemWorth, itemWeight, itemStats);
        if (checkExists && Database.ITEM_MAP.containsKey(hash)) {
            return Database.fromHashCode(hash);
        }
        Item result = new Item(categoryName, itemName, itemWorth, itemWeight, itemStats);
        if (saveToDatabase) {
            Database.addItem(result);
        }
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + category.getName() + ")"
                + " worth " + worth.toString()
                + (weight == null ? " no weight" : " weighs " + weight)
                + (details.isEmpty() ? "" : " stats: " + details);
    }

    /**
     * Item database.
     */
    public static final class Database {

        /**
         * File used to read/write database data.
         */
        private static final File DATABASE_FILE = new File("./itemdb.csv");

        /**
         * Mapping from hashcode to item in database.
         * Items are the same instances as found in {@code ITEM_LIST}.
         */
        private static final ConcurrentHashMap<Integer, Item>
                ITEM_MAP = new ConcurrentHashMap<>();

        static {
            for (String item : getRawData()) {
                Item newItem = fromCSV(item);
                addItem(newItem);
            }
        }

        /**
         * Get an item from the database based on name.
         * @param in The input hashcode
         * @return All items matching the given name
         */
        public static Item fromHashCode(final int in) {
            return ITEM_MAP.get(in);
        }

        /**
         * Get an item from the database by name (not recommended due to runtime).
         * Matches the closest matching item, not per-se the best item.
         * Use {@code #fromHashCode} instead.
         *
         * Given the database is non-empty, returns an item.
         * @param itemName the item name
         * @return an item in the database
         */
        public static Item fromName(final String itemName) {
            return matchAll(itemName).get(0);
        }

        /**
         * Get all values in the database,
         * ordered by how closely they match a certain string.
         * Uses {@code LevenshteinDistance} from Apache Commons Text.
         * @param in The input string to match with
         * @return An array of items sorted by how close they match
         */
        public static List<Item> matchAll(String in) {
            return matchAll(null, in);
        }

        /**
         * Get all values in the database,
         * ordered by how closely they match a certain string.
         * Uses {@code LevenshteinDistance} from Apache Commons Text.
         * @param in The input string to match with
         * @param category The category of the item
         * @return An array of items sorted by how close they match
         */
        public static List<Item> matchAll(final Type category, String in) {
            in = in.toLowerCase(Locale.ROOT);
            List<Item> items;
            if (category == null) {
                items = getItems();
            } else {
                items = getItems().stream().filter(i -> i.category.equals(category)).collect(Collectors.toList());
            };
            LevenshteinDistance d = new LevenshteinDistance();
            String finalIn = in;
            items.sort((i1, i2) -> {

                // Starts-with priority
                boolean i2StartsWith = i2.name.toLowerCase(Locale.ROOT).startsWith(finalIn);
                if (i1.name.toLowerCase(Locale.ROOT).startsWith(finalIn)) {
                    if (i2StartsWith) {
                        return Integer.compare(
                                i1.name.length(),
                                i2.name.length()
                        );
                    }
                    return -1;
                } else if (i2StartsWith) {
                    return 1;
                }

                // Levenshtein distance
                return Integer.compare(
                        d.apply(finalIn, i1.name),
                        d.apply(finalIn, i2.name)
                );
            });
            return items;
        }


        /**
         * Get all items in the database.
         * @return Items
         */
        @Contract(" -> new")
        public static @NotNull List<Item> getItems() {
            return new ArrayList<>(ITEM_MAP.values());
        }

        /**
         * Add an item to the database. Automatically saves to file.
         * @param item The item to add
         * @throws InvalidParameterException when item name already exists in DB
         */
        public static void addItem(final Item item)
                throws InvalidParameterException {

            // Exact copy check
            if (itemAlreadyExists(item)){
                throw new InvalidParameterException("An exact copy of this already exists in the database!" + item);
            }

            ITEM_MAP.put(item.hashCode(), item);

            // Save database
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(item);
                System.out.println("Failed to save database!");
            }
        }

        /**
         * Check if an item already exists
         * @param item the item to check existence of
         * @return true if it exists, false if not
         */
        public static boolean itemAlreadyExists(Item item) {
            return ITEM_MAP.containsKey(item.hashCode());
        }

        /**
         * Save to file.
         */
        public static void save() throws IOException {

            // Convert to CSV
            StringBuilder out = new StringBuilder(
                    "Category,Name,Value,Weight,IStats\n"
            );
            for (Item item : getItems()) {
                out.append(item.category).append(",")
                        .append(item.name).append(",")
                        .append(item.worth.getAsGP()).append(",")
                        .append(item.weight).append(",")
                        .append(item.details).append("\n");
            }

            // Write to file
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(DATABASE_FILE)
            );
            writer.write(out.toString());
            writer.close();
        }

        /**
         * Get raw data from database.
         * @return Raw item data from the DATABASE
         */
        private static @NotNull List<String> getRawData() {
            List<String> data = new ArrayList<>();
            String line;
            try {
                BufferedReader r = new BufferedReader(
                        new FileReader(DATABASE_FILE)
                );
                while ((line = r.readLine()) != null && !line.isEmpty()) {
                    data.add(line);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Item database file not found");
            } catch (IOException e) {
                throw new RuntimeException("Failed getting data from database");
            }
            data.remove(0); // Header
            return data;
        }

        /**
         * Get an item from a csv String.
         * @param csv The csv string
         * @return An item
         */
        public static Item fromCSV(final String csv) {
            return fromCSV(csv, ",");
        }

        /**
         * Get an item from a csv string with custom separator.
         * @param csv The csv string
         * @param separator The separator
         * @return An item
         */
        public static Item fromCSV(final @NotNull String csv, final String separator) {
            String[] split = csv.split(separator);
            return Item.makeGetItem(
                    Type.fromString(split[0]),
                    split[1],
                    new Money(Double.parseDouble(split[2])),
                    split[3].equals("null") ? null : Double.parseDouble(split[3]),
                    split.length < 5 ? "" : split[4],
                    false,
                    true
            );
        }

        private Database() {
            // Never called
        }
    }

    public enum Type {
        /** Animals. */
        ANIMALS("Animals", 5),
        /** Armor. */
        ARMOR("Armor", 2),
        /** Clothing. */
        CLOTHING("Clothing", 4),
        /** Weapons. */
        WEAPONS("Weapons", 1),
        /** Daily Food and Lodging. */
        FOOD_LODGING("Daily Food and Lodging", Type.NON_CARRY),
        /** Tack and Harness. */
        HARNESS("Tack and Harness", 4),
        /** Magic Items. */
        MAGIC("Magic Items", 3),
        /** Miscellaneous Equipment. */
        MISC("Miscellaneous Equipment", 4),
        /** Provisions. */
        PROVISIONS("Household Provisioning", 6),
        /**
         * Gemstones.
         */
        GEMSTONES("Gemstones", 0);

        /**
         * 'Items' that cannot be carried (such as services).
         */
        private static final int NON_CARRY = Integer.MAX_VALUE;

        /**
         * Category name.
         */
        private final String name;

        /**
         * Category position (lower = higher priority in inventory).
         */
        private final int pos;

        /**
         * Get category name.
         * @return Category name
         */
        public String getName() {
            return name;
        }

        /**
         * Get position.
         * @return Category position
         */
        public int getPos() {
            return pos;
        }

        Type(final String typeName, final int position) {
            name = typeName;
            pos = position;
        }

        /**
         * Get item type from string.
         * @param in The input string
         * @return The {@link Type} belonging to the input string
         * @throws InvalidParameterException When the input string
         *                          does not match an {@link Type}
         */
        public static @NotNull Type fromString(String in) throws IllegalArgumentException {
            in = in.toLowerCase(Locale.ROOT);
            for (Type value : Type.values()) {
                if (value.name.equals(in)
                        || value.getName().equals(in)
                        || value.toString().equals(in)
                        || value.getName().toLowerCase(Locale.ROOT).equals(in)
                        || value.toString().toLowerCase(Locale.ROOT).equals(in)
                        || value.getName().toLowerCase(Locale.ROOT).startsWith(in)
                        || value.toString().toLowerCase(Locale.ROOT).startsWith(in)
                        || value.getName().toLowerCase(Locale.ROOT).endsWith(in)
                        || value.toString().toLowerCase(Locale.ROOT).endsWith(in)
                ) {
                    return value;
                }
            }
            throw new InvalidParameterException(
                    "Cannot convert '" + in + "' to valid ItemType"
            );
        }
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     *     method, then calling the {@code hashCode} method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Object#equals(Object)}
     *     method, then calling the {@code hashCode} method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return hashCode(category, name, worth, weight, details);
    }

    /**
     * Compute hashcode without instance
     * @param category item category
     * @param name item name
     * @param worth item worth {@link Money}
     * @param weight item weight
     * @param details item details
     * @return hashcode based on aforementioned details
     */
    public static int hashCode(@NotNull Type category, @NotNull String name, @NotNull Money worth, Double weight, @NotNull String details) {
        return category.getName().hashCode() + name.hashCode() + worth.hashCode() + ((Double) (weight == null ? 0 : weight)).hashCode() + details.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return hashCode() == obj.hashCode();
    }
}
