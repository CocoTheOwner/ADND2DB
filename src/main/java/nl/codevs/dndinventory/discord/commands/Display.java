package nl.codevs.dndinventory.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.PlayerInventory;

import java.util.ArrayList;
import java.util.Random;

public class Display extends Command {

    private static final PlayerInventory inventory = new PlayerInventory(":)", new ArrayList<>(), new Money(5), 69);

    static {
        inventory.addItem(Item.Database.getItems().get(new Random().nextInt(Item.Database.getItems().size())), 5);
    }

    @Override
    public String[] getCommands() {
        return new String[]{"Display", "display"};
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        e.getMessage().reply(inventory.toString()).queue();
    }
}
