package nl.codevs.dndinventory.discord;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class DiscordIntegration extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        String token = new BufferedReader(new FileReader("./token.txt")).readLine();

        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new DiscordIntegration())
                .setActivity(Activity.playing("Type /ping"))
                .build();

        jda.upsertCommand("ping", "Calculate ping of the bot").queue(); // This can take up to 1 hour to show up in the client
        jda.awaitReady();
        Guild ourGuild = jda.getGuildById(790135428228448286L);
        assert ourGuild != null;
        ourGuild.upsertCommand("ping", "Gop").queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals("ping")) return; // make sure we handle the right command
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }
}