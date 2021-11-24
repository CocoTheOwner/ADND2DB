package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public final class Ping implements Command {

    @Override
    public String[] getCommands() {
        return new String[]{"Ping", "ping"};
    }

    @Override
    public void onCommand(
            final List<String> params,
            final MessageReceivedEvent e
    ) {
        e.getChannel().sendMessage("Pong").queue();
    }

}
