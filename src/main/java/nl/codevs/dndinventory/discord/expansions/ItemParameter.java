package nl.codevs.dndinventory.discord.expansions;

import nl.codevs.dndinventory.data.Item;
import nl.codevs.dndinventory.data.ItemDatabase;
import nl.codevs.dndinventory.data.ItemType;
import nl.codevs.strinput.system.parameter.StrParameterHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemParameter implements StrParameterHandler<Item> {
    /**
     * Get all possible values for this type.<br>
     * Do not specify lists of very high length (10^6)
     *
     * @return a list of possibilities
     */
    @Override
    public List<Item> getPossibilities() {
        return new ArrayList<>(ItemDatabase.get().values());
    }

    /**
     * Whether this handler supports the type or not.
     *
     * @param type a type
     * @return true if it supports the type
     */
    @Override
    public boolean supports(@NotNull Class<?> type) {
        return type.equals(Item.class);
    }

    /**
     * Parse a string to this type.<br>
     * You can throw:
     * <ul>
     *     <li>{@link StrWhichException}
     *     to indicate multiple options (ambiguity)</li>
     *     <li>{@link StrParseException}
     *     to indicate parsing problems</li>
     * </ul>
     *
     * @param text the string to parse
     * @return an instance of this type parsed from the string
     * @throws Throwable when something else fails.
     *                   (Exceptions don't have to be caught in the parser)
     */
    @Override
    public @NotNull Item parse(@NotNull String text) throws Throwable {
        List<Item> options = getPossibilities(text);

        if (options.isEmpty()) {
            throw new StrParseException(ItemParameter.class, text, "No options match this input");
        } else if (options.size() > 1) {
            throw new StrWhichException(ItemParameter.class, text, options);
        }

        return options.get(0);
    }

    /**
     * Get all possible values for this type,
     * filtered with some input string.<br>
     *
     * @param input the input string to filter by
     * @return a list of possibilities
     */
    @Override
    public @NotNull List<Item> getPossibilities(@NotNull String input) {
        List<Item> options;
        if (input.contains(",")) {
            String[] split = input.split(",");
            ItemType category = ItemType.fromString(split[0]);
            String itemName = split[1];
            options = ItemDatabase.matchAll(category, itemName);
        } else {
            options = ItemDatabase.matchAll(input);
        }
        return options.subList(0, Math.min(options.size() - 1, 5));
    }

    /**
     * Get a random default value.
     *
     * @return the random default
     */
    @Override
    public @NotNull String getRandomDefault() {
        return getPossibilities().get(RANDOM.nextInt(getPossibilities().size() - 1)).getName();
    }
}
