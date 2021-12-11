package nl.codevs.dndinventory.discord.expansions;

import nl.codevs.dndinventory.inventories.Inventory;
import nl.codevs.strinput.system.parameter.StrParameterHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryParameter implements StrParameterHandler<Inventory> {
    /**
     * Get all possible values for this type.<br>
     * Do not specify lists of very high length (10^6)
     *
     * @return a list of possibilities
     */
    @Override
    public List<Inventory> getPossibilities() {
        return Inventory.LOADED_INVENTORIES;
    }

    /**
     * Whether this handler supports the type or not.
     *
     * @param type a type
     * @return true if it supports the type
     */
    @Override
    public boolean supports(@NotNull Class<?> type) {
        return Inventory.class.isAssignableFrom(type);
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
    public @NotNull Inventory parse(@NotNull String text) throws Throwable {

        List<Inventory> options = getPossibilities(text);

        if (options.isEmpty()) {
            throw new StrParseException(InventoryParameter.class, text, "No matches found for input");
        } else if (options.size() > 1) {
            throw new StrWhichException(InventoryParameter.class, text, options);
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
    public @NotNull List<Inventory> getPossibilities(@NotNull String input) {
        return getPossibilities().stream().filter(i -> i.getName().contains(input)).collect(Collectors.toList());
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
