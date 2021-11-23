package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.interfaces.MoneyInventory;
import nl.codevs.dndinventory.inventories.interfaces.WeightedInventory;

import java.util.List;

public class PlayerInventory extends Inventory
        implements MoneyInventory, WeightedInventory {

    /**
     * The amount of worth in the inventory {@link Money}.
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
     * The amount of worth the inventory possesses.
     *
     * @return The Value
     */
    @Override
    public Money money() {
        return money;
    }

    /**
     * Set newMoney in the inventory.
     *
     * @param newMoney The new newMoney value
     */
    @Override
    public void setMoney(final Money newMoney) {
        this.money = newMoney;
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

    /**
     * Convert the inventory to Json.
     * @return A json string
     */
    @Override
    public String toJson() {
        return Inventory.GSON.toJson(this);
    }

    /**
     * Get amount of space left.
     * @return The amount of leftover space. Can be negative (over-encumbered)
     */
    @Override
    public double getRemainingWeight() {
        return WeightedInventory.super.getRemainingWeight() - money.getWeight();
    }
}
