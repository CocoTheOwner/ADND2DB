package nl.codevs.dndinventory.inventories.interfaces;

public interface IWeighted extends IStats {

    /**
     * Max weight capacity.
     * @return The max amount of weight capacity
     */
    default int getMaxWeight() {
        return switch (getStrength()) {
            case 1 -> 0;
            case 2 -> 1;
            case 3 -> 5;
            case 4, 5 -> 10;
            case 6, 7 -> 20;
            case 8, 9 -> 35;
            case 10, 11 -> 40;
            case 12, 13 -> 45;
            case 14, 15 -> 55;
            case 16 -> 70;
            case 17 -> 85;
            case 18 -> 110;
            default -> throw new IllegalStateException("Unexpected value: " + getStrength());
        };
    };

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
