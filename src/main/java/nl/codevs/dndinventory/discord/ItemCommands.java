package nl.codevs.dndinventory.discord;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemDatabase;
import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.Param;
import nl.codevs.strinput.system.StrInput;

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
}
