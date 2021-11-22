package nl.codevs.dndinventory.data;

import java.util.Comparator;

public record Item(
        ItemType category,
        String name,
        Value value,
        Double weight,
        String stats
) {

    @Override
    public String toString() {
        return name + " (" + category.name() + ")"
                + " worth " + value.toString()
                + " weighs " + weight
                + (stats.isEmpty() ? "" : " stats: " + stats);
    }
}
