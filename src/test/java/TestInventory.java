import nl.codevs.dndinventory.data.*;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestInventory {

    private static final Inventory SUT = PlayerInventory.TEST_INVENTORY;
    private static final Item testItem = Item.makeGetItem(ItemType.WEAPONS, "Apple", Money.fromString("5gp"), 5d, "Extra info here");

    @BeforeAll
    static void addItems() {
        ItemDatabase.get().values().forEach(item -> SUT.getItems().add(
                new Inventory.InventoryItem(item, 1)
        ));
        SUT.getItems().add(new Inventory.InventoryItem(testItem, 10));
    }

    @Test
    public void testInventorySorting() {
        assertEquals(SUT.getItems().get(SUT.getItems().size() - 1).getItem().name, testItem.name);
    }

    // TODO: TestInventoryItemSorter
}
