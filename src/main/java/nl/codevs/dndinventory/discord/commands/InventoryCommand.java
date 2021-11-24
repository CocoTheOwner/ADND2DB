package nl.codevs.dndinventory.discord.commands;

import nl.codevs.dndinventory.discord.commands.inventory.Display;

public class InventoryCommand implements CommandCategory {
    /**
     * Command strings that point to this command.
     * <p>Make sure to have unique pointers,
     * to avoid unexpected/ambiguous behaviour</p>
     *
     * @return The command strings that point here
     */
    @Override
    public String[] getCommands() {
        return new String[]{
                "inventory",
                "inv",
                "Inventory",
                "Inv"
        };
    }

    /**
     * Commands registered to this category.
     *
     * @return An array of registered commands
     */
    @Override
    public Command[] subCommands() {
        return new Command[]{
                new Display()
        };
    }
}
