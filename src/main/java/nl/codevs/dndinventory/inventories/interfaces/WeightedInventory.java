package nl.codevs.dndinventory.inventories.interfaces;

public interface WeightedInventory extends InventoryInterface {

    /**
     * Max weight capacity.
     * @return The max amount of weight capacity
     */
    double maxWeight();

    /**
     * Get amount of space left.
     * @return The amount of leftover space. Can be negative (over-encumbered)
     */
    default double getRemainingWeight() {
        double weight = inventory().getItems().stream()
                .mapToDouble(i -> i.amount() * i.item().weight()).sum();
        return maxWeight() - weight;
    }
}
