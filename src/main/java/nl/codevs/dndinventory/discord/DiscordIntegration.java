package nl.codevs.dndinventory.discord;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.codevs.dndinventory.discord.commands.*;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DiscordIntegration extends ListenerAdapter
        implements CommandCategory {

    /**
     * Commands registered to this category.
     *
     * @return An array of registered commands
     */
    @Override
    public Command[] subCommands() {
        return new Command[]{
                new Ping(),
                new InventoryCommand()
        };
    }

    /**
     * Prefix for the bot.
     */
    private static final String PREFIX = "!";

    /**
     * Construct a discord integration instance.
     * @param token The token for the bot
     * @throws LoginException If the login fails
     * @throws InterruptedException If waiting for the bot
     *                  to be ready is interrupted
     */
    public DiscordIntegration(final String token)
            throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(this)
                .setActivity(Activity.playing("Type !ping"))
                .build();

        jda.awaitReady();
        long guildID = 790135428228448286L;
        Guild ourGuild = jda.getGuildById(guildID);
        assert ourGuild != null;

        Environment environment = new Environment(
                "Dungeons & Dragons",
                ourGuild
        );
    }

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().startsWith(PREFIX)) {
            return;
        }

        List<String> arguments = new ArrayList<>(Arrays.asList(
                event.getMessage().getContentStripped().substring(1).split(" ")
        ));
        onCommand(arguments, event);
    }

    /**
     * Command strings that point to this command.
     * <p>Make sure to have unique pointers,
     * to avoid unexpected/ambiguous behaviour</p>
     *
     * @return The command strings that point here
     */
    @Override
    public String[] getCommands() {
        return null;
    }
}
