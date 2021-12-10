package nl.codevs.dndinventory.discord;

import nl.codevs.dndinventory.inventories.PlayerInventory;
import nl.codevs.strinput.examples.discord.DiscordCategory;
import nl.codevs.strinput.system.Param;
import nl.codevs.strinput.system.StrInput;

import java.io.IOException;

@StrInput(name = "player", aliases = "p", description = "Player commands")
public class PlayerCommands implements DiscordCategory {

    @StrInput(aliases = "add-xp")
    public void giveExp(
            @Param(
                    name = "player",
                    description = "The player to give xp to"
            ) final PlayerInventory player,
            @Param(
                    name = "xp",
                    description = "The amount of experience to give"
            ) final int xp
    ) throws IOException {
             player.addExperience(xp);
             player.save(true);
             user().sendMessage("Added " + xp + " to " + player.getName());
    }

    @StrInput(name = "possible-level", aliases = "trainable")
    public void getTrainableLevel(
            @Param(
                    name = "player",
                    description = "The player to get the trainable level of"
            ) final PlayerInventory player
    ) throws IOException {
        user().sendMessage("Player " + player.getName() + " can train to level " + player.getPotentialLevel());
    }
}
