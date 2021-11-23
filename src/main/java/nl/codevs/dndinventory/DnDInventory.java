package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.PlayerInventory;

import java.util.ArrayList;

public class DnDInventory {
    private static final PlayerInventory inv =
            new PlayerInventory(
                    "Lazerus",
                    new ArrayList<>(),
                    new Money("69gp"),
                    75
            );
    public static void main(String[] args) {
        inv.addItem(Item.Database.fromName("Sandals"), 1);
        System.out.println(inv.toJson());
    }
}
