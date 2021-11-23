package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.inventories.Inventory;

/**
 * Parent interface for all inventory interfaces to use.
 */
public interface InventoryInterface {

    /**
     * The inventory this applies to.
     * @return The inventory
     */
    Inventory inventory();

}
