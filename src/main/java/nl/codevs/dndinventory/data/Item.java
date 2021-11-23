package nl.codevs.dndinventory.data;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public record Item(
        Type category,
        String name,
        Money money,
        Double weight,
        String stats
) {

    @Override
    public String toString() {
        return name + " (" + category.getName() + ")"
                + " worth " + money.toString()
                + " weighs " + weight
                + (stats.isEmpty() ? "" : " stats: " + stats);
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
         * List of items currently in database.
         */
        private static final List<Item> ITEM_LIST = new ArrayList<>();

        /**
         * Mapping from name to item in database.
         * Items are the same instance as {@code ITEM_LIST}.
         */
        private static final ConcurrentHashMap<String, Item>
                ITEM_MAP = new ConcurrentHashMap<>();

        static {
            for (String item : getRawData()) {
                Item newItem = fromCSV(item);
                ITEM_LIST.add(newItem);
                ITEM_MAP.put(newItem.name(), newItem);
            }
        }

        /**
         * Get an item from the database based on name.
         * @param in The input string
         * @return The item
         */
        public static Item fromName(final String in) {
            return ITEM_MAP.get(in);
        }

        /**
         * Get all values in the database,
         * ordered by how closely they match a certain string.
         * Uses {@code LevenshteinDistance} from Apache Commons Text.
         * @param in The input string to match with
         * @return An array of items sorted by how close they match
         */
        public static List<Item> matchAll(final String in) {
            List<Item> items = new ArrayList<>(ITEM_LIST);
            LevenshteinDistance d = new LevenshteinDistance();
            items.sort((i1, i2) -> {

                // Starts-with priority
                if (i1.name().startsWith(in)) {
                    if (i2.name().startsWith(in)) {
                        return Integer.compare(
                                i1.name().length(),
                                i2.name().length()
                        );
                    }
                    return -1;
                } else if (i2.name().startsWith(in)) {
                    return 1;
                }

                // Levenshtein distance
                return Integer.compare(
                        d.apply(in, i1.name()),
                        d.apply(in, i2.name())
                );
            });
            return items;
        }

        /**
         * Get all items in the database.
         * @return Items
         */
        public static List<Item> getItems() {
            return ITEM_LIST;
        }

        /**
         * Add an item to the database. Automatically saves to file.
         * @param item The item to add
         */
        public static void addItem(final Item item) {
            ITEM_LIST.add(item);
            ITEM_MAP.put(item.name(), item);
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save database!");
            }
        }

        /**
         * Save to file.
         */
        public static void save() throws IOException {

            // Convert to CSV
            StringBuilder out = new StringBuilder(
                    "Category,Name,Value,Weight,Stats\n"
            );
            for (Item item : getItems()) {
                out.append(item.category()).append(",")
                        .append(item.name()).append(",")
                        .append(item.money().getAsGP()).append(",")
                        .append(item.weight()).append(",")
                        .append(item.stats()).append("\n");
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
        private static List<String> getRawData() {
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
        public static Item fromCSV(final String csv, final String separator) {
            String[] split = csv.split(separator);
            return new Item(
                    Type.fromString(split[0]),
                    split[1],
                    new Money(Double.parseDouble(split[2])),
                    split[3].equals("null") ? null : Double.parseDouble(split[3]),
                    split.length < 5 ? "" : split[4]
            );
        }

        private Database() {
            // Never called
        }
    }

    public enum Type {
        /** Animals. */
        ANIMALS("Animals", 4),
        /** Armor. */
        ARMOR("Armor", 1),
        /** Clothing. */
        CLOTHING("Clothing", 3),
        /** Weapons. */
        WEAPONS("Weapons", 0),
        /** Daily Food and Lodging. */
        FOOD_LODGING("Daily Food and Lodging", Type.NON_CARRY),
        /** Tack and Harness. */
        HARNESS("Tack and Harness", 3),
        /** Magic Items. */
        MAGIC("Magic Items", 2),
        /** Miscellaneous Equipment. */
        MISC("Miscellaneous Equipment", 3),
        /** Provisions. */
        PROVISIONS("Household Provisioning", 5);

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
         * @throws RuntimeException When the input string
         *                          does not match an {@link Type}
         */
        public static Type fromString(final String in) throws RuntimeException {
            for (Type value : Type.values()) {
                if (value.name.equals(in)
                        || value.getName().equals(in)
                        || value.toString().equals(in)
                ) {
                    return value;
                }
            }
            throw new RuntimeException(
                    "Cannot convert '" + in + "' to valid ItemType"
            );
        }
    }
}
