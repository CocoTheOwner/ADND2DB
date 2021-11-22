package nl.codevs.dndinventory.data;

public enum ItemType {
    ANIMALS("Animals", 4),
    ARMOR("Armor", 1),
    CLOTHING("Clothing", 3),
    WEAPONS("Weapons", 0),
    FOOD_LODGING("Daily Food and Lodging", ItemType.NON_CARRY),
    HARNESS("Tack and Harness", 3),
    MAGIC("Magic Items", 2),
    MISC("Miscellaneous Equipment", 3),
    PROVISIONS("Household Provisioning", 5),
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

    /**
     * Get item type from string
     * @param in The input string
     * @return The {@link ItemType} belonging to the input string
     * @throws RuntimeException When the input string does not match an {@link ItemType}
     */
    public static ItemType fromString(String in) throws RuntimeException {
        for (ItemType value : ItemType.values()) {
            if (value.name.equals(in) || value.getName().equals(in) || value.toString().equals(in)) {
                return value;
            }
        }
        throw new RuntimeException("Cannot convert '" + in + "' to valid ItemType");
    }
}
