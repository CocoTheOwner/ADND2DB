package nl.codevs.dndinventory.data;

import okhttp3.internal.annotations.EverythingIsNonNull;
import org.jetbrains.annotations.Contract;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.codevs.dndinventory.data.Money.Coin.CP;
import static nl.codevs.dndinventory.data.Money.Coin.SP;
import static nl.codevs.dndinventory.data.Money.Coin.EP;
import static nl.codevs.dndinventory.data.Money.Coin.GP;
import static nl.codevs.dndinventory.data.Money.Coin.PP;

@EverythingIsNonNull
public final class Money {

    /**
     * Copper pieces.
     */
    private int cp;
    /**
     * Silver pieces.
     */
    private int sp;
    /**
     * Electrum pieces.
     */
    private int ep;
    /**
     * Gold pieces.
     */
    private int gp;
    /**
     * Platinum pieces.
     */
    private int pp;

    /**
     * Should convert to cp, sp and gp for value.
     * Set this to false for an inventory with x amount of coins of a specific type.
     */
    private boolean simplify = true;

    /**
     * Set simplification.
     * If true, converts value to cp, sp and gp.
     * Set this to false for an inventory with x amount of coins of a specific type.
     * @param simplify whether to simplify or not
     */
    public void setSimplify(boolean simplify) {
        this.simplify = simplify;
    }

    /**
     * Create a new worthless value.
     */
    public Money() {
        this(0);
    }

    /**
     * Get a new value from an existing value and modifier.
     * @param money The value to modify
     * @param modifierFor The factor by which to in-/decrement
     * @return The new adjusted value
     * @throws IllegalArgumentException if the input is negative
     */
    @Contract("_, _ -> new")
    public static Money fromValueAndFactor(
            final Money money,
            final double modifierFor
    ) throws IllegalArgumentException {
        return new Money(CP, money.getAsCP() * modifierFor);
    }

    /**
     * Subtract a value from this value.
     * @param money The value to subtract
     * @return The new value
     * @throws IllegalArgumentException if the input is negative
     */
    @Contract("_ -> new")
    public Money subtract(final Money money) throws IllegalArgumentException {
        return new Money(CP, getAsCP() - money.getAsCP());
    }

    /**
     * Get the amount of copper pieces.
     * @return Amount of copper pieces
     */
    public int getCP() {
        return cp;
    }

    /**
     * Get the amount of silver pieces.
     * @return Amount of silver pieces
     */
    public int getSP() {
        return sp;
    }

    /**
     * Get the amount of electrum pieces.
     * @return Amount of electrum pieces
     */
    public int getEP() {
        return ep;
    }

    /**
     * Get the amount of gold pieces.
     * @return Amount of gold pieces
     */
    public int getGP() {
        return gp;
    }

    /**
     * Get the amount of platinum pieces.
     * @return Amount of platinum pieces
     */
    public int getPP() {
        return pp;
    }

    /**
     * Gold pieces only.
     * @param goldPieces The amount of GP
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(final int goldPieces) throws IllegalArgumentException {
        this(GP, goldPieces);
    }

    /**
     * Fractional Gold pieces only.
     * @param goldPieces The amount of GP
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(final double goldPieces) throws IllegalArgumentException {
        this(GP, goldPieces);
    }

    /**
     * Copper, Silver and Gold pieces only.
     * @param copperPieces The amount of CP
     * @param silverPieces The amount of SP
     * @param goldPieces The amount of GP
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final int copperPieces,
            final int silverPieces,
            final int goldPieces
    ) throws IllegalArgumentException {
        this(CP, copperPieces, SP, silverPieces, GP, goldPieces);
    }

    /**
     * Fractional one type.
     * @param type The coin type
     * @param amount The amount
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(final Coin type, final double amount) throws IllegalArgumentException {
        this(type, amount, CP, 0);
    }

    /**
     * Fractional two types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     * @throws IllegalArgumentException if a coin was negative
     */
    public Money(
            final Coin type1,
            final double amount1,
            final Coin type2,
            final double amount2
    ) throws IllegalArgumentException {
        addCoin(type1, amount1);
        addCoin(type2, amount2);
        maxTarget();
    }

