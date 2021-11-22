package nl.codevs.dndinventory.data;

public record Item(
        String category,
        String name,
        Value value,
        Double weight,
        String stats
) {

    @Override
    public String toString() {
        return name + " (" + category + ")"
                + " worth " + value.toString()
                + " weighs " + weight
                + (stats.isEmpty() ? "" : " stats: " + stats);
    }
}
