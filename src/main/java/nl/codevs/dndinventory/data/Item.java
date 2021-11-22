package nl.codevs.dndinventory.data;

public record Item(
        ItemType category,
        String name,
        Value value,
        Double weight,
        String stats
) {

    @Override
    public String toString() {
        return name + " (" + category.getName() + ")"
                + " worth " + value.toString()
                + " weighs " + weight
                + (stats.isEmpty() ? "" : " stats: " + stats);
    }
}
