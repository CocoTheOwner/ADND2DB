package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Money;

public interface IShop extends IInterface {

    /**
     * Item modifier for a specific category.
     * @param type The item type (category)
     * @return The modifier for the type
     */
    double getModifierFor(ItemType type);

    /**
     * Item modified for individual items.
     * @param item The item
     * @return The modifier for the item
     */
    double getModifierFor(Item item);

    /**
     * The amount the shopkeeper will pay for an item.
     * @param item The item to get the price for
     * @return The price
     */
    default Money getWouldPay(final Item item) {
        return Money.fromValueAndFactor(
                item.worth,
                getModifierFor(item.category) * getModifierFor(item)
        );
    }
}
