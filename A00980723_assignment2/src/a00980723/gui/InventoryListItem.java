/**
 * Project: A00980723_assignment2
 * File: InventoryListItem.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:14:10
 */

package a00980723.gui;

import a00980723.bcmc.data.Inventory;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class InventoryListItem {
	private Inventory part;

	/**
	 * @param inventoryForSqlServer
	 */
	public InventoryListItem(Inventory part) {
		this.part = part;
	}

	/**
	 * @return the customerForSqlServer
	 */
	public Inventory getPart() {
		return part;
	}

	/**
	 * @param partForSqlServer
	 *            the partForSqlServer to set
	 */
	public void setPart(Inventory partForSqlServer) {
		this.part = partForSqlServer;
	}

	@Override
	public String toString() {
		if (part == null) {
			return null;
		}

		return String.format("%-20s | %-30s | %-20s | %-10d | %-10d", part.getMotorcycleId(), part.getDescription(), part.getPartNumber(),
				part.getPrice(), part.getQuantity());
	}
}
