package nl.codevs.dndinventory.discord;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DiscordIntegration extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        BufferedReader r = new BufferedReader(new FileReader("./token.txt"));
        String token = r.readLine();
        JDA jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new DiscordIntegration())
                .setActivity(Activity.playing("Type /ping"))
                .build();

        jda.upsertCommand("ping" , "Get the bot ping").queue();
        jda.awaitReady();
        jda.getGuildById("790135428228448286").upsertCommand("pung", "Get the bot ping").queue();
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

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
    }
}