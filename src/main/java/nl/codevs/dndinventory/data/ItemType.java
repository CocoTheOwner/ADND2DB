package nl.codevs.dndinventory.data;

import okhttp3.internal.annotations.EverythingIsNonNull;

import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * Possible item types (categories).
 */
@EverythingIsNonNull
public enum ItemType {
    /**
     * Animals.
     */
    ANIMALS("Animals", 5),
    /**
     * Armor.
     */
    ARMOR("Armor", 2),
    /**
     * Clothing.
     */
    CLOTHING("Clothing", 4),
    /**
     * Weapons.
     */
    WEAPONS("Weapons", 1),
    /**
     * Daily Food and Lodging.
     */
    FOOD_LODGING("Daily Food and Lodging", ItemType.NON_CARRY),
    /**
     * Tack and Harness.
     */
    HARNESS("Tack and Harness", 4),
    /**
     * Magic Items.
     */
    MAGIC("Magic Items", 3),
    /**
     * Miscellaneous Equipment.
     */
    MISC("Miscellaneous Equipment", 4),
    /**
     * Provisions.
     */
    PROVISIONS("Household Provisioning", 6),
    /**
     * Gemstones.
     */
    GEMSTONES("Gemstones", 0);

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
     * Get category name.
     *
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Get position.
     *
     * @return Category position
     */
    public int getPos() {
        return pos;
    }

    ItemType(final String typeName, final int position) {
        name = typeName;
        pos = position;
    }

    /**
     * Get item type from string.
     *
     * @param in The input string
     * @return The {@link ItemType} belonging to the input string
     * @throws InvalidParameterException When the input string
     *                                   does not match an {@link ItemType}
     */
    public static ItemType fromString(String in) throws IllegalArgumentException {
        in = in.toLowerCase(Locale.ROOT);
        for (ItemType value : ItemType.values()) {
            if (value.name.equals(in)
                    || value.getName().equals(in)
                    || value.toString().equals(in)
                    || value.getName().toLowerCase(Locale.ROOT).equals(in)
                    || value.toString().toLowerCase(Locale.ROOT).equals(in)
                    || value.getName().toLowerCase(Locale.ROOT).startsWith(in)
                    || value.toString().toLowerCase(Locale.ROOT).startsWith(in)
                    || value.getName().toLowerCase(Locale.ROOT).endsWith(in)
                    || value.toString().toLowerCase(Locale.ROOT).endsWith(in)
            ) {
                return value;
            }
        }
        throw new InvalidParameterException(
                "Cannot convert '" + in + "' to valid ItemType"
        );
    }
}