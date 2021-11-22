package nl.codevs.dndinventory.data;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemDatabase {

    private ItemDatabase() {
        // Never called
    }

    /**
     * Separator in database (csv).
     */
    private static final String SEPARATOR = "\t";

    /**
     * URL to item database.
     */
    private static URL url;

    static {
        try {
            url = new URL(
                    "https://raw.githubusercontent.com/"
                    + "CocoTheOwner/ADND2DB/main/fulllist.csv"
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Items in database.
     */
    private static final List<Item> ITEMS = new ArrayList<>();

    /**
     * Get all items in the database.
     * @return Items
     */
    public static List<Item> getItems() {
        return ITEMS;
    }

    /**
     * Items in database, findable by name.
     */
    private static final ConcurrentHashMap<String, Item>
            ITEMS_BY_NAME = new ConcurrentHashMap<>();

    static {
        try {
            setupMaps(getRawData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup mappings.
     * @param raw Raw data from url
     */
    private static void setupMaps(final List<String> raw) {

        raw.remove(0); // header

        // Print info
        System.out.println("Loading database with " + raw.size() + " items");

        // Lines formatted as "###|ItemName"
        for (String line : raw) {
            itemizeLine(line);
        }

        // Print
        System.out.println(ITEMS.size() + " items loaded");

    }

    /**
     * Itemize a line.
     * @param line The line to parse
     */
    private static void itemizeLine(final String line) {
        try {
            List<String> split =
                    new ArrayList<>(List.of(line.split(SEPARATOR)));

            // Required
            ItemType category;
            String name;
            Value value = new Value(0);

            // Optional
            Double weight = null;
            String stats = "";

            // Case types
            final int allSpecified = 5;
            final int noStats = 4;
            final int noWeightNoStats = 3;
            final int nameAndCategoryOnly = 2;

            // Indexes
            final int statIndex = 4;
            final int perOrWeightIndex = 3;
            final int valueIndex = 2;
            final int nameIndex = 1;
            final int categoryIndex = 0;

            // Note the 'fall-through' behaviour of switches.
            // On case 5, case 4 and 3 are also ran.
            switch (split.size()) {
                case allSpecified: stats = split.get(statIndex);
                case noStats: {
                    if (split.get(perOrWeightIndex).startsWith("per ")) {
                        stats = split.get(perOrWeightIndex);
                    } else {
                        weight = Double.parseDouble(
                                split.get(perOrWeightIndex)
                        );
                    }
                }
                case noWeightNoStats: {
                    value = new Value(split.get(valueIndex));
                }
                case nameAndCategoryOnly: {
                    name = split.get(nameIndex);
                    category = ItemType.fromString(split.get(categoryIndex));
                    break;
                }
                default: {
                    throw new RuntimeException(
                            "Line has no(t enough of) separator character, "
                                    + SEPARATOR
                                    + " (" + line + ")"
                    );
                }
            }

            Item item = new Item(category, name, value, weight, stats);
            ITEMS.add(item);
            ITEMS_BY_NAME.put(name, item);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(line);
        }
    }

    /**
     * Get raw data from preset {@code url}.
     * @return Raw data in a list of strings
     * @throws IOException If a stream fails
     */
    private static List<String> getRawData() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(url.openConnection().getInputStream())
        );
        List<String> data = new ArrayList<>();
        String line;

        // read each line and write to System.out
        while ((line = br.readLine()) != null) {
            data.add(line);
        }

        return data;
    }

    /**
     * Get an item from the database based on name.
     * @param in The input string
     * @return The item
     */
    public static Item fromName(final String in) {
        return ITEMS_BY_NAME.get(in);
    }

    /**
     * Get all values in the database,
     * ordered by how closely they match a certain string.
     * Uses {@code LevenshteinDistance} from Apache Commons Text.
     * @param in The input string to match with
     * @return An array of items sorted by how close they match
     */
    public static List<Item> matchAll(final String in) {
        List<Item> items = new ArrayList<>(ITEMS);
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
}
