/**
 * Project: A00980723_assignment2
 * File: InventoryReader.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:54:34
 */

package a00980723.bcmc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.BcmcException;
import a00980723.gui.GuiController;
import a00980723.io.Reader;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class InventoryReader extends Reader {

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	private InventoryReader() {
	}

	/**
	 * Read the inventory input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of inventory.
	 * @throws BcmcException
	 */
	public static int read(File inventoryDataFile, InventoryDao dao) throws BcmcException {
		LOG.debug("reading data from " + inventoryDataFile + "file, and setting the dao object");
		BufferedReader inventoryReader = null;
		int inventoryCount = 0;

		try {
			inventoryReader = new BufferedReader(new FileReader(inventoryDataFile));

			String line = null;
			line = inventoryReader.readLine(); // skip the header line
			while ((line = inventoryReader.readLine()) != null) {
				Inventory inventory = buildInventoryFromString(line);
				try {
					dao.add(inventory);
					inventoryCount++;
				} catch (SQLException e) {
					GuiController.handle(e);
				}
			}
		} catch (IOException e) {
			GuiController.handle(e);
		} finally {
			try {
				if (inventoryReader != null) {
					inventoryReader.close();
				}
			} catch (IOException e) {
				GuiController.handle(e);
			}
		}
		LOG.debug("Red " + inventoryCount + " inventory items from " + inventoryDataFile + "file");
		return inventoryCount;
	}

	/**
	 * Parse a motorcycle data string into a Motorcycle object;
	 * 
	 * @param row
	 * @throws BcmcException
	 */
	private static Inventory buildInventoryFromString(String data) throws BcmcException {
		String[] elements = getElements(data, Inventory.ATTRIBUTE_COUNT);
		int index = 0;
		String motorcycleId = elements[index++];
		String description = elements[index++];
		String partNumber = elements[index++];
		int price = Integer.parseInt(elements[index++]);
		int quantity = Integer.parseInt(elements[index++]);

		return new Inventory.Builder(partNumber).setDescription(description).setMotorcycleId(motorcycleId).setPrice(price).setQuantity(quantity)
				.build();

	}

}
