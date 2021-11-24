package nl.codevs.dndinventory.inventories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.codevs.dndinventory.data.Item;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Inventory {
    /**
     * Gson used to convert inventories to/from JSON.
     */
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting().serializeNulls().create();

    /**
     * List of loaded (created) inventories.
     */
    public static final List<Inventory> LOADED_INVENTORIES = new ArrayList<>();

    private static final String[] HEADER =  {"AMOUNT", "CATEGORY", "NAME", "VALUE", "WEIGHT", "STATS"};

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
     * Inventory just on name.
     * @param inventoryName The inventory name
     */
    public Inventory(final String inventoryName) {
        this(inventoryName, new ArrayList<>());
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
    public void addItem(final Item item, final int amount) {
        addItem(new InventoryItem(item, amount));
    }

    /**
     * Add an inventory item to this inventory.
     * @param inventoryItem The inventory item to add
     */
    public void addItem(final InventoryItem inventoryItem) {
        List<InventoryItem> i = new ArrayList<>();
        i.add(inventoryItem);
        addItemsBulk(i);
    }

    /**
     * Bulk add items.
     * @param inventoryItems List of inventory items
     */
    public void addItemsBulk(final List<InventoryItem> inventoryItems) {

        // Condensed items
        for (InventoryItem iItem : getItems()) {
            for (InventoryItem inventoryItem : inventoryItems) {
                if (iItem.getItem().equals(inventoryItem.getItem())) {
                    iItem.setAmount(iItem.getAmount()
                            + inventoryItem.getAmount()
                    );
                    inventoryItems.remove(inventoryItem);
                }
            }
            if (inventoryItems.isEmpty()) {
                return;
            }
        }

        // Non-condensed items
        for (InventoryItem inventoryItem : inventoryItems) {
            getItems().add(inventoryItem);
        }
    }

    /**
     * Remove a specific amount of items.
     * @param item The item type to remove
     * @param amount The amount to remove
     */
    public void removeItem(final Item item, final int amount) {
        removeItem(new InventoryItem(item, amount));
    }

    /**
     * Remove a specific amount of items.
     * @param inventoryItem The inventoryItem to remove
     */
    public void removeItem(final InventoryItem inventoryItem) {
        removeItemsBulk(Collections.singletonList(inventoryItem));
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
                i -> i.getItem().weight() * i.getAmount()
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

    @Override
    public String toString() {
        List<String[]> rawData = inventoryData();
        List<String[]> paddedData = padTable(rawData);
        String stringTable = "``` \r\n";
        final String SpaceCharacters = "   ";
        for(int i = 0; i < paddedData.size(); i++){
            for(int j = 0; j < paddedData.get(i).length; j++){
                stringTable += paddedData.get(i)[j] + SpaceCharacters;
            }
            stringTable += "\r\n";
        }
        stringTable += "```";
        return stringTable;
    }

    private List<String[]> padTable(List<String[]> l){
        int[] maxLengths = maxColumnLengths(l);
        for(int i = 0; i < maxLengths.length; i++){
            for(int j = 0; j < l.size(); j++){
                while(l.get(j)[i].length() < maxLengths[i]){ l.get(j)[i] += " ";};
            }
        }
        return l;
    }

    private int[] maxColumnLengths(List<String[]> l){
        int[] maxLengths = new int[l.get(0).length];
        for(int i = 0; i < maxLengths.length; i++){
            for(int j = 0; j < l.size(); j++){
                maxLengths[i] = Math.max(maxLengths[i], l.get(j)[i].length());
            }
        }
        return maxLengths;
    }

    private List<String[]> inventoryData(){
        List<String[]> endTable = new ArrayList<>(){};
        endTable.add(HEADER);
        for (InventoryItem item : getItems()) {
            endTable.add(item.itemData());
        }
        return endTable;
    }
    /**
     * Inventory item used for storing items in an inventory.
     */
    public static class InventoryItem {

        /**
         * The item type.
         */
        private final Item item;

        public String[] itemData(){
            String[] endRow = new String[HEADER.length];
            endRow[0] = Integer.toString(amount);
            endRow[1] = item.category().getName();
            endRow[2] = item.name();
            endRow[3] = item.worth().toString();
            endRow[4] = item.weight().toString();
            endRow[5] = item.stats();
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
            if (i1.item.category().getPos() == i2.item.category().getPos()) {
                return i2.item.name().compareToIgnoreCase(i1.item.name());
            }

            // Different positions
            return (
                    i1.item.category().getPos()
                    - i2.item.category().getPos()
            ) / Math.abs(
                    i1.item.category().getPos()
                    - i2.item.category().getPos()
            );
        };
    }
}
