package nl.codevs.dndinventory.inventories;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import okhttp3.internal.annotations.EverythingIsNonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

@EverythingIsNonNull
public abstract class Inventory {

    /**
     * Gson used to convert inventories to/from JSON.
     */
    public static final Gson GSON = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getAnnotation(Exclude.class) != null;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).setPrettyPrinting().serializeNulls().create();

    /**
     * Inventory directory.
     */
    public static final File INVENTORY_DIRECTORY = new File("./DNDInventories/");

    static {
        INVENTORY_DIRECTORY.mkdirs();
    }

    /**
     * List of loaded (created) inventories.
     */
    public static final List<Inventory> LOADED_INVENTORIES = new ArrayList<>();


    static {
        try {
            LOADED_INVENTORIES.addAll(instantiateAllInventories(
                    PlayerInventory.class,
                    AnimalsInventory.class
            ));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Discord table headers.
     */
    private static final String[] HEADER =
            {"AMOUNT", "CATEGORY", "NAME", "VALUE", "WEIGHT", "STATS"};

    /**
     * Spacing between headers in {@code #toString}.
     */
    private static final String SPACING_CHARACTER = "\t";


    /**
     * Items in inventory.
     */
    private final List<InventoryItem> items;

    /**
     * Get items in inventory.
     * @return Items in inventory
     */
    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * Name of inventory.
     */
    private final String name;

    /**
     * Get inventory name.
     * @return Inventory name
     */
    public String getName() {
        return name;
    }

    /**
     * Generate from an existing, loaded, inventory.
     * @param inventoryName The name of the inventory
     * @param inventoryItems The inventory mapping
     */
    public Inventory(
            final String inventoryName,
            final List<InventoryItem> inventoryItems
    ) {
        name = inventoryName;
        items = inventoryItems;
        items.sort(InventoryItem.ITEM_COMPARATOR);
        LOADED_INVENTORIES.add(this);
    }

    /**
     * Create an inventory from a file.
     * @param fromFile The file to use to create the inventory.
     * @return {@link Inventory} object from json file
     * @throws FileNotFoundException If the file does not exist
     */
    public static Inventory fromJson(final File fromFile)
            throws FileNotFoundException {
        return GSON.fromJson(new FileReader(fromFile), Inventory.class);
    }

    /**
     * Try loading an inventory by name (using files stored in the directory stored in INVENTORY_DIRECTORY)
     * @param name the name of the inventory to load
     * @return an {@link Inventory}
     * @throws FileNotFoundException if the file does not exist
     */
    public static Inventory fromName(final String name) throws FileNotFoundException {
        return fromJson(new File(name));
    }

    /**
     * Instantiate all inventories in the {@code INVENTORY_DIRECTORY/type#getSimpleName/} folder.
     * @param type the type to instantiate
     * @return a list of inventory instances,of the given type,
     *      for each valid Json in the aforementioned directory.
     */
    @SafeVarargs
    public static List<Inventory> instantiateAllInventories(Class<? extends Inventory>... type) throws FileNotFoundException {
        List<Inventory> inventories = new ArrayList<>();
        for (Class<? extends Inventory> aClass : type) {
            File target = new File(INVENTORY_DIRECTORY + "/" + aClass.getSimpleName().toLowerCase(Locale.ROOT));
            if (!target.exists()) {
                return inventories;
            }
            File[] files = target.listFiles(file -> file.getPath().endsWith(".json"));
            assert files != null;
            for (File inventoryFile : files) {
                inventories.add(GSON.fromJson(new FileReader(inventoryFile), (Type) aClass));
            }

        }
        return inventories;
    }

    /**
     * Save all inventories.
     * @param overwrite set to true to overwrite existing inventory files
     */
    public static void saveAll(boolean overwrite) {
        LOADED_INVENTORIES.forEach(i -> {
            try {
                i.save(overwrite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Save the inventory to a file in INVENTORY_DIRECTORY
     * @param overwrite set to true to overwrite existing inventory files
     * @throws FileAlreadyExistsException if the inventory already exists
     */
    public void save(boolean overwrite) throws IOException {
        File targetDir = new File(INVENTORY_DIRECTORY + "/" + getClass().getSimpleName().toLowerCase(Locale.ROOT));
        File targetFile = new File(targetDir + "/" + getName().toLowerCase(Locale.ROOT) + ".json");
        targetDir.mkdirs();

        if (!overwrite && targetFile.exists()) {
            System.out.println(toJson());
            throw new FileAlreadyExistsException("Inventory by name: " + getName() + " already exists and overwrite is off");
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile));
            bw.write(toJson());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert the inventory to Json.
     * @return A json string
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Get the inventory from Json.
     * @param json The json string to use
     * @return The inventory object
     */
    public static Inventory fromJson(final String json) {
        return GSON.fromJson(json, Inventory.class);
    }

    /**
     * Add an item to this inventory.
     * Use addItemBulk wherever possible because it's faster.
     * @param item The item to add
     * @param amount The amount of this item to add
     */
    public void addItems(final Item item, final int amount) {
        addItems(new InventoryItem(item, amount));
    }

    /**
     * Add an inventory item to this inventory.
     * @param InventoryItems The inventory item to add
     */
    public void addItems(final InventoryItem... InventoryItems) {

        List<InventoryItem> items = new ArrayList<>(List.of(InventoryItems));


        // Condensed items
        for (InventoryItem iItem : getItems()) {
            for (InventoryItem inventoryItem : new ArrayList<>(items)) {
                if (iItem.getItem().equals(inventoryItem.getItem())) {
                    iItem.setAmount(iItem.getAmount()
                            + inventoryItem.getAmount()
                    );
                    items.remove(inventoryItem);
                }
            }
            if (items.isEmpty()) {
                return;
            }
        }

        // Non-condensed items
        for (InventoryItem inventoryItem : items) {
            getItems().add(inventoryItem);
        }
    }

    /**
     * Bulk add items.
     * @param inventoryItems List of inventory items
     */
    public void addItems(final List<InventoryItem> inventoryItems) {
    }

    /**
     * Remove a specific amount of items.
     * @param item The item type to remove
     * @param amount The amount to remove
     * @return the inventory item if it could not be removed
     * (or with lower amounts of items if not all could be removed)
     */
    public @Nullable InventoryItem removeItem(final Item item, final int amount) {
        return removeItem(new InventoryItem(item, amount));
    }

    /**
     * Remove a specific amount of items.
     * @param inventoryItem The inventoryItem to remove
     * @return the inventory item if it could not be removed, null if it could
     * (or with lower amounts of items if not all could be removed)
     */
    public @Nullable InventoryItem removeItem(final InventoryItem inventoryItem) {
        List<InventoryItem> x = removeItemsBulk(Collections.singletonList(inventoryItem));
        if (x.isEmpty()) {
            return null;
        } else {
            return x.get(0);
        }
    }

    /**
     * Remove items in bulk.
     * @param inventoryItems the items to remove
     * @return remaining items that cannot be removed
     * because they are not part of this inventory
     */
    public List<InventoryItem> removeItemsBulk(
            final List<InventoryItem> inventoryItems
    ) {

        // Condensed items
        for (InventoryItem iItem : getItems()) {
            for (InventoryItem inventoryItem : inventoryItems) {
                if (iItem.getItem().equals(inventoryItem.getItem())) {

                    // Items match
                    int newAmount = iItem.getAmount()
                            - inventoryItem.getAmount();

                    if (newAmount == 0) {
                        inventoryItems.remove(inventoryItem);
                        getItems().remove(iItem);
                    } else if (newAmount < 0) {
                        // Too many items to remove
                        getItems().remove(iItem);
                        inventoryItem.setAmount(
                                Math.abs(newAmount)
                        ); // Remaining to-remove items
                    } else {
                        // More items remain than that are removed
                        inventoryItems.remove(inventoryItem);
                        iItem.setAmount(newAmount);
                    }
                }
            }
            if (inventoryItems.isEmpty()) {
                // Done
                return inventoryItems;
            }
        }

        return inventoryItems;
    }

    /**
     * Remove random items up to a certain weight.
     * @param targetWeight the weight to target for removal
     * @return the items that were removed
     */
    public List<InventoryItem> removeRandomItems(final double targetWeight) {
        List<InventoryItem> removed = new ArrayList<>();

        Random r = new Random();

        emptying: while (removed.stream().mapToDouble(
                i -> (i.getItem().weight != null ? i.getItem().weight * i.getAmount() : 0)
        ).sum() < targetWeight) {
            if (getItems().isEmpty()) {
                System.out.println(
                        "Inventory empty,"
                        + "no further items could be removed"
                );
                return removed;
            }

            InventoryItem item = getItems().get(r.nextInt(getItems().size()));
            item.setAmount(item.getAmount() - 1);

            for (InventoryItem inventoryItem : removed) {
                if (inventoryItem.getItem().equals(item.getItem())) {
                    inventoryItem.setAmount(inventoryItem.getAmount() + 1);
                }
                continue emptying;
            }

            // Item not removed before
            removed.add(new InventoryItem(item.getItem(), 1));
        }

        return removed;
    }

    /**
     * Convert to string.
     * @return String representation
     */
    @Override
    public String toString() {
        List<String[]> rawData = inventoryData();
        List<String[]> paddedData = padTable(rawData);
        StringBuilder stringTable = new StringBuilder("```asciidoc\n");
        for (String[] paddedDatum : paddedData) {
            for (String s : paddedDatum) {
                stringTable.append(s).append(SPACING_CHARACTER);
            }
            stringTable.append("\n");
        }
        stringTable.append("```");
        return stringTable.toString();
    }

    /**
     * Add padding between elements of the table to align the characters.
     * Assumes characters are of equal size.
     * <p>Modifies {@code l}</p>
     * @param l the list of lines to align.
     * @return Aligned list of characters
     */
    private List<String[]> padTable(final List<String[]> l) {
        int[] maxLengths = maxColumnLengths(l);

        // Headers + Table elements
        for (int i = 0; i < maxLengths.length; i++) {
            for (String[] strings : l) {
                while (strings[i].length() < maxLengths[i]) {
                    strings[i] += " ";
                }
            }
        }

        // Separator line
        String[] separator = new String[maxLengths.length];
        for (int i = 0; i < separator.length; i++) {
            separator[i] = "=".repeat(maxLengths[i]);
        }
        l.add(1, separator);
        l.add(0, separator);
        l.add(l.size() - 1, separator);

        return l;
    }

    private int[] maxColumnLengths(List<String[]> l) {
        int[] maxLengths = new int[l.get(0).length];
        for (int i = 0; i < maxLengths.length; i++) {
            for (String[] strings : l) {
                maxLengths[i] = Math.max(maxLengths[i], strings[i].length());
            }
        }
        return maxLengths;
    }

    private List<String[]> inventoryData() {
        List<String[]> endTable = new ArrayList<>();
        endTable.add(HEADER);
        for (InventoryItem item : getItems()) {
            endTable.add(item.itemData());
        }
        endTable.add(inventoryStats());
        return endTable;
    }

    /**
     * Statistics for the inventory (sums for items).
     * @return String array of stats (equal sized to HEADER)
     */
    private String[] inventoryStats() {
        String amount = String.valueOf(getItems().stream().mapToInt(InventoryItem::getAmount).sum());
        String category = "TOTALS";
        String name = "";
        String value = new Money(getItems().stream().mapToDouble(i -> i.getAmount() * i.getItem().worth.getAsGP()).sum()).toString();
        String weight = String.valueOf(getItems().stream().mapToDouble(i -> i.getAmount() * (i.getItem().weight == null ? 0 : i.getItem().weight)).sum());
        String stats = getAdditionalStats();
        return new String[]{amount, category, name, value, weight, stats};
    }

    /**
     * Add any additional stats to show in the "stats" column.
     * @return A string for the stats to display
     */
    protected abstract String getAdditionalStats();

    /**
     * Inventory item used for storing items in an inventory.
     */
    @EverythingIsNonNull
    public static class InventoryItem {

        /**
         * The item type.
         */
        private final Item item;

        /**
         * Item data retrieval.
         * @return Array of strings
         */
        public String[] itemData() {
            String[] endRow = new String[HEADER.length];
            endRow[0] = Integer.toString(amount);
            endRow[1] = item.category.getName();
            endRow[2] = item.name;
            endRow[3] = new Money(amount * item.worth.getAsGP())
                    + " (" + amount + "*" + item.worth.getAsGP() + "gp)";
            endRow[4] = item.weight == null ? "0" : amount * item.weight
                    + " (" + amount + "*" + item.weight + ")";
            endRow[5] = item.details;
            return endRow;
        }

        /**
         * The amount of items of the `item` type in this inventory item.
         */
        private int amount;

        /**
         * Whether this inventory item is loot or not.
         */
        private boolean loot;

        /**
         * Get the item type.
         * @return item type
         */
        public Item getItem() {
            return item;
        }

        /**
         * Get amount.
         * @return the amount
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Set amount.
         * @param newAmount new amount
         */
        public void setAmount(final int newAmount) {
            amount = newAmount;
        }

        /**
         * Get loot.
         * @return loot or not
         */
        public boolean isLoot() {
            return loot;
        }

        /**
         * Set loot.
         * @param isLoot Loot or not
         */
        public void setLoot(final boolean isLoot) {
            loot = isLoot;
        }

        /**
         * Create an inventory item.
         * @param itemType The item type
         * @param itemAmount The item amount
         * @param isLoot Whether it is loot
         */
        public InventoryItem(
                final Item itemType,
                final int itemAmount,
                final boolean isLoot
        ) {
            item = itemType;
            amount = itemAmount;
            loot = isLoot;
        }

        /**
         * Create an inventory item.
         * @param itemType The item type
         * @param itemAmount The item amount
         */
        public InventoryItem(final Item itemType, final int itemAmount) {
            this(itemType, itemAmount, false);
        }

        /**
         * Sorting comparator for items.
         * First sorts by category:
         * If the category pos' are equal, they sort by name
         * If the category pos' are different,
         * they sort by 'lowest pos to highest pos'
         */
        public static final Comparator<InventoryItem>
                ITEM_COMPARATOR = (i1, i2) -> {

            // Equal positions
            if (i1.item.category.getPos() == i2.item.category.getPos()) {
                return i2.item.name.compareToIgnoreCase(i1.item.name);
            }

            // Different positions
            return (
                    i1.item.category.getPos()
                    - i2.item.category.getPos()
            ) / Math.abs(
                    i1.item.category.getPos()
                    - i2.item.category.getPos()
            );
        };
    }
}
