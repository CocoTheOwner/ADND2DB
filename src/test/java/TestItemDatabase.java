import nl.codevs.dndinventory.data.ItemDatabase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItemDatabase {

    @Test
    public void testDatabaseSlasherWeight() {
        assertEquals(ItemDatabase.fromName("Kick-slasher").weight(), 3.0d);
    }

    @Test
    public void testDatabaseBasilardValue() {
        assertEquals(ItemDatabase.fromName("Basilard").value().getAsGP(), 7);
    }
}
