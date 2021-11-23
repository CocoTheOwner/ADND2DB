import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItemDatabase {

    @Test
    public void testDatabaseSlasherWeight() {
        assertEquals(Item.Database.fromName("Kick-slasher").weight(), 3.0d);
    }

    @Test
    public void testDatabaseBasilardValue() {
        assertEquals(Item.Database.fromName("Basilard").value().getAsGP(), 7);
    }

    @Test
    public void testMatchingFromName() {
        assertEquals(Item.Database.fromName("Assegai"), Item.Database.matchAll("Assegai").get(0));
    }

    @Test
    public void testMatchingSingle1() {
        assertEquals(1d, Item.Database.matchAll("Assegai").get(0).weight());
    }

    @Test
    public void testMatchingSingle2() {
        assertEquals(new Value("10gp").getAsGP(), Item.Database.matchAll("Boar").get(0).value().getAsGP());
    }

    @Test
    public void testMatchingNonExact() {
        assertEquals(new Value(5000).getAsGP(), Item.Database.matchAll("Hunting cat").get(0).value().getAsGP());
    }
}
