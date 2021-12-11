package nl.codevs.dndinventory;

import nl.codevs.dndinventory.data.*;
import nl.codevs.dndinventory.discord.Bot;
import nl.codevs.dndinventory.discord.Commands;
import nl.codevs.dndinventory.discord.expansions.InventoryParameter;
import nl.codevs.dndinventory.discord.expansions.ItemParameter;
import nl.codevs.dndinventory.inventories.AnimalsInventory;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import nl.codevs.dndinventory.inventories.interfaces.ILevel;
import nl.codevs.strinput.examples.discord.DiscordCenter;
import nl.codevs.strinput.system.StrInput;
import nl.codevs.strinput.system.StrUser;
import nl.codevs.strinput.system.context.StrContextHandler;
import nl.codevs.strinput.system.parameter.StrParameterHandler;
import nl.codevs.strinput.system.text.Str;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class DnDInventory {

    private static Bot bot = null;

    private static final DiscordCenter center = new DiscordCenter(
            new File("strSettings"),
            new StrUser() {
                /**
                 * The name of the user (something to identify them by).
                 *
                 * @return the name of the user
                 */
                @Override
                public @NotNull String getName() {
                    return "Discord Console";
                }

                /**
                 * Send a message to the user.
                 *
                 * @param message the message to send
                 */
                @Override
                public void sendMessage(@NotNull Str message) {
                    if (bot == null) {
                        System.out.println(message.toHumanReadable());
                    } else {
                        bot.debug(message.toHumanReadable());
                    }
                }

                /**
                 * @return whether this user supports clickable {@link Str}s.
                 */
                @Override
                public boolean supportsClickables() {
                    return false;
                }

                /**
                 * Play a sound effect.
                 *
                 * @param sfx the sound effect type
                 */
                @Override
                public void playSound(@NotNull StrSoundEffect sfx) {

                }

                /**
                 * If this sender supports context,
                 * i.e. has values it stores for getting data automatically
                 * (instead of specifying it in commands).
                 *
                 * @return true if the user supports context
                 */
                @Override
                public boolean supportsContext() {
                    return false;
                }

                /**
                 * Whether this user has permission for a certain node or not.
                 *
                 * @param permission the permissions node
                 * @return true if permitted.
                 */
                @Override
                public boolean hasPermission(@NotNull String permission) {
                    return true;
                }
            },
            new StrParameterHandler<?>[]{
                    new ItemParameter(),
                    new InventoryParameter()
            },
            new StrContextHandler<?>[]{},
            new Commands()
    );

    private DnDInventory() {
        // Never called
    }

    /**
     * Testing inventory.
     */
    public static PlayerInventory Lazerus = new PlayerInventory(
        "Lazerus",
        new ArrayList<>(),
        Money.fromString("0"),
        new StonePouch(),
        ILevel.CharacterClass.FIGHTER,
        8700,
        2,
        51,
        15,
        13,
        18,
        9,
        11,
        10,
        12
    );

    public static PlayerInventory Manjizz = new PlayerInventory(
            "Manjizz",
            new ArrayList<>(),
            Money.fromString("0"),
            new StonePouch(),
            ILevel.CharacterClass.SPECIALIST,
            10000,
            2,
            16,
            11,
            16,
            10,
            18,
            8,
            14,
            12
    );

    public static PlayerInventory Foraoise = new PlayerInventory(
            "Foraoise",
            new ArrayList<>(),
            Money.fromString("0"),
            new StonePouch(),
            ILevel.CharacterClass.RANGER,
            9500,
            2,
            23,
            13,
            13,
            14,
            11,
            14,
            12,
            13
    );

    public static PlayerInventory Carlos = new PlayerInventory(
            "Carlos",
            new ArrayList<>(),
            Money.fromString("0"),
            new StonePouch(),
            ILevel.CharacterClass.RANGER,
            9550,
            2,
            41,
            16,
            13,
            17,
            10,
            17,
            12,
            11
    );

    public static PlayerInventory Trevor = new PlayerInventory(
            "Trevor",
            new ArrayList<>(),
            Money.fromString("0"),
            new StonePouch(),
            ILevel.CharacterClass.CLERIC,
            7000,
            2,
            20,
            11,
            12,
            15,
            11,
            18,
            13,
            6
    );

    public static AnimalsInventory mules = new AnimalsInventory(
            "Animals",
            new ArrayList<>(),
            new ArrayList<>()
    );

    /**
     * Main function.
     * @param args Java arguments
     * @throws IOException Thrown when token file not found
     */
    public static void main(final String[] args)
            throws IOException {
        Lazerus.addItems(
                new Inventory.InventoryItem(Item.makeGetItem(ItemType.CLOTHING, "Belt", Money.fromString("3sp"), 0, ""), 1)
        );

        for (int i = 0; i < 7; i++) {
            mules.getAnimals().add(AnimalsInventory.Animal.MULE);
        }
        for (int i = 0; i < 6; i++) {
            mules.getAnimals().add(AnimalsInventory.Animal.HORSE);
        }
        Inventory.saveAll(true);
        try {
            bot = new Bot(
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
