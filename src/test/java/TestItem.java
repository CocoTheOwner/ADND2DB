import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestItem {

    private static final Item ITEM = Item.makeGetItem(ItemType.WEAPONS, "Alhulak", Money.fromString("9gp"), 9, "(SM) 1d6 (L) 1d6");

    @Test
    public void testItemStat() {
        assertEquals(ITEM.toString(),
                "Alhulak (Weapons) worth " + Money.fromString("9gp") + " weighs 9.0 stats: (SM) 1d6 (L) 1d6");
    }

    @Test
    public void testItemWeight() {
        assertEquals(9d, ITEM.weight);
    }
}
