import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestItem {

    private static final Item item = Item.makeGetItem(Item.Type.WEAPONS, "Apple", new Money("5gp"), 5d, "Extra info here");

    @Test
    public void testItemStat() {
        assertEquals(item.toString(),
                "Apple (Weapons) worth " + new Money("5gp") + " weighs 5.0 stats: Extra info here");
    }

    @Test
    public void testItemWeight() {
        assertEquals(5d, item.weight);
    }
}
