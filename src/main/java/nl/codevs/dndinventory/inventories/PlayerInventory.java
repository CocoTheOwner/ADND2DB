package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.data.Money;
import nl.codevs.dndinventory.data.StonePouch;
import nl.codevs.dndinventory.inventories.interfaces.*;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.util.ArrayList;
import java.util.List;

@EverythingIsNonNull
public class PlayerInventory extends Inventory
        implements IMoney, IWeighted, IStats, IHealth, ILevel {

    /**
     * The amount of worth in the inventory
     * {@link nl.codevs.dndinventory.data.Money}.
     */
    private Money money;
    /**
     * Character strength.
     */
    private final int str;
    /**
     * Character dexterity.
     */
    private final int dex;
    /**
     * Character constitution.
     */
    private final int con;
    /**
     * Character intelligence.
     */
    private final int intl;
    /**
     * Character wisdom.
     */
    private final int wis;
    /**
     * Character charisma.
     */
    private final int chr;
    /**
     * Character complexion.
     */
    private final int com;
    /**
     * Character health.
     */
    private int health;
    /**
     * Max character strength.
     */
    private final int mHp;
    /**
     * Character class.
     */
    private final CharacterClass cc;
    /**
     * Character experience.
     */
    private int exp;
    /**
     * Actual character level.
     */
    private int realLvl;
    /**
     * Stone pouch.
     */
    private final StonePouch stones;

    /**
     * Test inventory.
     */
    public static final PlayerInventory TEST_INVENTORY = new PlayerInventory(
            "Test Inventory",
            new ArrayList<>(),
            new Money(0),
            new StonePouch(),
            ILevel.CharacterClass.FIGHTER,
            8_400,
            2,
            39,
            15, 13, 18, 9, 11, 10, 12
    );

    static {
        TEST_INVENTORY.getMoney().setSimplify(false);
    }

    /**
     * Create a player inventory.
     * @param playerName The player name
     * @param startingItems The starting player items
     * @param startingMoney The amount of starting capital
     * @param stonePouch Stones in the player inventory
     * @param characterClass The
     * {@link CharacterClass} of the player (Fighter, etc.)
     * @param experience The current experience of the player
     * @param actualLevel The actual (trained) level of the character
     * @param maxHealth MaxHealth
     * @param strength Strength
     * @param dexterity Dexterity
     * @param constitution Constitution
     * @param intelligence Intelligence
     * @param wisdom Wisdom
     * @param charisma Charisma
     * @param complexion Complexion
     */
    public PlayerInventory(
            final String playerName,
            final List<InventoryItem> startingItems,
            final Money startingMoney,
            final StonePouch stonePouch,
            final CharacterClass characterClass,
            final int experience,
            final int actualLevel,
            final int maxHealth,
            final int strength,
            final int dexterity,
            final int constitution,
            final int intelligence,
            final int wisdom,
            final int charisma,
            final int complexion
    ) {

        super(playerName, startingItems);
        money = startingMoney;
        this.stones = stonePouch;
        this.cc = characterClass;
        this.exp = experience;
        this.realLvl = actualLevel;
        this.str = strength;
        this.dex = dexterity;
        this.con = constitution;
        this.intl = intelligence;
        this.wis = wisdom;
        this.chr = charisma;
        this.com = complexion;
        this.mHp = maxHealth;
        money.setSimplify(false);
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
     * Get the stones in the inventory.
     * @return The stones
     */
    public StonePouch getStones() {
        return stones;
    }

    /**
     * Set newMoney in the inventory.
     *
     * @param newMoney The new newMoney value
     */
    @Override
    public void setMoney(final nl.codevs.dndinventory.data.Money newMoney) {
        newMoney.setSimplify(false);
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
        return mHp;
    }

    /**
     * Set the health.
     *
     * @param newHealth the new health amount
     */
    @Override
    public void setHealth(final int newHealth) {
        health = newHealth;
    }

    /**
     * Character strength.
     *
     * @return -5 to 25, literal noodle to stronk
     */
    @Override
    public int getStrength() {
        return str;
    }

    /**
     * Character dexterity.
     *
     * @return -5 to 25, about as agile as a wall to being a literal snake
     */
    @Override
    public int getDexterity() {
        return dex;
    }

    /**
     * Character constitution.
     *
     * @return -5 to 25, exhausted all the time to ADHD
     */
    @Override
    public int getConstitution() {
        return con;
    }

    /**
     * Character intelligence.
     *
     * @return -5 to 25, useless to creative
     */
    @Override
    public int getIntelligence() {
        return intl;
    }

    /**
     * Character wisdom.
     *
     * @return -5 to 25, goldfish brain to big brain
     */
    @Override
    public int getWisdom() {
        return wis;
    }

    /**
     * Character charisma.
     *
     * @return -5 to 25, rude to besties with everyone
     */
    @Override
    public int getCharisma() {
        return chr;
    }

    /**
     * Character looks.
     *
     * @return -5 to 25, ugly af to beautiful
     */
    @Override
    public int getComplexion() {
        return com;
    }

    /**
     * Get the current amount of experience.
     *
     * @return the current amount of experience
     */
    @Override
    public int getExperience() {
        return exp;
    }

    /**
     * Add experience.
     *
     * @param experience the experience to add
     */
    @Override
    public void addExperience(final int experience) {
        this.exp += experience;
    }

    /**
     * Get the current character's class.
     *
     * @return the current character's class
     */
    @Override
    public CharacterClass getCharacterClass() {
        return cc;
    }

    /**
     * Get the current character's actual level.
     */
    @Override
    public int getActualLevel() {
        return realLvl;
    }

    /**
     * Set the actual level.
     *
     * @param actualLevel the new actual level
     */
    @Override
    public void setActualLevel(final int actualLevel) {
        this.realLvl = actualLevel;
    }
}
