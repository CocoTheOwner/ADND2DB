package nl.codevs.dndinventory.data;

import nl.codevs.strinput.system.util.NGram;
import okhttp3.internal.annotations.EverythingIsNonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
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
    public static @NotNull Item fromName(final String itemName) throws InstanceNotFoundException {
        List<Item> matches = match(itemName);
        if (matches.isEmpty()) {
            throw new InstanceNotFoundException("Could not find that item!");
        } else {
            return matches.get(0);
        }
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
    public static List<Item> match(String in) {
        return match(null, in, 0.1);
    }

    /**
     * Get all values in the database that have a custom N-Gram match score above 0.1,
     * ordered by how closely they match a certain string.
     * Uses {@link NGram}.
     *
     * @param in       The input string to match with
     * @param category The category of the item
     * @param threshold The matching threshold
     * @return An array of items sorted by how close they match
     */
    @Contract("_, _, _ -> new")
    public static List<Item> match(@Nullable final ItemType category, String in, double threshold) {
        return sortByNGram(
                in,
                category == null
                        ? new ArrayList<>(DATABASE.values())
                        : DATABASE.values().stream().filter(i -> i.category.equals(category)).collect(Collectors.toList()),
                threshold
        );
    }

    /**
     * Sort a list of items by n-gram match to a string input.<br>
     * {@code itemList} is sorted and returned.<br>
     * The best match (the highest n-gram score) is first, and the lowest last.
     * @param input the input string for matching (source)
     * @param itemList the list of items nodes to sort.
     *                       <em>Not modified.</em>
     * @param threshold the minimal matching score
     * @return an array with the elements of
     * {@code itemList}, in sorted order.
     */
    @Contract(mutates = "param2")
    public static @NotNull List<Item> sortByNGram(
            @NotNull final String input,
            @NotNull final List<Item> itemList,
            final double threshold
    ) {
        ConcurrentHashMap<Item, Double> results = new ConcurrentHashMap<>();
        double[] scores = NGram.ngramMatching(input, itemList.stream().map(Item::getName).toList());
        for (int i = 0; i < scores.length; i++) {
            results.put(itemList.get(i), scores[i]);
        }

        // Get & sort
        return itemList
                .stream()
                .filter(v -> results.get(v) >= threshold)
                .sorted(Comparator.comparingInt(v -> v.getName().length()))
                .sorted(Comparator.comparingDouble(v -> -results.get(v)))
                .collect(Collectors.toList());
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
