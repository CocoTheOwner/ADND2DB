package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.inventories.interfaces.ILevel;
import nl.codevs.dndinventory.inventories.interfaces.IMoney;
import nl.codevs.dndinventory.inventories.interfaces.IStats;
import nl.codevs.dndinventory.inventories.interfaces.IWeighted;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventory extends Inventory
        implements IMoney, IWeighted, IStats, ILevel {

    /**
     * The amount of worth in the inventory {@link nl.codevs.dndinventory.data.Money}.
     */
    private Money money;
    /**
     * Character strength.
     */
    private final int strength;
    /**
     * Character dexterity.
     */
    private final int dexterity;
    /**
     * Character constitution.
     */
    private final int constitution;
    /**
     * Character intelligence.
     */
    private final int intelligence;
    /**
     * Character wisdom.
     */
    private final int wisdom;
    /**
     * Character charisma.
     */
    private final int charisma;
    /**
     * Character complexion.
     */
    private final int complexion;
    /**
     * Character health.
     */
    private int health;
    /**
     * Max character strength.
     */
    private final int maxHealth;
    /**
     * Character class.
     */
    private final CharacterClass characterClass;
    /**
     * Character experience.
     */
    private int experience;
    /**
     * Actual character level.
     */
    private int actualLevel;

    /**
     * Test inventory.
     */
    public static final PlayerInventory TEST_INVENTORY = new PlayerInventory(
            "Test Inventory",
            new ArrayList<>(),
            new Money(0),
            ILevel.CharacterClass.FIGHTER,
            8_400,
            2,
            39,
            15, 13, 18, 9, 11, 10, 12
    );

    /**
     * Create a player inventory.
     * @param inventoryName The inventory name
     * @param startingItems The starting inventory items
     * @param startingMoney The amount of starting capital
     * @param maximalEncumbrance The maximal carry capacity
     * @param strength Strength
     * @param dexterity Dexterity
     * @param constitution Constitution
     * @param intelligence Intelligence
     * @param wisdom Wisdom
     * @param charisma Charisma
     * @param complexion Complexion
     * @param maxHealth MaxHealth
     */
    public PlayerInventory(
            final String inventoryName,
            final List<InventoryItem> startingItems,
            final Money startingMoney,
            final CharacterClass characterClass,
            final int experience,
            final int actualLevel,
            final int maxHealth,
            int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, int complexion
    ) {

        super(inventoryName, startingItems);
        money = startingMoney;
        this.characterClass = characterClass;
        this.experience = experience;
        this.actualLevel = actualLevel;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.complexion = complexion;
        this.maxHealth = maxHealth;
    }

    /**
     * The inventory this applies to.
     *
     * @return The inventory
     */
    @Override
    public Inventory getInventory() {
        return this;
    }

    /**
     * The amount of worth the inventory possesses.
     *
     * @return The Value
     */
    @Override
    public nl.codevs.dndinventory.data.Money getMoney() {
        return money;
    }

    /**
     * Set newMoney in the inventory.
     *
     * @param newMoney The new newMoney value
     */
    @Override
    public void setMoney(final nl.codevs.dndinventory.data.Money newMoney) {
        this.money = newMoney;
    }

    /**
     * Add any additional stats you want to show in the "stats" column.
     *
     * @return A string for the stats to display
     */
    @Override
    protected String getAdditionalStats() {
        return "weight left: " + getRemainingWeight() + " of " + getMaxWeight();
    }

    /**
     * Get amount of space left.
     * @return The amount of leftover space. Can be negative (over-encumbered)
     */
    @Override
    public double getRemainingWeight() {
        return IWeighted.super.getRemainingWeight() - money.getWeight();
    }

    /**
     * Current character health.
     *
     * @return the current health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * Get the maximal character health.
     *
     * @return the maximal health
     */
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set the health.
     *
     * @param newHealth the new health amount
     */
    @Override
    public void setHealth(int newHealth) {
        health = newHealth;
    }

    /**
     * Character strength.
     *
     * @return -5 to 25, literal noodle to stronk
     */
    @Override
    public int getStrength() {
        return strength;
    }

    /**
     * Character dexterity.
     *
     * @return -5 to 25, about as agile as a wall to being a literal snake
     */
    @Override
    public int getDexterity() {
        return dexterity;
    }

    /**
     * Character constitution.
     *
     * @return -5 to 25, exhausted all the time to ADHD
     */
    @Override
    public int getConstitution() {
        return constitution;
    }

    /**
     * Character intelligence.
     *
     * @return -5 to 25, useless to creative
     */
    @Override
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * Character wisdom.
     *
     * @return -5 to 25, goldfish brain to big brain
     */
    @Override
    public int getWisdom() {
        return wisdom;
    }

    /**
     * Character charisma.
     *
     * @return -5 to 25, rude to besties with everyone
     */
    @Override
    public int getCharisma() {
        return charisma;
    }

    /**
     * Character looks.
     *
     * @return -5 to 25, ugly af to beautiful
     */
    @Override
    public int getComplexion() {
        return complexion;
    }

    /**
     * Get the current amount of experience.
     *
     * @return the current amount of experience
     */
    @Override
    public int getExperience() {
        return experience;
    }

    /**
     * Add experience
     *
     * @param experience the experience to add
     */
    @Override
    public void addExperience(int experience) {
        this.experience += experience;
    }

    /**
     * Get the current character's class.
     *
     * @return the current character's class
     */
    @Override
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * Get the current character's actual level.
     */
    @Override
    public int getActualLevel() {
        return actualLevel;
    }

    /**
     * Set the actual level.
     *
     * @param actualLevel the new actual level
     */
    @Override
    public void setActualLevel(int actualLevel) {
        this.actualLevel = actualLevel;
    }
}
