package nl.codevs.dndinventory.discord;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.dndinventory.inventories.PlayerInventory;
import nl.codevs.dndinventory.inventories.interfaces.ILevel;
import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.Param;
import nl.codevs.strinput.system.StrInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@StrInput(name = "inventory", aliases = "inv", description = "Player Inventory Management")
public class InventoryCommands implements DiscordCategory {

    @StrInput(description = "List all inventories currently loaded")
    public void list(
            @Param(
                    name = "filter",
                    description = "Specify a bit of text that all the inventories' name must contain." +
                            " Non case-sensitive.",
                    defaultValue = "includeAll"
            )
            final String filter
    ) {
        String lower = filter.equals("includeAll") ? "" : filter.toLowerCase();
        List<Inventory> filtered = Inventory.LOADED_INVENTORIES.stream().filter(
                i -> i.getName().toLowerCase(Locale.ROOT).contains(lower)
        ).toList();
        StringBuilder s = new StringBuilder(lower.isEmpty() ? "Loaded" : "Filtered").append(" inventories ").append(filtered.size()).append(":");
        for (Inventory loadedInventory : filtered) {
            PlayerInventory inv = (PlayerInventory) loadedInventory;
            s.append("\n - ").append(loadedInventory.getName()).append(" (items: ").append(loadedInventory.getItems().size()).append(", ");
            s.append(" money: ").append(inv.getMoney().toString()).append(")");
        }
        channel().sendMessage(s.toString()).queue();
    }

    @StrInput(description = "Add an item to an inventory", aliases = {"add", "+"})
    public void addTo(
            @Param(
                    name = "inventory",
                    description = "the inventory"
            )
            final Inventory inventory,
            @Param(
                    name = "item",
                    description = "the item"
            )
            final Item item,
            @Param(
                    name = "amount",
                    description = "the amount",
                    defaultValue = "1"
            )
            final Integer amount
    ) throws IOException {
        inventory.addItems(item, amount);
        inventory.save(true);
        user().sendMessage("Added " + amount
                + " of " + item
                + " to " + inventory.getName()
                + " (now has " + inventory.getItems().size() + " items)"
        );
    }

    @StrInput(description = "Remove an item from an inventory", aliases = {"remove", "-"})
    public void removeFrom(
            @Param(
                    name = "inventory",
                    description = "the inventory"
            )
            final Inventory inventory,
            @Param(
                    name = "item",
                    description = "the item"
            )
            final Item item,
            @Param(
                    name = "amount",
                    description = "the amount",
                    defaultValue = "1"
            )
            final Integer amount
    ) throws IOException {
        Inventory.InventoryItem i = inventory.removeItem(item, amount);
        inventory.save(true);
        user().sendMessage("Removed " + amount
                + " of " + item
                + " from " + inventory.getName()
                + " (now has " + inventory.getItems().size() + " items)"
                + (i != null ? " remaining items: " + i.getAmount() + " of " + i.getItem() : " no remaining items")
        );
    }

    @StrInput(description = "Display an inventory")
    public void open(
            @Param(name = "inventory", description = "The inventory to open")
            final Inventory inventory
    ) {
        user().sendMessage(inventory.toString());
    }

    @StrInput(description = "Create an inventory")
    public void create(
            @Param(name = "name", description = "The new inventory name")
            final String name,
            @Param(name = "money", description = "The character's money")
            final Money money
    ) {
        new PlayerInventory(
                name,
                new ArrayList<>(),
                money,
                ILevel.CharacterClass.FIGHTER,
                0,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1
        );
    }
}