    /**
     * Any type whole amount.
     * @param type The coin type
     * @param amount The amount
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(final Coin type, final int amount) throws IllegalArgumentException {
        this(type, amount, CP, 0);
    }

    /**
     * Whole two types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final Coin type1,
            final int amount1,
            final Coin type2,
            final int amount2
    ) throws IllegalArgumentException {
        this(type1, amount1, type2, amount2, CP, 0);
    }

    /**
     * Whole three types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     * @param type3 The third coin type
     * @param amount3 The third coin amount
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final Coin type1,
            final int amount1,
            final Coin type2,
            final int amount2,
            final Coin type3,
            final int amount3
    ) throws IllegalArgumentException {
        this(
                type1,
                amount1,
                type2,
                amount2,
                type3,
                amount3,
                true
        );
    }

    /**
     * Whole three types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     * @param type3 The third coin type
     * @param amount3 The third coin amount
     * @param simplify If set to true, simplifies by maximizing gp, then sp, then cp
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final Coin type1,
            final int amount1,
            final Coin type2,
            final int amount2,
            final Coin type3,
            final int amount3,
            boolean simplify
    ) throws IllegalArgumentException {
        addCoin(type1, amount1);
        addCoin(type2, amount2);
        addCoin(type3, amount3);
        validate();
        setSimplify(simplify);
        maxTarget();
    }

    /**
     * All {@link Coin} types separately.
     * @param copperPieces Amount of CP
     * @param silverPieces Amount of SP
     * @param electrumPieces Amount of EP
     * @param goldPieces Amount of GP
     * @param platinumPieces Amount of PP
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final int copperPieces,
            final int silverPieces,
            final int electrumPieces,
            final int goldPieces,
            final int platinumPieces
    ) throws IllegalArgumentException {
        this(
                copperPieces,
                silverPieces,
                electrumPieces,
                goldPieces,
                platinumPieces,
                true
        );
    }

    /**
     * All {@link Coin} types separately.
     * @param copperPieces Amount of CP
     * @param silverPieces Amount of SP
     * @param electrumPieces Amount of EP
     * @param goldPieces Amount of GP
     * @param platinumPieces Amount of PP
     * @param simplify If set to true, simplifies by maximizing gp, then sp, then cp
     * @throws IllegalArgumentException if the input is negative
     */
    public Money(
            final int copperPieces,
            final int silverPieces,
            final int electrumPieces,
            final int goldPieces,
            final int platinumPieces,
            boolean simplify
    ) throws IllegalArgumentException {
        this.cp = copperPieces;
        this.sp = silverPieces;
        this.ep = electrumPieces;
        this.gp = goldPieces;
        this.pp = platinumPieces;
        validate();
        setSimplify(simplify);
        maxTarget();
    }

    /**
     * <p>Create a value from a formatted string.</p><br>
     * <h3>Format examples:</h3>
     * <ul>
     *     <li>5gp 6sp 9pp</li>
     *     li>500 <i>Assumed to be GP</i></li>
     *     <li>5gp 5gp 5gp <i>Added up to 15gp</i></li>
     *     <li>5gp5sp5cp5pp</li>
     *     <li>5g <i>c, s, e, g, p work as well as cp, sp, ep, gp, pp</i></li>
     * </ul>
     * <p>
     *     See {@link #fromString(String, boolean)} for creating money that is not simplified<br>
     *     (5ep will become 2gp 5sp, and 1pp becomes 10gp)
     * </p>
     * <p>
     *     Note that this system uses regex to find specific element in your string.
     *     The more precise the input, the better this process goes.
     *     Regex can skip characters in your string in order to find suitable matches.
     * </p>
     * @param value The string representation of a value
     * @throws InvalidParameterException when the input is invalid
     * @throws IllegalArgumentException if the input is negative
     */
    @Contract("_ -> new")
    public static Money fromString(final String value)
            throws InvalidParameterException, IllegalArgumentException {
        return fromString(value, true);
    }

    /**
     * Money matching regex.
     */
    private static final Pattern MONEY_REGEX = Pattern.compile("((?:[0-9]*[.])?[0-9]+)([CSEGP])[P]?|([0-9]*[.]?[0-9]+)");

