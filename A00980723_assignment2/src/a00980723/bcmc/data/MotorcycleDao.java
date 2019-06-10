/**
 * Project: A00980723_assignment2
 * File: MotorcycleDao.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:58:18
 */

package a00980723.bcmc.data;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.portable.ApplicationException;

import a00980723.bcmc.BcmcException;
import a00980723.bcmc.dao.Dao;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class MotorcycleDao extends Dao {

	private static MotorcycleDao theInstance;
	private static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Motorcycles";
	private static final String MOTORCYCLES_DATA_FILENAME = "motorcycles.dat";
	private static Logger LOG = LogManager.getLogger();

	private MotorcycleDao() {
		super(TABLE_NAME);
	}

	/**
	 * @return the theinstance
	 */
	public static MotorcycleDao getTheinstance() {
		return theInstance;
	}

	public static synchronized void init() throws BcmcException {
		if (theInstance == null) {
			theInstance = new MotorcycleDao();
		}
		int motorcycleCount = theInstance.load();
		LOG.debug("Inserted " + motorcycleCount + " motorcycles");
	}

	/**
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	private int load() throws BcmcException {
		int motorcycleCount = 0;
		File file = new File(MOTORCYCLES_DATA_FILENAME);
		try {
			if ((!(Database.tableExists(MotorcycleDao.TABLE_NAME)) || Database.dbTableDropRequested())) {
				if (Database.tableExists(MotorcycleDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}

				create();

				LOG.debug("Inserting the motorcycles");

				// if (!file.exists()) {
				// String message = String.format("Required '%s' is missing.", MOTORCYCLES_DATA_FILENAME);
				// JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
				// }

				motorcycleCount = MotorcycleReader.read(file, this);
			} else {

				Connection connection = database.getConnection();
				Statement statement = connection.createStatement();
				motorcycleCount = getRowCount(statement);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return motorcycleCount;
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format(
				"CREATE TABLE %s(%s BIGINT, %s VARCHAR(%d), %s VARCHAR(%d), %s BIGINT, %s VARCHAR(%d), %s BIGINT, %s BIGINT, PRIMARY KEY (%s))", // ,
																																					// FOREIGN
																																					// KEY
																																					// (%s)
																																					// REFERENCES
																																					// "
				// + DbConstants.CUSTOMER_TABLE_NAME + " (ID))",
				TABLE_NAME, //
				Column.ID.name, //
				Column.MAKE.name, Column.MAKE.length, //
				Column.MODEL.name, Column.MODEL.length, //
				Column.YEAR.name, //
				Column.SERIAL_NUMBER.name, Column.SERIAL_NUMBER.length, //
				Column.MILEAGE.name, //
				Column.CUSTOMER_ID.name, //

				// Primary key
				Column.ID.name, //
				// Foreign key
				Column.CUSTOMER_ID.name);

		super.create(sqlString);
	}

	public void add(Motorcycle motorcycle) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				motorcycle.getId(), //
				motorcycle.getMake(), //
				motorcycle.getModel(), //
				motorcycle.getYear(), //
				motorcycle.getSerialNumber(), //
				motorcycle.getMileage(), //
				motorcycle.getCustomerId());

		LOG.debug(String.format("Adding %s was %s", motorcycle, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the motorcycle.
	 * 
	 * @param motorcycleForSqlServer
	 * @throws SQLException
	 */
	public void update(Motorcycle motorcycle) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.MAKE.name, //
				Column.MODEL.name, //
				Column.YEAR.name, //
				Column.SERIAL_NUMBER.name, //
				Column.MILEAGE.name, //
				Column.CUSTOMER_ID.name, //
				Column.ID.name);
		LOG.debug("Update statment: " + sqlString);

		@SuppressWarnings("unused")
		boolean result = execute(sqlString, motorcycle.getMake(), motorcycle.getModel(), motorcycle.getYear(), motorcycle.getSerialNumber(),
				motorcycle.getMileage(), motorcycle.getCustomerId(), motorcycle.getId());
		LOG.debug(String.format("Updated %s", motorcycle));
	}

	/**
	 * Delete the motorcycle from the database.
	 * 
	 * @param motorcycleForSqlServer
	 * @throws SQLException
	 */
	public void delete(Motorcycle motorcycle) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, motorcycle.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the motorcycle IDs from the database
	 * 
	 * @return the list of motorcycle IDs
	 * @throws SQLException
	 */
	public List<Long> getMotorcycleIds() throws SQLException {
		List<Long> ids = new ArrayList<>();

		String selectString = String.format("SELECT %s FROM %s", Column.ID.name, TABLE_NAME);
		LOG.debug(selectString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getLong(Column.ID.name));
			}

		} finally {
			close(statement);
		}

		LOG.debug(String.format("Loaded %d motorcycle IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public Motorcycle getMotorcycle(Long motorcycleId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, motorcycleId);
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
					throw new BcmcException(String.format("Expected one result, got %d", count));
				}

				Motorcycle motorcycle = new Motorcycle.Builder(resultSet.getInt(Column.ID.name()))//
						.setMake(resultSet.getString(Column.MAKE.name))//
						.setModel(resultSet.getString(Column.MODEL.name))//
						.setYear(resultSet.getInt(Column.YEAR.name))//
						.setSerialNumber(resultSet.getString(Column.SERIAL_NUMBER.name))//
						.setMileage(resultSet.getInt(Column.MILEAGE.name))//
						.setCustomerId(resultSet.getInt(Column.CUSTOMER_ID.name))//
						.build();

				return motorcycle;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	public enum Column {
		ID("id", 16), //
		MAKE("make", 20), //
		MODEL("model", 20), //
		YEAR("a_year", 10), //
		SERIAL_NUMBER("serialNumber", 20), //
		MILEAGE("mileage", 7), //
		CUSTOMER_ID("customerID", 14); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}
