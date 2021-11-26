package nl.codevs.dndinventory.inventories.interfaces;

public interface Stats extends Health {

    /**
     * Character strength.
     * @return -5 to 25, literal noodle to stronk
     */
    int strength();

    /**
     * Character dexterity.
     * @return -5 to 25, about as agile as a wall to being a literal snake
     */
    int dexterity();

    /**
     * Character constitution.
     * @return -5 to 25, exhausted all the time to ADHD
     */
    int constitution();

    /**
     * Character intelligence.
     * @return -5 to 25, useless to creative
     */
    int intelligence();

    /**
     * Character wisdom.
     * @return -5 to 25, goldfish brain to big brain
     */
    int wisdom();

    /**
     * Character charisma.
     * @return -5 to 25, rude to besties with everyone
     */
    int charisma();

    /**
     * Character looks.
     * @return -5 to 25, ugly af to beautiful
     */
    int complexion();

    /**
     * Get absolute minimal health (can be negative).
     */
    @Override
    default int minHealth() {
        return -(constitution() + 1);
    }
}
