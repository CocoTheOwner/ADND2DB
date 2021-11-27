import nl.codevs.dndinventory.inventories.PlayerInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWeighted {
    @Test
    public void testWeightedMaxWeight() {
        assertEquals(55, PlayerInventory.TEST_INVENTORY.getMaxWeight());
    }
}
