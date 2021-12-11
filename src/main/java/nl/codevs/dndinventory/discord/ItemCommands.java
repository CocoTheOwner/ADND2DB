package nl.codevs.dndinventory.discord;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemDatabase;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.Param;
import nl.codevs.strinput.system.StrInput;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;

@StrInput(description = "Commands for items", name = "items", aliases = "i")
public class ItemCommands implements DiscordCategory {

    @StrInput(description = "get random items from the database")
    public void getRandom(
            @Param(
                    name = "amount",
                    description = "the amount of items to get",
                    defaultValue = "10"
            ) final Integer amount
    ) {
        StringBuilder result = new StringBuilder("Showing ").append(amount).append(" items: ");
        for (Item item : new ArrayList<>(ItemDatabase.get().values()).subList(0, amount)) {
            if (result.length() > 2000) {
                break;
            }
            result.append("\n - ").append(item.toString());
        }
        user().sendMessage(result.toString());
    }

    @StrInput(description = "get an item by name", aliases = "find")
    public void getItem(
            @Param(
                    name = "name",
                    description = "the item name"
            ) final String name
    ) {
        try {
            user().sendMessage(ItemDatabase.fromName(name).toString());
        } catch (InstanceNotFoundException e) {
            user().sendMessage("Could not find an item by the name: " + name);
        }
    }

    @StrInput(description = "Make a new item", aliases = "new")
    public void make(
            @Param(name = "name", description = "the item name")
            final String name,
            @Param(name = "category", description = "the item category")
            final String category,
            @Param(name = "value", description = "the item value")
            final Money value,
            @Param(name = "weight", description = "the item's weight")
            final double weight,
            @Param(name = "description", description = "the item's description", defaultValue = "")
            final String description
    ) {
        Item i = Item.makeGetItem(ItemType.fromString(category), name, value, weight, description);
        user().sendMessage("Made new item: " + i);
    }
}
