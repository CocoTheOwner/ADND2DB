package nl.codevs.dndinventory.data;

import okhttp3.internal.annotations.EverythingIsNonNull;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.management.InstanceAlreadyExistsException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Item database.
 */
@EverythingIsNonNull
public final class ItemDatabase {

    /**
     * File used to read/write database data.
     */
    private static final File DATABASE_FILE = new File("./DNDInventories/databases/itemdb.csv");

    /**
     * Separator used in the CSV {@link #DATABASE_FILE}.
     */
    private static final String CSV_SEPARATOR = ",";

    /**
     * Expected CSV header.
     */
    private static final String EXPECTED_HEADER = "Category,Name,Value,Weight,Stats";

    /**
     * Levenshtein distance.
     */
    private static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance();

    /**
     * The item database.
     */
    private static final ConcurrentHashMap<Integer, Item> DATABASE = new ConcurrentHashMap<>();

    // Setup data from database
    static {
        try {
            AtomicBoolean valid = new AtomicBoolean(false);
            new BufferedReader(new FileReader(DATABASE_FILE)).lines().map(l -> {
                if (l.equalsIgnoreCase(EXPECTED_HEADER)) {
                    valid.set(true);
                    return null;
                }
                String[] split = l.split(CSV_SEPARATOR);
                return new Item(
                        ItemType.fromString(split[0]),
                        split[1],
                        new Money(Double.parseDouble(split[2])),
                        split[3].equals("null") ? null : Double.parseDouble(split[3]),
                        split.length < 5 ? "" : split[4]
                );
            }).filter(Objects::nonNull).forEachOrdered(i -> DATABASE.put(i.hashCode(), i));
            if (!valid.get()) {
                throw new RuntimeException("CSV file did not have a valid header: " + EXPECTED_HEADER);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Item database file not found");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new RuntimeException("Database entry corrupt, not enough ',' available");
        }
    }

    /**
     * Get the database.
     */
    public static ConcurrentHashMap<Integer, Item> get() {
        return DATABASE;
    }

    /**
     * Get an item from the database by name (not recommended due to runtime).
     * Matches the closest matching item, not per-se the best item.
     * Use {@code #fromHashCode} instead.
     * <p>
     * Given the database is non-empty, returns an item.
     *
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
     *
     * @param in The input string to match with
     * @return An array of items sorted by how close they match
     */
    @Contract("_ -> new")
    public static List<Item> matchAll(String in) {
        return matchAll(null, in);
    }

    /**
     * Get all values in the database,
     * ordered by how closely they match a certain string.
     * Uses {@code LevenshteinDistance} from Apache Commons Text.
     *
     * @param in       The input string to match with
     * @param category The category of the item
     * @return An array of items sorted by how close they match
     */
    @Contract("_, _ -> new")
    public static List<Item> matchAll(@Nullable final ItemType category, String in) {
        List<Item> items = category == null ? new ArrayList<>(DATABASE.values())
                : DATABASE.values().stream().filter(i -> i.category.equals(category)).collect(Collectors.toList());
        String finalIn = in.toLowerCase(Locale.ROOT);
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
                    LEVENSHTEIN_DISTANCE.apply(finalIn, i1.name),
                    LEVENSHTEIN_DISTANCE.apply(finalIn, i2.name)
            );
        });
        return items;
    }

    /**
     * Add an item to the database. Automatically saves to file.
     *
     * @param item The item to add
     * @throws InstanceAlreadyExistsException when item already exists in the database
     */
    public static void add(final Item item)
            throws InstanceAlreadyExistsException {

        // Exact copy check
        if (DATABASE.containsKey(item.hashCode())) {
            throw new InstanceAlreadyExistsException("A copy of this already exists in the database!" + item);
        }

        DATABASE.put(item.hashCode(), item);

        // Save database
        save();
    }

    /**
     * Save to file.
     */
    public static void save() {

        // Convert to CSV
        StringBuilder out = new StringBuilder(EXPECTED_HEADER).append("\n");
        DATABASE.values().stream().sorted().forEachOrdered(item -> out
                .append(item.category).append(",")
                .append(item.name).append(",")
                .append(item.worth.getAsGP()).append(",")
                .append(item.weight).append(",")
                .append(item.details).append("\n"));

        try {
            // Write to file
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(DATABASE_FILE)
            );
            writer.write(out.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save database!");
        }
    }

    private ItemDatabase() {
        // Never called
    }
}
