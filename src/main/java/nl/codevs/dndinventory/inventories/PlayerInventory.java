package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.data.Inventory;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.interfaces.MoneyInventory;
import nl.codevs.dndinventory.inventories.interfaces.WeightedInventory;

import java.util.List;

public class PlayerInventory extends Inventory
        implements MoneyInventory, WeightedInventory {

    /**
     * The amount of money in the inventory {@link Money}.
     */
    private Money money;

    /**
     * The maximal carry weight for this inventory.
     */
    private final double maxWeight;

    /**
     * Create a player inventory.
     * @param inventoryName The inventory name
     * @param startingItems The starting inventory items
     * @param startingMoney The amount of starting capital
     * @param maximalEncumbrance The maximal carry capacity
     */
    public PlayerInventory(
            final String inventoryName,
            final List<InventoryItem> startingItems,
            final Money startingMoney,
            final double maximalEncumbrance
    ) {
        super(inventoryName, startingItems);
        money = startingMoney;
        maxWeight = maximalEncumbrance;
    }

    /**
     * The inventory this applies to.
     *
     * @return The inventory
     */
    @Override
    public Inventory inventory() {
        return this;
    }

    /**
     * The amount of money the inventory possesses.
     *
     * @return The Value
     */
    @Override
    public Money money() {
        return money;
    }

    /**
     * Set money in the inventory.
     *
     * @param money The new money value
     */
    @Override
    public void setMoney(final Money money) {
        this.money = money;
    }

    /**
     * Max weight capacity.
     *
     * @return The max amount of weight capacity
     */
    @Override
    public double maxWeight() {
        return maxWeight;
    }
}
