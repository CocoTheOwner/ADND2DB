package nl.codevs.dndinventory.discord;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.codevs.dndinventory.DnDInventory;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class DiscordIntegration extends ListenerAdapter
{

    private static final String PREFIX = "!";

    private final Guild ourGuild;

    private final JDA jda;

    public DiscordIntegration(String token) throws LoginException, IOException, InterruptedException {

        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(this)
                .setActivity(Activity.playing("Type /ping"))
                .build();

        jda.upsertCommand("ping", "Calculate ping of the bot").queue(); // This can take up to 1 hour to show up in the client
        jda.awaitReady();
        ourGuild = jda.getGuildById(790135428228448286L);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().startsWith(PREFIX)) return;

        switch (event.getMessage().getContentStripped().substring(1)) {
            case "inv" -> event.getChannel().sendMessage(DnDInventory.inv.getItems().get(0).getItem().toString()).queue();
        }
    }
}