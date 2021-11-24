package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends Command {

    @Override
    public String[] getCommands() {
        return new String[]{"Ping", "ping"};
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage("Pong").queue();
    }

}
