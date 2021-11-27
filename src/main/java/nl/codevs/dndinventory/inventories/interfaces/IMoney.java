package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Money;

public interface IMoney extends IInterface {

    /**
     * The amount of worth the inventory possesses.
     * @return The Value
     */
    Money getMoney();

    /**
     * Set worth in the inventory.
     * @param money The new worth value
     */
    void setMoney(Money money);

    /**
     * Whether this inventory can afford a value.
     * @param money The Value requested
     * @return True if it can, false if not
     */
    default boolean canAfford(Money money) {
        return money.getAsCP() <= getMoney().getAsCP();
    }

    /**
     * Pay a certain amount.
     * @param amount The amount to pay
     * @throws TooPoorException When there are insufficient funds available
     */
    default void pay(Money amount) throws TooPoorException {
        if (canAfford(amount)) {
            setMoney(getMoney().subtract(amount));
        } else {
            throw new TooPoorException(amount.subtract(getMoney()));
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
        public TooPoorException(final nl.codevs.dndinventory.data.Money remaining) {
            super("You need " + remaining + " gp more!");
        }
    }
}
