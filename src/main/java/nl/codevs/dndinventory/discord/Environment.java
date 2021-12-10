package nl.codevs.dndinventory.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Deprecated
public class Environment {

    /**
     * Environment title.
     */
    private final String title;

    /**
     * Environment guild (server).
     */
    private final AtomicReference<Guild> guild = new AtomicReference<>();

    /**
     * Environment category (where channels reside).
     */
    private final AtomicReference<Category> category = new AtomicReference<>();

    /**
     * Environment channels.
     */
    private final List<AtomicReference<TextChannel>> channels;

    /**
     * Create a new environment.
     * @param categoryTitle the title of the environment
     * @param discordGuild the guild in which to create the environment
     */
    public Environment(final String categoryTitle, final Guild discordGuild) {
        title = categoryTitle;
        guild.set(discordGuild);

        if (guild.get().getCategories().stream()
                .anyMatch(c -> c.getName().equals(title))) {
            guild.get().getCategories().stream()
                    .filter(c -> c.getName().equals(title))
                    .findAny().get().delete().queue();
        }

        guild.get().createCategory(title).queue(category::set);

        channels = new ArrayList<>();
    }

    /**
     * Shutdown the environment (i.e. cleanup).
     */
    public void shutDown() {
        category.get().delete().queue();
    }

    /**
     * Register a new channel in the environment.
     * @param name the channel name
     */
    public void registerChannel(final String name) {
        AtomicReference<TextChannel> c = new AtomicReference<>();
        category.get().createTextChannel(name).queue(c::set);
        channels.add(c);
    }

    /**
     * Assign a player (Discord {@link Member}) to an environment channel.
     * @param player the player to assign
     * @param channel the channel to which to assign the player
     */
    public void registerPlayerToChannel(
            final Member player,
            final TextChannel channel
    ) {
        if (channels.stream()
                .noneMatch(c -> c.get().getName().equals(channel.getName()))) {
            throw new IllegalArgumentException(
                    "Channel " + channel.getName()
                            + " not a channel in environment!"
            );
        }

        channel.createPermissionOverride(player)
                .setAllow(Permission.VIEW_CHANNEL)
                .setAllow(Permission.MESSAGE_WRITE)
                .setAllow(Permission.MESSAGE_ADD_REACTION)
                .queue();

        channel.sendMessage(
                "Test: Registered player " + player.getNickname()
                        + " to this channel"
        ).queue();
    }

    /**
     * Remove a player (Discord {@link Member}) from an environment channel.
     * @param player the player to remove
     * @param channel the channel from which to remove the player
     */
    public void deregisterPlayerFromChannel(
            final Member player,
            final TextChannel channel
    ) {
        if (channels.stream()
                .noneMatch(c -> c.get().getName().equals(channel.getName()))) {
            throw new IllegalArgumentException(
                    "Channel " + channel.getName()
                            + " not a channel in environment!"
            );
        }

        channel.createPermissionOverride(player)
                .setDeny(Permission.VIEW_CHANNEL)
                .setDeny(Permission.MESSAGE_WRITE)
                .setDeny(Permission.MESSAGE_ADD_REACTION)
                .queue();

        channel.sendMessage(
                "Test: Deregistered player " + player.getNickname()
                        + " from this channel"
        ).queue();
    }
}
