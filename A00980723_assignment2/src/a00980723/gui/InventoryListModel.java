/**
 * Project: A00980723_assignment2
 * File: InventoryListModel.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:14:22
 */

package a00980723.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import a00980723.bcmc.data.Customer;
import a00980723.bcmc.data.Inventory;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

@SuppressWarnings("serial")
public class InventoryListModel extends AbstractListModel<InventoryListItem> {

	private List<InventoryListItem> inventoryItems;

	public InventoryListModel() {
		inventoryItems = new ArrayList<>();
	}

	public void setParts(List<Inventory> parts) {
		for (Inventory part : parts) {
			inventoryItems.add(new InventoryListItem(part));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return inventoryItems == null ? 0 : inventoryItems.size();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public InventoryListItem getElementAt(int index) {
		return inventoryItems.get(index);
	}

	/**
	 * Add an element to the list. Modify the behavior of DefaultListModel.addElement to change the text to 'pig-latin'
	 * 
	 * @param element
	 *            element to be added
	 */
	public void add(Inventory part) {
		add(-1, part);
	}

	/**
	 * @param customerForSqlServer
	 * @param index
	 */
	public void add(int index, Inventory part) {
		InventoryListItem item = new InventoryListItem(part);
		if (index == -1) {
			inventoryItems.add(item);
			index = inventoryItems.size() - 1;
		} else {
			inventoryItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	/**
	 * @param index
	 * @param customer
	 */
	public void update(int index, InventoryListItem item) {
		inventoryItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 *
	 * @param obj
	 *            the component to be removed
	 * @return <code>true</code> if the argument was a component of this
	 *         list; <code>false</code> otherwise
	 */
	public boolean remove(Customer customer) {
		int index = inventoryItems.indexOf(customer);
		boolean removed = inventoryItems.remove(customer);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}