    /**
     * <p>Create a value from a formatted string.</p><br>
     * <h3>Format examples:</h3>
     * <ul>
     *     <li>5gp 6sp 9pp</li>
     *     <li>500 <i>Assumed to be GP</i></li>
     *     <li>5gp 5gp 5gp <i>Added up to 15gp</i></li>
     *     <li>5gp5sp5cp5pp</li>
     *     <li>5g <i>c, s, e, g, p work as well as cp, sp, ep, gp, pp</i></li>
     * </ul>
     * <p>
     *     Note that this system uses regex to find specific element in your string.
     *     The more precise the input, the better this process goes.
     *     Regex can skip characters in your string in order to find suitable matches.
     * </p>
     * @param value The string representation of a value
     * @param simplify If set to true, simplifies by maximizing gp, then sp, then cp
     * @throws InvalidParameterException when the input is invalid
     * @throws IllegalArgumentException if the input is negative
     */
    @Contract("_, _ -> new")
    public static Money fromString(final String value, boolean simplify)
            throws InvalidParameterException, IllegalArgumentException {

        String cleanValue = value
                .replace(" ", "")
                .replace(",", ".")
                .replace("pp", "x")
                .replace("p", "")
                .replace("x", "p")
                .toUpperCase(Locale.ROOT);

        // Match results
        Matcher matcher = MONEY_REGEX.matcher(cleanValue);
        List<MatchResult> resultList = new ArrayList<>();
        while (matcher.find()) {
            resultList.add(matcher.toMatchResult());
        }

        // Ensure some were found
        if (resultList.isEmpty()) {
            throw new InvalidParameterException("Input " + value + " not in valid Money format!");
        }

        // Money that will accumulate total
        Money money = new Money();

        // Go over matched result groups.
        for (MatchResult matchResult : resultList) {

            if (matchResult.groupCount() != 3) {
                throw new InvalidParameterException("Input " + value + " invalid because not enough groups matched!");
            }
            // matchResult.groupCount == 3

            if (matchResult.group(1) == null && matchResult.group(2) == null) {
                if (resultList.size() != 1) {
                    throw new InvalidParameterException("Input " + value + " invalid because not all C/S/E/G/P specified but not only number!");
                } else {
                    return new Money(Double.parseDouble(matchResult.group(0)));
                }
            }

            money.addCoin(
                    Coin.valueOf(matchResult.group(2) + "P"),
                    Double.parseDouble(matchResult.group(1))
            );
        }

        // Simplification on coins
        if (simplify) {
            money.maxTarget();
        }

        return money;
    }

    /**
     * The factor to use to fix double division errors.
     */
    private static final double ROUND_FACTOR = 1000d;

    /**
     * Add a fractional amount of a coin.
     * @param type The coin type
     * @param amount The amount to add
     */
    private void addCoin(final Coin type, final double amount) {
        // 0 amount
        if (amount == 0) {
            validate();
            return;
        }

        // round amount
        if (Math.round(amount) == amount) {
            addCoin(type, (int) amount);
            validate();
            return;
        }

        double newAmount;

        // more than 1
        if (amount > 1) {
            int whole = (int) amount; // The "whole" number component
            double part = amount - whole;
            assert part == amount % 1; // Just a check
            newAmount = part;
            addCoin(type, whole);
        } else {
            newAmount = amount;
        }

        // cannot decrease CP
        if (type.equals(CP)) {
            throw new RuntimeException(
                    "Cannot further decrease remaining cp: " + newAmount
            );
        }

        // remainder
        int factor = type.decrementFactor();
        addCoin(
                type.decrement(),
                Math.round(newAmount * factor * ROUND_FACTOR) / ROUND_FACTOR
        );

        validate();
    }

    /**
     * Add a whole amount of a certain {@link Coin}.
     * @param type The coin type
     * @param amount The amount to add
     */
    private void addCoin(final Coin type, final int amount) {
        switch (type) {
            case CP -> cp += amount;
            case SP -> sp += amount;
            case EP -> ep += amount;
            case GP -> gp += amount;
            case PP -> pp += amount;
            default -> throw new RuntimeException("Unhandled coin type");
        }
    }

