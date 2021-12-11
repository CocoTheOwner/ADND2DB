package nl.codevs.dndinventory.inventories.interfaces;

public interface IWeightedPlayer extends IStats, IWeighted {

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
}
