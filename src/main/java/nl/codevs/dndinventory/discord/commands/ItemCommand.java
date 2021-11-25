package nl.codevs.dndinventory.discord.commands;

import nl.codevs.dndinventory.discord.commands.items.AddItem;

public class ItemCommand implements CommandCategory {

    /**
     * Commands
     */
    private static final Command[] commands = new Command[]{
        new AddItem()
    };

    /**
     * Command strings that point to this command.
     * <p>Make sure to have unique pointers,
     * to avoid unexpected/ambiguous behaviour</p>
     *
     * @return The command strings that point here
     */
    @Override
    public String[] getCommands() {
        return new String[]{"Item", "item", "items", "Items", "i", "I"};
    }

    /**
     * Commands registered to this category.
     *
     * @return An array of registered commands
     */
    @Override
    public Command[] subCommands() {
        return commands;
    }
}
