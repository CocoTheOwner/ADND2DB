import nl.codevs.dndinventory.inventories.PlayerInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLevel {
    /**
     * Tests:
     * Fighter XP & Warrior IHealth Dice
     * Paladin XP & ^
     * Mage XP & Wizard IHealth Dice
     * Specialist XP & ^
     * Cleric XP & Priest IHealth Dice
     * Druid XP & ^
     */


    @Test
    public void testPotentialLevel() {
        assertEquals(4, PlayerInventory.TEST_INVENTORY.getPotentialLevel());
    }

    @Test
    public void testTrainableLevels() {
        assertEquals(2, PlayerInventory.TEST_INVENTORY.getTrainableLevels());
    }
}
