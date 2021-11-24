package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Display extends Command {

    @Override
    public String[] getCommands() {
        return new String[]{"Display", "display"};
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        e.getMessage().reply("Hey!").queue();
    }
}
