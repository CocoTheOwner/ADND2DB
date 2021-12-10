package nl.codevs.dndinventory;

import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.StrInput;

@StrInput(name = "dnd", description = "Dungeons & Dragons", aliases = "d")
public class Commands implements DiscordCategory {

    @StrInput(name = "ping", description = "Pong!")
    public void ping() {
        long time = System.currentTimeMillis();
        channel().sendMessage("Pong!")
                .queue(response -> response.editMessageFormat(
                        "Pong: %d ms", System.currentTimeMillis() - time
                ).queue());
    }
}
