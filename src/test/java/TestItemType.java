import nl.codevs.dndinventory.data.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestItemType {
    @Test
    public void testFromStringConst() {
        assertEquals(ItemType.WEAPONS, ItemType.fromString("WEAPONS"));
    }
    @Test
    public void testFromStringName() {
        assertEquals(ItemType.WEAPONS, ItemType.fromString("Weapons"));
    }
    @Test
    public void testFromStringNonSame() {
        assertEquals(ItemType.HARNESS, ItemType.fromString("Tack and Harness"));
    }
    @Test
    public void testFromStringFails() {
        assertThrows(RuntimeException.class, () -> ItemType.fromString("Fail"));
    }
    @Test
    public void testGetName() {
        assertEquals(ItemType.WEAPONS.getName(), "Weapons");
    }
    @Test
    public void testGetPos() {
        assertEquals(5, ItemType.ANIMALS.getPos());
    }
}
