package nl.codevs.dndinventory.inventories.interfaces;

public interface IHealth extends IInterface {

    /**
     * Current character health.
     * @return the current health
     */
    int getHealth();

    /**
     * Get the maximal character health.
     * @return the maximal health
     */
    int getMaxHealth();

    /**
     * Set the health.
     * @param newHealth the new health amount
     */
    void setHealth(int newHealth);

    /**
     * Damage the character.
     * @param damage damage to the character
     */
    default void damage(int damage) {
        setHealth(Math.min(getMinHealth(), getHealth() - damage));
    }

    /**
     * Get absolute minimal health (can be negative).
     */
    default int getMinHealth() {
        return -10;
    }
}
