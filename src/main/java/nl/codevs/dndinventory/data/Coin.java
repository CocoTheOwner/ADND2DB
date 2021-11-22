package nl.codevs.dndinventory.data;

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
            case CP -> throw new RuntimeException("Cannot decrementFactor CP");
            case SP -> S_TO_C;
            case EP -> E_TO_S;
            case GP -> G_TO_P;
            case PP -> P_TO_G;
        };
    }
}
