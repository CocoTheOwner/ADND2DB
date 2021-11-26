package nl.codevs.dndinventory.inventories.interfaces;

public interface Health {

    /**
     * Current character health.
     * @return the current health
     */
    int health();

    /**
     * Get the maximal character health.
     * @return the maximal health
     */
    int maxHealth();

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
        setHealth(Math.min(minHealth(), health() - damage));
    }

    /**
     * Get absolute minimal health (can be negative).
     */
    default int minHealth() {
        return -10;
    }
}
