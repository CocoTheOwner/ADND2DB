package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Money;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Temp {
    public static void main(String[] args) throws FileNotFoundException {
        new BufferedReader(new FileReader("./DNDInventories/databases/stones.db.csv")).lines().forEachOrdered(l -> {
            String[] split = l.split(",");
            Item.makeGetItem(ItemType.GEMSTONES, split[0]+ " (" + split[1] + ")", new Money(Money.Coin.GP, Integer.parseInt(split[2])), 0, "");
        });
    }
}
