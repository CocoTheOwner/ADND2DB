import nl.codevs.dndinventory.data.ItemDatabase;
import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestItemDatabase {

    @Test
    public void testDatabaseSlasherWeight() throws InstanceNotFoundException {
        assertEquals(ItemDatabase.fromName("Kick-slasher").weight, 3.0d);
    }

    @Test
    public void testDatabaseBasilardValue() throws InstanceNotFoundException {
        assertEquals(7, ItemDatabase.fromName("Basilard").worth.getAsGP());
    }

    @Test
    public void testMatchingFromName() throws InstanceNotFoundException {
        assertEquals(ItemDatabase.fromName("Assegai"), ItemDatabase.match("Assegai").get(0));
    }

    @Test
    public void testMatchingSingle1() {
        assertEquals(1d, ItemDatabase.match("Assegai").get(0).weight);
    }

    @Test
    public void testMatchingSingle2() {
        assertEquals(Money.fromString("10gp").getAsGP(), ItemDatabase.match("Boar").get(0).worth.getAsGP());
    }

    @Test
    public void testMatchingNonExact() {
        assertEquals(new Money(5000).getAsGP(), ItemDatabase.match("Hunting cat").get(0).worth.getAsGP());
    }

    @Test
    public void testDuplicateItemAdd() {
        assertThrowsExactly(
                InstanceAlreadyExistsException.class,
                () -> ItemDatabase.add(new ArrayList<>(ItemDatabase.get().values()).get(0))
        );
    }
}
