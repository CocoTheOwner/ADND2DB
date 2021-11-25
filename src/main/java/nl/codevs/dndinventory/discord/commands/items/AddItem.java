package nl.codevs.dndinventory.discord.commands.items;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.discord.commands.Command;

import java.util.List;

public class AddItem implements Command {
    /**
     * Command strings that point to this command.
     * <p>Make sure to have unique pointers,
     * to avoid unexpected/ambiguous behaviour</p>
     *
     * @return The command strings that point here
     */
    @Override
    public String[] getCommands() {
        return new String[]{"add", "Add", "new", "New", "+"};
    }

    /**
     * What to do when a command is received.
     *
     * @param params Remaining parameters
     * @param e      The command event
     */
    @Override
    public void onCommand(List<String> params, MessageReceivedEvent e) {
        e.getMessage().reply("Hey!").queue();

        if (params.size() < 4) {
            e.getMessage().reply("You must specify all arguments: Category Name Money Weight (Stats...)").queue();
            e.getMessage().reply("Example: `Weapons Sword 2gp 5 very cool weapon").queue();
        }

        Item.Type category;
        try {
            category = Item.Type.fromString(params.get(0));
        } catch (IllegalArgumentException ex) {
            e.getMessage().reply("Cannot find category from input '" + params.get(0) + "'").queue();
            return;
        }

        String name = params.get(1);

        Money worth;
        try {
            worth = new Money(params.get(2));
        } catch (IllegalArgumentException ex) {
            e.getMessage().reply("Your third argument '" + params.get(2) + "' could not be converted to money.\n" +
                    "Please make sure to format it as `###cp` (replacing `#` with a number and `cp` with `cp|sp|ep|gp|pp`").queue();
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(params.get(3));
        } catch (NumberFormatException ex) {
            e.getMessage().reply("Your fourth argument '" + params.get(3) + "' could not be converted to a double, please format it as `#.#`").queue();
            return;
        }

        String stats = params.size() >= 5 ? String.join(" ", params.subList(4, params.size())) : "";

        Item item = Item.makeGetItem(category, name, worth, weight, stats);

        e.getMessage().reply("Successfully added item: " + item).queue();
    }
}
