package nl.codevs.dndinventory.data;

import java.util.Locale;

import static nl.codevs.dndinventory.data.Coin.CP;
import static nl.codevs.dndinventory.data.Coin.SP;
import static nl.codevs.dndinventory.data.Coin.EP;
import static nl.codevs.dndinventory.data.Coin.GP;
import static nl.codevs.dndinventory.data.Coin.PP;

public final class Value {

    /**
     * Target these coins (in order) to be maximised in the value.
     */
    private static final Coin[] TARGET = new Coin[]{GP, SP, CP};

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
     * Create a new worthless value.
     */
    public Value() {
        this(0);
    }

    /**
     * Get a new value from an existing value and modifier.
     * @param value The value to modify
     * @param modifierFor The factor by which to in-/decrement
     * @return The new adjusted value
     */
    public static Value fromValueAndFactor(
            final Value value,
            final double modifierFor
    ) {
        return new Value(CP, value.getAsCP() * modifierFor);
    }

    /**
     * Subtract a value from this value.
     * @param value The value to subtract
     * @return The new value
     */
    public Value subtract(final Value value) {
        return new Value(CP, getAsCP() - value.getAsCP());
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
     */
    public Value(final int goldPieces) {
        this(0, 0, goldPieces);
    }

    /**
     * Fractional Gold pieces only.
     * @param goldPieces The amount of GP
     */
    public Value(final double goldPieces) {
        this(GP, goldPieces);
    }

    /**
     * Copper, Silver and Gold pieces only.
     * @param copperPieces The amount of CP
     * @param silverPieces The amount of SP
     * @param goldPieces The amount of GP
     */
    public Value(
            final int copperPieces,
            final int silverPieces,
            final int goldPieces
    ) {
        this(CP, copperPieces, SP, silverPieces, GP, goldPieces);
    }

    /**
     * Fractional one type.
     * @param type The coin type
     * @param amount The amount
     */
    public Value(final Coin type, final double amount) {
        this(type, amount, CP, 0);
    }

    /**
     * Fractional two types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     */
    public Value(
            final Coin type1,
            final double amount1,
            final Coin type2,
            final double amount2
    ) {
        addCoin(type1, amount1);
        addCoin(type2, amount2);
        maxTarget();
    }

    /**
     * Any type whole amount.
     * @param type The coin type
     * @param amount The amount
     */
    public Value(final Coin type, final int amount) {
        this(type, amount, CP, 0);
    }

    /**
     * Whole two types.
     * @param type1 The first coin type
     * @param amount1 The first coin amount
     * @param type2 The second coin type
     * @param amount2 The second coin amount
     */
    public Value(
            final Coin type1,
            final int amount1,
            final Coin type2,
            final int amount2
    ) {
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
     */
    public Value(
            final Coin type1,
            final int amount1,
            final Coin type2,
            final int amount2,
            final Coin type3,
            final int amount3
    ) {
        addCoin(type1, amount1);
        addCoin(type2, amount2);
        addCoin(type3, amount3);
        maxTarget();
    }

    /**
     * All {@link Coin} types separately.
     * @param copperPieces Amount of CP
     * @param silverPieces Amount of SP
     * @param electrumPieces Amount of EP
     * @param goldPieces Amount of GP
     * @param platinumPieces Amount of PP
     */
    public Value(
            final int copperPieces,
            final int silverPieces,
            final int electrumPieces,
            final int goldPieces,
            final int platinumPieces
    ) {
        this.cp = copperPieces;
        this.sp = silverPieces;
        this.ep = electrumPieces;
        this.gp = goldPieces;
        this.pp = platinumPieces;
        maxTarget();
    }

    /**
     * <p>Create a value from a carefully formatted string.</p>
     * <h3></h3>
     *
     * <h3>Format options:</h3>
     * <li>0 <i>Only 0. Non-0 requires a type</i></li>
     * <li>5 gp</li>
     * <li>5gp</li>
     * <li>5  gp</li>
     * <li>5pp</li>
     * <li>0.5pp</li>
     * <li>0.3pp</li>
     * <li>1\tpp</li>
     * <li>(etc)</li>
     * @param value The string representation of a value
     */
    public Value(final String value) {
        String cleanValue = value
                .toLowerCase(Locale.ROOT)
                .replaceAll(" ", "")
                .replaceAll("\t", "")
                .replaceAll(",", ".")
                .strip();
        if (cleanValue.equals("0")) {
            return;
        }
        String end = cleanValue.substring(cleanValue.length() - 2);
        Coin type = switch (end) {
            case "cp" -> CP;
            case "sp" -> SP;
            case "ep" -> EP;
            case "gp" -> GP;
            case "pp" -> PP;
            default -> throw new RuntimeException(
                    "Value (" + cleanValue + ")"
                    + " ends with '" + end + "'"
                    + " which is not cp, sp, ep, gp or pp"
            );
        };
        double amount = Double.parseDouble(cleanValue.replace(end, ""));
        assert amount >= 0;
        addCoin(type, amount);
        maxTarget();
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
            return;
        }

        // round amount
        if (Math.round(amount) == amount) {
            addCoin(type, (int) amount);
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
     * Maximizes the amount of currency in a specific coin in this value.<p>
     * Uses the local constant array 'targets' to define order
     */
    private void maxTarget() {
        assert cp >= 0;
        assert sp >= 0;
        assert ep >= 0;
        assert gp >= 0;
        assert pp >= 0;
        int totalCP = getAsCP();

        // Reset value counters (all value is stored in 'totalCP')
        this.cp = 0;
        this.sp = 0;
        this.ep = 0;
        this.gp = 0;
        this.pp = 0;

        // Conversion factors
        final int cpCp = 1;
        final int spCp = SP.decrementFactor() * cpCp;
        final int epCp = EP.decrementFactor() * spCp;
        final int gpCp = GP.decrementFactor() * epCp;
        final int ppCp = PP.decrementFactor() * gpCp;

        /*
         * It is a bit unclear what happens here
         * So I will elaborate:
         *
         * We loop through all the target coin types
         * We then find the highest amount of that coin possible,
         * based on the amount of CP (the total CP in the Value)
         *
         * We then add that amount to the corresponding value
         * And then subtract the CP amount corresponding to that,
         * from the total remaining CP
         *
         * Finally, the remaining CP is stored
         */

        for (Coin t : TARGET) {
            if (totalCP != 0) {
                switch (t) {
                    case CP -> totalCP -=
                            (this.cp = (totalCP));
                    case SP -> totalCP -= spCp
                            * (this.sp = (totalCP - (totalCP % spCp)) / spCp);
                    case EP -> totalCP -= epCp
                            * (this.ep = (totalCP - (totalCP % epCp)) / epCp);
                    case GP -> totalCP -= gpCp
                            * (this.gp = (totalCP - (totalCP % gpCp)) / gpCp);
                    case PP -> totalCP -= ppCp
                            * (this.pp = (totalCP - (totalCP % ppCp)) / ppCp);
                    default -> throw new RuntimeException(
                            "Unhandled coin type"
                    );
                }
            }
        }
        this.cp += totalCP; // In case CP is not in the 'target' array
    }

    @Override
    public String toString() {
        return "c" + cp
                + " s" + sp
                + " e" + ep
                + " g" + gp
                + " p" + pp
                + " totals " + getAsGP() + "gp";
    }
}
