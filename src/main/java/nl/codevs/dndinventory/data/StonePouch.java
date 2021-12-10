package nl.codevs.dndinventory.data;

import java.util.ArrayList;

public class StonePouch extends ArrayList<StonePouch.Stone> {

    public record Stone(
            Money value,
            String name,
            String color,
            String details
    ) {

    }
}
