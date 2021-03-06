package nl.codevs.dndinventory;

import nl.codevs.dndinventory.discord.Bot;
import nl.codevs.dndinventory.discord.Commands;
import nl.codevs.dndinventory.discord.expansions.InventoryParameter;
import nl.codevs.dndinventory.discord.expansions.ItemParameter;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.strinput.examples.discord.DiscordCenter;
import nl.codevs.strinput.system.StrInput;
import nl.codevs.strinput.system.StrUser;
import nl.codevs.strinput.system.context.StrContextHandler;
import nl.codevs.strinput.system.parameter.StrParameterHandler;
import nl.codevs.strinput.system.text.Str;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;

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
     * Main function.
     * @param args Java arguments
     */
    public static void main(final String[] args) {

        Inventory.LOADED_INVENTORIES.forEach(i -> System.out.println(i.getName()));

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
