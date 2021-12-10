package nl.codevs.dndinventory.discord.expansions;

import nl.codevs.dndinventory.data.Money;
import nl.codevs.strinput.system.parameter.StrParameterHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MoneyParameter implements StrParameterHandler<Money> {
    /**
     * Get all possible values for this type.<br>
     * Do not specify lists of very high length (10^6)
     *
     * @return a list of possibilities
     */
    @Override
    public List<Money> getPossibilities() {
        return null;
    }

    /**
     * Whether this handler supports the type or not.
     *
     * @param type a type
     * @return true if it supports the type
     */
    @Override
    public boolean supports(@NotNull Class<?> type) {
        return type.equals(Money.class);
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
    public @NotNull Money parse(@NotNull String text) throws Throwable {
        return Money.fromString(text);
    }

    /**
     * Get a random default value.
     *
     * @return the random default
     */
    @Override
    public @NotNull String getRandomDefault() {
        return new Money(
                Money.Coin.CP, RANDOM.nextInt(30),
                Money.Coin.SP, RANDOM.nextInt(30),
                Money.Coin.GP, RANDOM.nextInt(30)
        ).toConstructorString();
    }
}
