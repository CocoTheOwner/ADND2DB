package nl.codevs.dndinventory.discord;


import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.codevs.dndinventory.discord.commands.*;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class DiscordIntegration extends ListenerAdapter
{

    private static final ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();

    static {
        registerCommand(new Ping());
        registerCommand(new Display());
    }

    private static final String PREFIX = "!";

    private TextChannel channel;

    private Guild ourGuild;

    private JDA jda;

    private Environment environment;

    public DiscordIntegration(String token) throws LoginException, InterruptedException {

        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(this)
                .setActivity(Activity.playing("Type !ping"))
                .build();

        jda.upsertCommand("ping", "Calculate ping of the bot").queue(); // This can take up to 1 hour to show up in the client
        jda.awaitReady();
        ourGuild = jda.getGuildById(790135428228448286L);
        assert ourGuild != null;

        environment = new Environment("Dungeons & Dragons", ourGuild);
    }

    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().startsWith(PREFIX)) return;

        if (commands.containsKey(event.getMessage().getContentStripped().substring(1))) {
            commands.get(event.getMessage().getContentStripped().substring(1)).onCommand(event);
        } else {
            event.getMessage().reply("Command: `" + event.getMessage().getContentStripped().substring(1) + "` not found").queue();
        }
    }

    /**
     * Register a command
     * @param command The {@link Command} that contains the command code
     */
    public static void registerCommand(Command command) {

        // Print
        System.out.println("Registered command class " + command.getClass().getSimpleName() + " with commands: " + Arrays.toString(command.getCommands()));

        // Register this instance by all its commands
        for (String cmd : command.getCommands()) {
            commands.put(cmd, command);
        }

    }
}