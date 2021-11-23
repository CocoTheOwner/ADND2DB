package nl.codevs.dndinventory.inventories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.codevs.dndinventory.data.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Inventory {
    /**
     * Gson used to convert inventories to/from JSON.
     */
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting().serializeNulls().create();

    /**
     * List of loaded (created) inventories
     */
    public static final List<Inventory> loadedInventories = new ArrayList<>();

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
        loadedInventories.add(this);
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
     * Add an item to this inventory
     * @param item The item to add
     * @param amount The amount of this item to add
     */
    public void addItem(Item item, int amount) {
        addItem(new InventoryItem(item, amount));
    }

    /**
     * Add an inventory item to this inventory
     * @param inventoryItem The inventory item to add
     */
    public void addItem(InventoryItem inventoryItem) {
        getItems().add(inventoryItem);
    }


    /**
     * Inventory item used for storing items in an inventory.
     * @param item The item type
     * @param amount The amount of the item
     */
    public static record InventoryItem(Item item, int amount) {

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
