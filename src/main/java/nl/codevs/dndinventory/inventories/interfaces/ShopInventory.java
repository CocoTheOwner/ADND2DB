package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Value;

public interface ShopInventory extends InventoryInterface {

    /**
     * Item modifier for a specific category.
     * @param type The item type (category)
     * @return The modifier for the type
     */
    double modifierFor(ItemType type);

    /**
     * Item modified for individual items.
     * @param item The item
     * @return The modifier for the item
     */
    double modifierFor(Item item);

    /**
     * The amount the shopkeeper will pay for an item.
     * @param item The item to get the price for
     * @return The price
     */
    default Value willPayFor(final Item item) {
        return Value.fromValueAndFactor(
                item.value(),
                modifierFor(item.category()) * modifierFor(item)
        );
    }
}
