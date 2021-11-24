package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.discord.DiscordIntegration;
import nl.codevs.dndinventory.inventories.PlayerInventory;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class DnDInventory {

    private DnDInventory() {
        // Never called
    }

    /**
     * Testing inventory.
     */
    public static final PlayerInventory INV =
            new PlayerInventory(
                    "Lazerus",
                    new ArrayList<>(),
                    new Money("69gp"),
                    75
            );

    /**
     * Main function.
     * @param args Java arguments
     * @throws IOException Thrown when token file not found
     * @throws LoginException
     *      Thrown when the {@link DiscordIntegration} cannot login
     * @throws InterruptedException
     *      Thrown when the {@link DiscordIntegration} is interrupted
     */
    public static void main(final String[] args)
            throws IOException, LoginException, InterruptedException {
        INV.addItem(Item.Database.fromName("Sandals"), 1);

        String token = new BufferedReader(
                new FileReader("./token.txt")
        ).readLine();
        DiscordIntegration di = new DiscordIntegration(token);
    }
}
