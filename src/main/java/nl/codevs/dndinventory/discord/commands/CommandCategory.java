package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface CommandCategory extends Command {

    /**
     * Commands registered to this category.
     * @return An array of registered commands
     */
    Command[] subCommands();

    /**
     * Command-string to command mapping.
     */
    ConcurrentHashMap<String, Command> COMMAND_MAP = new ConcurrentHashMap<>();

    /**
     * Category implementation for onCommand.
     * Do not overwrite this!
     * @param e The command event
     */
    @Override
    default void onCommand(List<String> params, MessageReceivedEvent e) {
        if (COMMAND_MAP.isEmpty() && subCommands().length > 0) {
            setup();
        }

        String cmd = params.remove(0);
        if (COMMAND_MAP.containsKey(cmd)) {
            COMMAND_MAP.get(cmd).onCommand(params, e);
        } else {
            e.getMessage().reply("Command: `"
                    + e.getMessage().getContentStripped().substring(1)
                    + "` not found in category "
                    + Arrays.toString(getCommands())
            ).queue();
        }
    }

    /**
     * Setup command mappings.
     */
    private void setup() {
        for (Command command : subCommands()) {
            for (String commandCommand : command.getCommands()) {
                COMMAND_MAP.put(commandCommand, command);
            }
        }
    }
}
