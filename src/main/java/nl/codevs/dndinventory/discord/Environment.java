package nl.codevs.dndinventory.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Environment {

    private final String title;

    private final AtomicReference<Guild> guild = new AtomicReference<>();

    private final AtomicReference<Category> category = new AtomicReference<>();

    private final List<AtomicReference<TextChannel>> channels;

    public Environment(final String categoryTitle, final Guild discordGuild) {
        title = categoryTitle;
        guild.set(discordGuild);

        if (guild.get().getCategories().stream().anyMatch(c -> c.getName().equals(title))) {
            guild.get().getCategories().stream().filter(c -> c.getName().equals(title)).findAny().get().delete().queue();
        }

        guild.get().createCategory(title).queue(category::set);

        channels = new ArrayList<>();
    }

    public void shutDown() {
        category.get().delete().queue();
    }

    public void registerChannel(final String name) {
        AtomicReference<TextChannel> c = new AtomicReference<>();
        category.get().createTextChannel(name).queue(c::set);
        channels.add(c);
    }

    public void registerPlayerToChannel(final Member player, final TextChannel channel) {
        if (channels.stream().noneMatch(c -> c.get().getName().equals(channel.getName()))) {
            throw new IllegalArgumentException("Channel " + channel.getName() + " not a channel in environment!");
        }

        channel.createPermissionOverride(player).setAllow(Permission.VIEW_CHANNEL).setAllow(Permission.MESSAGE_WRITE).setAllow(Permission.MESSAGE_ADD_REACTION).queue();

        channel.sendMessage("Test: Registered player " + player.getNickname() + " to this channel").queue();
    }

    public void deregisterPlayerFromChannel(final Member player, final TextChannel channel) {
        if (channels.stream().noneMatch(c -> c.get().getName().equals(channel.getName()))) {
            throw new IllegalArgumentException("Channel " + channel.getName() + " not a channel in environment!");
        }

        channel.createPermissionOverride(player).setDeny(Permission.VIEW_CHANNEL).setDeny(Permission.MESSAGE_WRITE).setDeny(Permission.MESSAGE_ADD_REACTION).queue();

        channel.sendMessage("Test: Deregistered player " + player.getNickname() + " from this channel").queue();
    }
}
