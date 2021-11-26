package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.inventories.interfaces.MoneyInventory;
import nl.codevs.dndinventory.inventories.interfaces.Stats;
import nl.codevs.dndinventory.inventories.interfaces.Weighted;

import java.util.List;

public class PlayerInventory extends Inventory
        implements MoneyInventory, Weighted, Stats {

    /**
     * The amount of worth in the inventory {@link nl.codevs.dndinventory.data.Money}.
     */
    private nl.codevs.dndinventory.data.Money money;

    /**
     * The maximal carry weight for this inventory.
     */
    private final double maxWeight;
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
            final nl.codevs.dndinventory.data.Money startingMoney,
            final double maximalEncumbrance,
            int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, int complexion, int maxHealth) {

        super(inventoryName, startingItems);
        money = startingMoney;
        maxWeight = maximalEncumbrance;
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
    public Inventory inventory() {
        return this;
    }

    /**
     * The amount of worth the inventory possesses.
     *
     * @return The Value
     */
    @Override
    public nl.codevs.dndinventory.data.Money money() {
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
     * Max weight capacity.
     *
     * @return The max amount of weight capacity
     */
    @Override
    public double maxWeight() {
        return maxWeight;
    }

    /**
     * Convert the inventory to Json.
     * @return A json string
     */
    @Override
    public String toJson() {
        return Inventory.GSON.toJson(this);
    }

    /**
     * Add any additional stats you want to show in the "stats" column.
     *
     * @return A string for the stats to display
     */
    @Override
    protected String getAdditionalStats() {
        return "weight left: " + getRemainingWeight() + " of " + maxWeight();
    }

    /**
     * Get amount of space left.
     * @return The amount of leftover space. Can be negative (over-encumbered)
     */
    @Override
    public double getRemainingWeight() {
        return Weighted.super.getRemainingWeight() - money.getWeight();
    }

    /**
     * Current character health.
     *
     * @return the current health
     */
    @Override
    public int health() {
        return health;
    }

    /**
     * Get the maximal character health.
     *
     * @return the maximal health
     */
    @Override
    public int maxHealth() {
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
    public int strength() {
        return strength;
    }

    /**
     * Character dexterity.
     *
     * @return -5 to 25, about as agile as a wall to being a literal snake
     */
    @Override
    public int dexterity() {
        return dexterity;
    }

    /**
     * Character constitution.
     *
     * @return -5 to 25, exhausted all the time to ADHD
     */
    @Override
    public int constitution() {
        return constitution;
    }

    /**
     * Character intelligence.
     *
     * @return -5 to 25, useless to creative
     */
    @Override
    public int intelligence() {
        return intelligence;
    }

    /**
     * Character wisdom.
     *
     * @return -5 to 25, goldfish brain to big brain
     */
    @Override
    public int wisdom() {
        return wisdom;
    }

    /**
     * Character charisma.
     *
     * @return -5 to 25, rude to besties with everyone
     */
    @Override
    public int charisma() {
        return charisma;
    }

    /**
     * Character looks.
     *
     * @return -5 to 25, ugly af to beautiful
     */
    @Override
    public int complexion() {
        return complexion;
    }
}
