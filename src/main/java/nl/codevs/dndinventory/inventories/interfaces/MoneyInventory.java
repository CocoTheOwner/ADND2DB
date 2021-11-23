package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Value;

public interface MoneyInventory extends InventoryInterface {

    /**
     * The amount of money the inventory possesses.
     * @return The Value
     */
    Value money();

    /**
     * Set money in the inventory.
     * @param value The new money value
     */
    void setMoney(Value value);

    /**
     * Whether this inventory can afford a value.
     * @param value The Value requested
     * @return True if it can, false if not
     */
    default boolean canAfford(Value value) {
        return value.getAsCP() <= money().getAsCP();
    }

    /**
     * Pay a certain amount.
     * @param amount The amount to pay
     * @throws TooPoorException When you have insufficient value
     */
    default void pay(Value amount) throws TooPoorException {
        if (canAfford(amount)) {
            setMoney(money().subtract(amount));
        } else {
            throw new TooPoorException(amount.subtract(money()));
        }
    }

    /**
     * Exception used to indicate something is too expensive.
     */
    final class TooPoorException extends Exception {

        /**
         * Exception thrown when something is too expensive.
         * @param remaining The remaining value
         */
        public TooPoorException(final Value remaining) {
            super("You need " + remaining + " gp more!");
        }
    }
}
