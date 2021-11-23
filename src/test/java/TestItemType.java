import nl.codevs.dndinventory.data.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestItemType {
    @Test
    public void testFromStringConst() {
        assertEquals(Item.Type.WEAPONS, Item.Type.fromString("WEAPONS"));
    }
    @Test
    public void testFromStringName() {
        assertEquals(Item.Type.WEAPONS, Item.Type.fromString("Weapons"));
    }
    @Test
    public void testFromStringNonSame() {
        assertEquals(Item.Type.HARNESS, Item.Type.fromString("Tack and Harness"));
    }
    @Test
    public void testFromStringFails() {
        assertThrows(RuntimeException.class, () -> Item.Type.fromString("Fail"));
    }
    @Test
    public void testGetName() {
        assertEquals(Item.Type.WEAPONS.getName(), "Weapons");
    }
    @Test
    public void testGetPos() {
        assertEquals(Item.Type.ANIMALS.getPos(), 4);
    }
}
