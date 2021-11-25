package nl.codevs.dndinventory.discord.commands.items;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.discord.commands.Command;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class FindItem implements Command {
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
                "find", "Find", "search", "Search"
        };
    }

    /**
     * What to do when a command is received.
     *
     * @param params Remaining parameters
     * @param e      The command event
     */
    @Override
    public void onCommand(List<String> params, MessageReceivedEvent e) {
        if (params.isEmpty()) {
            e.getMessage().reply("No item name (or category) specified").queue();
            return;
        }
        List<String> items = new ArrayList<>();
        Item.Type category;
        if (params.size() < 2) {
            Item.Database.matchAll(params.get(0)).subList(0, 5).forEach(i -> items.add(i.toString()));
            e.getMessage().reply(String.join("\n", items)).queue();
            return;
        }
        try {
            category = Item.Type.fromString(params.get(0));
        } catch (InvalidParameterException ex) {
            e.getMessage().reply("Cannot find category: " + params.get(0)).queue();
            return;
        }
        Item.Database.matchAll(category, String.join(" ", params.get(0))).subList(0, 5).forEach(i -> items.add(i.toString()));
        e.getMessage().reply(String.join("\n", items)).queue();
    }
}
