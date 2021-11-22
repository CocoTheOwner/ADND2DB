import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestItem {

    private static final Item item = new Item("Fruits", "Apple", new Value("5gp"), 5d, "Extra info here");

    @Test
    public void testItemStat() {
        assertEquals(item.toString(),
                "Apple (Fruits) worth " + new Value("5gp") + " weighs 5.0 stats: Extra info here");
    }
}
