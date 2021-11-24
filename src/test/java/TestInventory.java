import nl.codevs.dndinventory.data.*;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestInventory {

    private static final Inventory SUT = new PlayerInventory("i", new ArrayList<>(), new Money(), 75);
    private static final Item testItem = new Item(Item.Type.WEAPONS, "Apple", new Money("5gp"), 5d, "Extra info here");

    @BeforeAll
    static void addItems() {
        Item.Database.getItems().forEach(item -> SUT.getItems().add(
                new Inventory.InventoryItem(item, 1)
        ));
        SUT.getItems().add(new Inventory.InventoryItem(testItem, 10));
    }

    @Test
    public void testInventorySorting() {
        assertEquals(SUT.getItems().get(SUT.getItems().size() - 1).getItem().name(), testItem.name());
    }

    // TODO: TestInventoryItemSorter
}
