package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;

public interface ShopInventory extends InventoryInterface {

    /**
     * Item modifier for a specific category.
     * @param type The item type (category)
     * @return The modifier for the type
     */
    double modifierFor(Item.Type type);

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
    default Money willPayFor(final Item item) {
        return Money.fromValueAndFactor(
                item.worth,
                modifierFor(item.category) * modifierFor(item)
        );
    }
}
