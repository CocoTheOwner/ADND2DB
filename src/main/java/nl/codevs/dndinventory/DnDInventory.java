package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.discord.DiscordIntegration;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.dndinventory.inventories.PlayerInventory;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DnDInventory {
    public static final PlayerInventory inv =
            new PlayerInventory(
                    "Lazerus",
                    new ArrayList<>(),
                    new Money("69gp"),
                    75
            );

    public static void main(String[] args) throws IOException, LoginException, InterruptedException {
        inv.addItem(Item.Database.fromName("Sandals"), 1);

        String token = new BufferedReader(new FileReader("./token.txt")).readLine();
        DiscordIntegration di = new DiscordIntegration(token);
    }
}
