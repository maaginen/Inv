/**
 * Project: A00980723_assignment2
 * File: InventoryDao.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:54:23
 */

package a00980723.bcmc.data;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.BcmcException;
import a00980723.bcmc.dao.Dao;
import a00980723.gui.GuiController;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class InventoryDao extends Dao {

	private static InventoryDao theInstance;
	private static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Inventory";
	private static final String INVENTORY_DATA_FILENAME = "inventory.dat";
	private static Logger LOG = LogManager.getLogger();

	private InventoryDao() {
		super(TABLE_NAME);
	}

	/**
	 * @return the theinstance
	 */
	public static InventoryDao getTheinstance() {
		return theInstance;
	}

	public static synchronized void init() throws BcmcException {
		if (theInstance == null) {
			theInstance = new InventoryDao();
		}
		int inventoryCount = theInstance.load();
		LOG.debug("Inserted " + inventoryCount + " parts");
	}

	/**
	 * @throws BcmcException
	 * @throws SQLException
	 */
	private int load() throws BcmcException {
		int inventoryCount = 0;
		File file = new File(INVENTORY_DATA_FILENAME);
		try {
			if ((!(Database.tableExists(InventoryDao.TABLE_NAME)) || Database.dbTableDropRequested())) {
				if (Database.tableExists(InventoryDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}
				create();
				LOG.debug("Inserting the inventory items");
				if (!file.exists()) {
					String message = String.format("Required '%s' is missing.", INVENTORY_DATA_FILENAME);
					JOptionPane.showMessageDialog(GuiController.getMainFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				inventoryCount = InventoryReader.read(file, this);
			} else {
				Connection connection = database.getConnection();
				Statement statement = connection.createStatement();
				inventoryCount = getRowCount(statement);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return inventoryCount;
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format("CREATE TABLE %s(%s VARCHAR(%d), %s VARCHAR(%d), %s VARCHAR(%d), %s BIGINT, %s BIGINT, PRIMARY KEY (%s))",
				TABLE_NAME, //
				Column.MOTORCYCLE_ID.name, Column.MOTORCYCLE_ID.length, //
				Column.DESCRIPTION.name, Column.DESCRIPTION.length, //
				Column.PART_NUMBER.name, Column.PART_NUMBER.length, //
				Column.PRICE.name, //
				Column.QUANTITY.name, //
				// Primary key
				Column.PART_NUMBER.name);

		super.create(sqlString);
	}

	public void add(Inventory inventory) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				inventory.getMotorcycleId(), //
				inventory.getDescription(), //
				inventory.getPartNumber(), //
				inventory.getPrice(), //
				inventory.getQuantity());
		LOG.debug(String.format("Adding %s was %s", inventory, result ? "unsuccessful" : "successful"));
	}

	/**
	 * Update the inventory.
	 * 
	 * @param inventoryForSqlServer
	 * @throws SQLException
	 */
	public void update(Inventory inventory) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.MOTORCYCLE_ID.name, //
				Column.DESCRIPTION.name, //
				Column.PRICE.name, //
				Column.QUANTITY.name, //
				Column.PART_NUMBER.name);
		LOG.debug("Update statment: " + sqlString);
		@SuppressWarnings("unused")
		boolean result = execute(sqlString, inventory.getMotorcycleId(), inventory.getDescription(), inventory.getPrice(), inventory.getQuantity(),
				inventory.getPartNumber());
		LOG.debug(String.format("Updated %s", inventory));
	}

	/**
	 * Delete the motorcycle from the database.
	 * 
	 * @param motorcycleForSqlServer
	 * @throws SQLException
	 */
	public void delete(Inventory inventory) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.PART_NUMBER.name, inventory.getPartNumber());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the inventory parts' numbers from the database
	 * 
	 * @return the list of inventory parts' numbers
	 * @throws SQLException
	 */
	public List<String> getInventoryPartsNums() throws SQLException {
		List<String> ids = new ArrayList<>();

		String selectString = String.format("SELECT %s FROM %s", Column.PART_NUMBER.name, TABLE_NAME);
		LOG.debug(selectString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getString(Column.PART_NUMBER.name));
			}

		} finally {
			close(statement);
		}

		LOG.debug(String.format("Loaded %d parts from the database", ids.size()));

		return ids;
	}

	/**
	 * Retrieve all the inventory parts' numbers from the database
	 * 
	 * @return the list of inventory parts' numbers
	 * @throws SQLException
	 */
	public List<String> getInventorySorted() throws SQLException {
		List<String> ids = new ArrayList<>();
		boolean descending = GuiController.getInDescending();
		boolean byDescription = GuiController.isByDescription();
		boolean byCount = GuiController.isByCount();
		String queryMake = GuiController.getMake();
		String queryDesc = "";
		String querySort = "";
		// String queryMake = "";
		String fullQuery = "";

		if (descending) {
			queryDesc = "DESC";
		}
		if (byDescription == true) {
			querySort = "DESCRIPTION";
		} else if (byCount == true) {
			querySort = "QUANTITY";
		} else {
			querySort = "PRICE";
		}
		if (queryMake != "") {
			String makeLike = queryMake + "%";
			fullQuery = String.format("SELECT %s FROM %s WHERE LCASE(%s) LIKE LCASE('%s') ORDER BY %s %s", Column.PART_NUMBER.name, TABLE_NAME,
					Column.MOTORCYCLE_ID.name, makeLike, Column.DESCRIPTION.name, queryDesc);
		} else {

			fullQuery = String.format("SELECT %s FROM %s ORDER BY %s %s", Column.PART_NUMBER.name, TABLE_NAME, querySort, queryDesc);
		}
		LOG.debug(fullQuery);
		GuiController.setMake("");
		GuiController.setByCount(false);
		GuiController.setByDescription(false);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(fullQuery);

			while (resultSet.next()) {
				ids.add(resultSet.getString(Column.PART_NUMBER.name));
			}

		} finally {
			close(statement);
		}

		LOG.debug(String.format("Loaded %d parts from the database", ids.size()));

		return ids;
	}

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public Inventory getPart(String partNumber) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, Column.PART_NUMBER.name, partNumber);
		// LOG.debug(sqlString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getTheInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					GuiController.handle(
							new BcmcException(String.format(resultSet.getString(Column.PART_NUMBER.name) + " - expected one result, got %d", count)));
				}

				Inventory inventory = new Inventory.Builder(resultSet.getString(Column.PART_NUMBER.name))//
						.setMotorcycleId(resultSet.getString(Column.MOTORCYCLE_ID.name))//
						.setDescription(resultSet.getString(Column.DESCRIPTION.name))//
						.setPrice(resultSet.getInt(Column.PRICE.name))//
						.setQuantity(resultSet.getInt(Column.QUANTITY.name))//
						.build();

				return inventory;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	/**
	 * 
	 * @return total
	 * @throws Exception
	 */
	public int getTotalValue() throws Exception {
		int total = 0;
		String result = "TOTAL";
		String sqlString = String.format("SELECT SUM(%s * %s) AS %s FROM  %s", Column.PRICE.name, //
				Column.QUANTITY.name, //
				result, //
				TABLE_NAME);
		LOG.debug(sqlString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getTheInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					GuiController.handle(new BcmcException(String.format("Total - expected one result, got %d", count)));
				}

				total = resultSet.getInt(result);
			}
		} finally {
			close(statement);
		}

		return total;
	}

	public enum Column {
		MOTORCYCLE_ID("motorcycleId", 26), //
		DESCRIPTION("description", 30), //
		PART_NUMBER("partNumber", 30), //
		PRICE("price", 16), //
		QUANTITY("quantity", 16); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}
