import nl.codevs.dndinventory.data.Inventory;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Value;
import org.junit.jupiter.api.Test;

public class TestInventory {

    private static final Inventory SUT = new Inventory("i");
    private static final Item testItem = new Item(ItemType.WEAPONS, "Apple", new Value("5gp"), 5d, "Extra info here");

    static {
        SUT.getItems().add(new Inventory.InventoryItem(testItem, 10));
    }


    @Test
    public void testInventoryItem() {
        System.out.println(SUT.toJson());
    }

    // TODO: TestInventoryItemSorter
}
