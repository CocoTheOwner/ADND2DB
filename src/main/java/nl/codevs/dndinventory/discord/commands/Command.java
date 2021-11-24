package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {

    public abstract String[] getCommands();

    public abstract void onCommand(MessageReceivedEvent e);
}
