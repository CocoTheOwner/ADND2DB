import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayerInventory {

    private static final PlayerInventory SUT = new PlayerInventory("hi", new ArrayList<>(), new Money(5), 10);

    static {
        SUT.addItem(new Item(Item.Type.WEAPONS, "Sword", new Money(5), 1d, ""), 5);
    }

    @Test
    public void testMaxWeight() {
        assertEquals(SUT.getRemainingWeight(), 10 - 5 * 1d - new Money(5).getWeight());
    }
}
