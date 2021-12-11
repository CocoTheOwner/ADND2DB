package nl.codevs.dndinventory.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.codevs.strinput.examples.discord.DiscordCenter;
import nl.codevs.strinput.system.StrCenter;
import nl.codevs.strinput.system.util.AtomicCache;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {

    /**
     * JDA.
     */
    private final JDA jda;

    /**
     * Channel cache.
     */
    private final AtomicCache<TextChannel> channelCache = new AtomicCache<>();

    /**
     * Create a new Discord bot.
     * @param authToken the bot authToken
     * @param commandPrefix command prefix
     * @param activityCommand command to display in activity
     * @param commandCenter the command center
     *
     * @throws LoginException if login fails
     * @throws InterruptedException if waiting for JDA setup fails
     */
    public Bot(
            @NotNull final String authToken,
            @NotNull final String commandPrefix,
            @NotNull final String activityCommand,
            @NotNull final DiscordCenter commandCenter
    ) throws LoginException, InterruptedException {
        this.jda = setup(authToken, commandPrefix, activityCommand);
        this.center = commandCenter;
        this.prefix = commandPrefix;
    }
    
    /**
     * Command center.
     */
    private final DiscordCenter center;

    /**
     * Command prefix.
     */
    private final String prefix;

    /**
     * Main method.
     *
     * @param authToken bot token
     * @param commandPrefix bot command prefix
     * @param activityCommand command to display in activity
     *
     * @throws LoginException if the bot token isn't working
     * @throws InterruptedException if the setup fails
     *
     * @return the setup JDA
     */
    public JDA setup(
            @NotNull final String authToken,
            @NotNull final String commandPrefix,
            @NotNull final String activityCommand
    ) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(authToken);

        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.listening(
                commandPrefix + activityCommand
        ));
        // Add listener
        builder.addEventListeners(this);
        // Set intents
        builder.setEnabledIntents(
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        );

        return builder.build().awaitReady();
    }

    /**
     * When a message is received.
     * @param event the received event
     */
    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(prefix)) {
            return;
        }
        if (event.getAuthor().isBot()) {
            return;
        }
        center.onCommand(event, prefix);
    }

    /**
     * Send a debug message
     * @param string the debug message
     */
    public void debug(String string) {
        StrCenter.DEFAULT_CONSOLE_USER.sendMessage(string);
    }
}
