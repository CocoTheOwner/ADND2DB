package nl.codevs.dndinventory.inventories.interfaces;

public interface IWeighted extends IInterface {

    /**
     * Max weight capacity.
     * @return The max amount of weight capacity
     */
    int getMaxWeight();

    /**
     * Get amount of space left.
     * @return The amount of leftover space. Can be negative (over-encumbered)
     */
    default double getRemainingWeight() {
        double weight = getInventory().getItems().stream()
                .mapToDouble(i -> i.getAmount()
                        * (i.getItem().weight == null
                        ? 0
                        : i.getItem().weight)
                ).sum();
        return getMaxWeight() - weight;
    }
}
