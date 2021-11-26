package nl.codevs.dndinventory.discord.commands.inventory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.discord.commands.Command;
import nl.codevs.dndinventory.inventories.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Display implements Command {

    @Override
    public String[] getCommands() {
        return new String[]{"Display", "display"};
    }

    @Override
    public void onCommand(
            final List<String> params,
            final MessageReceivedEvent e
    ) {
        PlayerInventory inventory = new PlayerInventory(
                ":)",
                new ArrayList<>(),
                new Money(1),
                2,
                1, 2, 3, 4, 5, 6, 7, 8);
        inventory.addItem(
                Item.Database.getItems().get(
                        new Random().nextInt(Item.Database.getItems().size())
                ), 2
        );
        e.getMessage().reply(inventory.toString()).queue();
    }
}
