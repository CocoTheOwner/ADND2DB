package nl.codevs.dndinventory.data;

public enum ItemType {
    ANIMALS("Animals", 4),
    ARMOR("Armor", 1),
    CLOTHING("Clothing", 3),
    WEAPONS("Weapons", 0),
    FOODLODGING("Daily Food and Lodging", ItemType.NON_CARRY),
    HARNESS("Tack and Harness", 3),
    MAGIC("Magic Items", 2),
    MISC("Miscellaneous Equipment", 3),
    PROVISIONS("Household Provisions", 5),
    ;

    /**
     * 'Items' that cannot be carried (such as services).
     */
    private static final int NON_CARRY = Integer.MAX_VALUE;

    /**
     * Category name.
     */
    private final String name;

    /**
     * Category position (lower = higher priority in inventory).
     */
    private final int pos;

    /**
     * Get category name
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Get position
     * @return Category position
     */
    public int getPos() {
        return pos;
    }

    ItemType(String typeName, int position) {
        name = typeName;
        pos = position;
    }
}
