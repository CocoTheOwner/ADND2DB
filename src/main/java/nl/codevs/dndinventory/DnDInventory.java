package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.data.StonePouch;
import nl.codevs.dndinventory.discord.Bot;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import nl.codevs.dndinventory.inventories.interfaces.ILevel;
import nl.codevs.strinput.examples.discord.DiscordCenter;
import nl.codevs.strinput.system.StrInput;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;

public final class DnDInventory {

    private static final DiscordCenter center = new DiscordCenter(
            new File("strSettings"),
            new Commands()
    );

    private DnDInventory() {
        // Never called
    }

    /**
     * Testing inventory.
     */
    public static PlayerInventory INV = new PlayerInventory(
        "testPlayer",
        new ArrayList<>(),
        new Money("10gp"),
        new StonePouch(),
        ILevel.CharacterClass.FIGHTER,
        0,
        1,
        15,
        14,
        12,
        18,
        16,
        12,
        9,
        8
    );

    /**
     * Main function.
     * @param args Java arguments
     * @throws IOException Thrown when token file not found
     */
    public static void main(final String[] args)
            throws IOException {
        INV.addItem(Item.Database.fromName("Sandals"), 1);
        INV.getStones().add(new StonePouch.Stone(
                new Money("100gp"),
                "stone",
                "blue",
                ""
        ));
        INV.save(true);

        try {
            new Bot(
                    new BufferedReader(new FileReader("token.txt")).readLine(),
                    "!",
                    Commands.class.getDeclaredAnnotation(StrInput.class).name(),
                    center
            );
        } catch (LoginException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
