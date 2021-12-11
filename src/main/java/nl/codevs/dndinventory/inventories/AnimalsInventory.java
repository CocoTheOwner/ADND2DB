package nl.codevs.dndinventory.inventories;

import nl.codevs.dndinventory.inventories.interfaces.IWeighted;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.util.List;

@EverythingIsNonNull
public class AnimalsInventory extends Inventory implements IWeighted {

    /**
     * The animals whose inventory this is.
     */
    final List<Animal> animals;

    /**
     * Generate from an existing, loaded, inventory.
     *
     * @param inventoryName The name of the inventory
     * @param inventoryItems The inventory mapping
     * @param herd The {@link Animal}s whose inventory this is
     */
    public AnimalsInventory(
            final String inventoryName,
            final List<InventoryItem> inventoryItems,
            final List<Animal> herd
    ) {
        super(inventoryName, inventoryItems);
        animals = herd;
    }

    /**
     * Add any additional stats to show in the "stats" column.
     *
     * @return A string for the stats to display
     */
    @Override
    protected String getAdditionalStats() {
        return "weight left: " + getRemainingWeight() + " of " + getMaxWeight();
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
     * Max weight capacity.
     *
     * @return The max amount of weight capacity
     */
    @Override
    public int getMaxWeight() {
        return animals.stream().mapToInt(Animal::getCarries).sum();
    }

    /**
     * An animal.
     */
    public static class Animal {
        /**
         * A mule.
         */
        public static final Animal MULE = new Animal("Mule", 250);
        /**
         * A horse.
         */
        public static final Animal HORSE = new Animal("Horse", 250);

        /**
         * The name of the animal.
         */
        private final String name;
        /**
         * The weight the animal can carry.
         */
        private final int carries;
        /**
         * Animal stats.
         */
        private final String stats;

        /**
         * Create a new animal.
         * @param animalName the name of the animal
         * @param maxWeight the weight the animal can carry
         */
        public Animal(
                final String animalName,
                final int maxWeight
        ) {
            this(animalName, maxWeight, "");
        }

        /**
         * Create a new animal.
         * @param animalName the name of the animal
         * @param maxWeight the weight the animal can carry
         * @param details the animal details
         */
        public Animal(
                final String animalName,
                final int maxWeight,
                final String details
        ) {
            name = animalName;
            carries = maxWeight;
            stats = details;
        }

        /**
         * Get the animal name.
         * @return the animal name
         */
        public String getName() {
            return name;
        }

        /**
         * Get the weight the animal can carry.
         * @return the weight the animal can carry
         */
        public int getCarries() {
            return carries;
        }

        /**
         * Get animal stats (details).
         * @return animal stats
         */
        public String getStats() {
            return stats;
        }
    }

    /**
     * Get the animals of this inventory.
     * @return the animals
     */
    public List<Animal> getAnimals() {
        return animals;
    }
}
