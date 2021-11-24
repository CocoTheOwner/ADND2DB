package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
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
    ConcurrentHashMap<Class<? extends Command>, ConcurrentHashMap<String, Command>> COMMAND_MAP = new ConcurrentHashMap<>();

    /**
     * Category implementation for onCommand.
     * Do not overwrite this!
     * @param e The command event
     */
    @Override
    default void onCommand(List<String> params, MessageReceivedEvent e) {
        if (getCommandMap() == null) {
            setup();
            System.out.println(getCommandMap().values().size() + " commands on output in " + getClass().getSimpleName() + " after setup!");
        }

        String cmd = params.remove(0);
        System.out.println("Category " + getClass().getSimpleName() + " received params " + params + " and command " + cmd);
        if (getCommandMap().containsKey(cmd)) {
            getCommandMap().get(cmd).onCommand(params, e);
        } else {
            params.add(0, cmd);
            runOnNoCommand(params, e);
        }
    }

    /**
     * Get the command map for this category.
     * @return the command map for this category.
     */
    default ConcurrentHashMap<String, Command> getCommandMap() {
        return COMMAND_MAP.get(this.getClass());
    }

    /**
     * Ran if no command match is found in this category.
     * @param params parameters received
     * @param e event
     */
    default void runOnNoCommand(
            final List<String> params,
            final MessageReceivedEvent e
    ) {
        List<String> options = new ArrayList<>();
        for (Command command : subCommands()) {
            options.addAll(Arrays.asList(command.getCommands()));
        }
        e.getMessage().reply("Command: `"
                + e.getMessage().getContentStripped().substring(1)
                + "` got stuck in category "
                + this.getClass().getSimpleName()
                + " (" + options + " | " + params + ")"
        ).queue();
    }

    /**
     * Setup command mappings.
     */
    private void setup() {
        COMMAND_MAP.put(this.getClass(), new ConcurrentHashMap<>());

        List<String> options = new ArrayList<>();
        for (Command command : subCommands()) {
            for (String commandCommand : command.getCommands()) {
                options.add(commandCommand);
                getCommandMap().put(commandCommand, command);
            }
        }

        System.out.println("Setup " + getClass().getSimpleName() + " with new commands: " + options);
    }
}
