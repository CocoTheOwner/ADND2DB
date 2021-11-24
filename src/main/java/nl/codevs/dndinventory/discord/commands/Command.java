package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {

    /**
     * Command strings that point to this command.
     * <p>Make sure to have unique pointers,
     * to avoid unexpected/ambiguous behaviour</p>
     * @return The command strings that point here
     */
    String[] getCommands();

    /**
     * What to do when a command is received.
     * @param params Remaining parameters
     * @param e The command event
     */
    void onCommand(
            List<String> params,
            MessageReceivedEvent e
    );
}
