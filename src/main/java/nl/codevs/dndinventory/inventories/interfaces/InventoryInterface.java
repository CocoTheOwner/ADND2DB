package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Inventory;

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
