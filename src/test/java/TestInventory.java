import nl.codevs.dndinventory.data.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestInventory {

    private static final Inventory SUT = new Inventory("i");
    private static final Item testItem = new Item(ItemType.WEAPONS, "Apple", new Value("5gp"), 5d, "Extra info here");

    @BeforeAll
    static void addItems() {
        ItemDatabase.getItems().forEach(item -> SUT.getItems().add(
                new Inventory.InventoryItem(item, 1)
        ));
        SUT.getItems().add(new Inventory.InventoryItem(testItem, 10));
    }

    @Test
    public void testInventorySorting() {
        assertEquals(SUT.getItems().get(SUT.getItems().size() - 1).item().name(), testItem.name());
    }

    // TODO: TestInventoryItemSorter
}