    /**
     * Get the amount of coins in this value in {@link Coin}.CP.
     * @return The amount of CP
     */
    public int getAsCP() {
        return cp + SP.decrementFactor()
                * (sp + EP.decrementFactor()
                    * (ep + GP.decrementFactor()
                        * (gp + PP.decrementFactor() * pp)));
    }

    /**
     * Get the amount of coins in this value in {@link Coin}.GP.
     * @return The amount of CP
     */
    public double getAsGP() {
        return ((double) getAsCP())
                / SP.decrementFactor()
                / EP.decrementFactor()
                / GP.decrementFactor();
    }

    /**
     * Get the amount of coins in this value in {@link Coin}.PP.
     * @return The amount of CP
     */
    public double getAsPP() {
        return getAsGP() / PP.decrementFactor();
    }

    /**
     * Get the weight of this money.
     * @return The weight of the money
     */
    public double getWeight() {
        return (getCP() + getSP() + getEP() + getGP() + getPP())
                * Coin.COIN_WEIGHT;
    }

    /**
     * Validate money, ensuring all parameters are positive or 0.
     * @throws IllegalArgumentException if invalid
     */
    private void validate() throws IllegalArgumentException {
        if (cp < 0 || sp < 0 || ep < 0 || gp < 0 || pp < 0) {
            throw new IllegalArgumentException("Input negative value: " + this);
        }
    }

    /**
     * Maximizes the amount of currency in a specific coin in this value.<p>
     * Uses the local constant array 'targets' to define order
     */
    private void maxTarget() {

        if (!simplify) {
            return;
        }

        int totalCP = getAsCP();

        // Conversion factors
        final int spCp = SP.decrementFactor();
        final int gpCp = GP.decrementFactor() * EP.decrementFactor() * spCp;

        ep = 0;
        pp = 0;

        gp = (totalCP - (totalCP % gpCp)) / gpCp;
        totalCP -= gp * gpCp;
        sp = (totalCP - (totalCP % spCp)) / spCp;
        totalCP -= sp * spCp;
        cp = totalCP;
    }

    @Override
    public String toString() {
        return toConstructorString();
    }

    /**
     * Create a string that can also be used in {@link #fromString(String, boolean)} or {@link #fromString(String)}.
     * @return a string representation usable in the fromString method
     */
    public String toConstructorString() {
        StringBuilder res = new StringBuilder();
        if (pp != 0) {
            res.append(pp).append("pp ");
        }
        if (gp != 0) {
            res.append(gp).append("gp ");
        }
        if (ep != 0) {
            res.append(ep).append("ep ");
        }
        if (sp != 0) {
            res.append(sp).append("sp ");
        }
        if (cp != 0) {
            res.append(cp).append("cp ");
        }
        String out = res.toString().strip();
        return out.isBlank() ? "0gp" : out;
    }

    /**
     * Coin types.
     */
    public enum Coin {
        /**
         * Copper Pieces.
         */
        CP,
        /**
         * Silver Pieces.
         */
        SP,
        /**
         * Electrum Pieces.
         */
        EP,
        /**
         * Gold Pieces.
         */
        GP,
        /**
         * Platinum Pieces.
         */
        PP;

        /**
         * The weight of each coin.
         */
        public static final double COIN_WEIGHT = 0.02;

        /**
         * Decrement the coin type.
         * @return The next-lower coin type.
         */
        public Coin decrement() {
            return switch (this) {
                case CP -> throw new RuntimeException("Cannot decrement CP");
                case SP -> CP;
                case EP -> SP;
                case GP -> EP;
                case PP -> GP;
            };
        }

        /**
         * SP to CP conversion factor.
         */
        private static final int S_TO_C = 10;

        /**
         * EP to SP conversion factor.
         */
        private static final int E_TO_S = 5;

        /**
         * GP to EP conversion factor.
         */
        private static final int G_TO_P = 2;

        /**
         * PP to GP conversion factor.
         */
        private static final int P_TO_G = 5;

        /**
         * Get the decrement factor.
         * @return The decrement factor.
         */
        public int decrementFactor() {
            return switch (this) {
                case CP -> throw new RuntimeException(
                        "Cannot decrementFactor CP"
                );
                case SP -> S_TO_C;
                case EP -> E_TO_S;
                case GP -> G_TO_P;
                case PP -> P_TO_G;
            };
        }
    }
}
