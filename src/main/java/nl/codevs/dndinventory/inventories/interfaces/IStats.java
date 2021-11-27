package nl.codevs.dndinventory.inventories.interfaces;

public interface IStats extends IHealth {

    /**
     * Character strength.
     * @return -5 to 25, literal noodle to stronk
     */
    int getStrength();

    /**
     * Character dexterity.
     * @return -5 to 25, about as agile as a wall to being a literal snake
     */
    int getDexterity();

    /**
     * Character constitution.
     * @return -5 to 25, exhausted all the time to ADHD
     */
    int getConstitution();

    /**
     * Character intelligence.
     * @return -5 to 25, useless to creative
     */
    int getIntelligence();

    /**
     * Character wisdom.
     * @return -5 to 25, goldfish brain to big brain
     */
    int getWisdom();

    /**
     * Character charisma.
     * @return -5 to 25, rude to besties with everyone
     */
    int getCharisma();

    /**
     * Character looks.
     * @return -5 to 25, ugly af to beautiful
     */
    int getComplexion();

    /**
     * Get absolute minimal health (can be negative).
     */
    @Override
    default int getMinHealth() {
        return -(getConstitution() + 1);
    }
}
