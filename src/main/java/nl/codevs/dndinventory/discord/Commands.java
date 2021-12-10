package nl.codevs.dndinventory.discord;

import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.StrInput;

@StrInput(name = "dnd", description = "Dungeons & Dragons", aliases = "d")
public class Commands implements DiscordCategory {

    InventoryCommands c;

    ItemCommands i;

    PlayerCommands k;

    @StrInput(name = "ping", description = "Pong!")
    public void ping() {
        long time = System.currentTimeMillis();
        channel().sendMessage("Pong!")
                .queue(response -> response.editMessageFormat(
                        "Pong: %d ms", System.currentTimeMillis() - time
                ).queue());
    }
}
