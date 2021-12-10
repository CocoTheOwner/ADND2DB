import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestItemDatabase {

    @Test
    public void testDatabaseSlasherWeight() {
        assertEquals(Item.Database.fromName("Kick-slasher").weight, 3.0d);
    }

    @Test
    public void testDatabaseBasilardValue() {
        assertEquals(Item.Database.fromName("Basilard").worth.getAsGP(), 7);
    }

    @Test
    public void testMatchingFromName() {
        assertEquals(Item.Database.fromName("Assegai"), Item.Database.matchAll("Assegai").get(0));
    }

    @Test
    public void testMatchingSingle1() {
        assertEquals(1d, Item.Database.matchAll("Assegai").get(0).weight);
    }

    @Test
    public void testMatchingSingle2() {
        assertEquals(Money.fromString("10gp").getAsGP(), Item.Database.matchAll("Boar").get(0).worth.getAsGP());
    }

    @Test
    public void testMatchingNonExact() {
        assertEquals(new Money(5000).getAsGP(), Item.Database.matchAll("Hunting cat").get(0).worth.getAsGP());
    }

    @Test
    public void testDuplicateItemAdd() {
        assertThrowsExactly(
                InvalidParameterException.class,
                () -> Item.Database.addItem(Item.Database.getItems().get(0))
        );
    }
}
