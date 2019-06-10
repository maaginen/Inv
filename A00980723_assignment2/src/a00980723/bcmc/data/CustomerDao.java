/**
 * Project: A00980723_assignment2
 * File: CustomerDao.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:48:17
 */

package a00980723.bcmc.data;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.BcmcException;
import a00980723.bcmc.dao.Dao;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class CustomerDao extends Dao {

	private static CustomerDao theInstance;
	private static final String TABLE_NAME = DbConstants.CUSTOMER_TABLE_NAME;
	private static final String CUSTOMERS_DATA_FILENAME = "customers.dat";
	private static Logger LOG = LogManager.getLogger();

	private CustomerDao() {
		super(TABLE_NAME);
	}

	/**
	 * @return the theinstance
	 */
	public static CustomerDao getTheinstance() {
		return theInstance;
	}

	public static synchronized void init() throws BcmcException {

		if (theInstance == null) {
			theInstance = new CustomerDao();
		}
		int customerCount = theInstance.load();
		LOG.debug("Inserted " + customerCount + " customers");
	}

	/**
	 * @throws BcmcException
	 * @throws SQLException
	 */
	private int load() throws BcmcException {
		int customerCount = 0;
		File file = new File(CUSTOMERS_DATA_FILENAME);
		try {
			if ((!(Database.tableExists(CustomerDao.TABLE_NAME)) || Database.dbTableDropRequested())) {
				if (Database.tableExists(CustomerDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}

				create();

				LOG.debug("Inserting the customers");

				customerCount = CustomerReader.read(file, this);
			} else {
				Connection connection = database.getConnection();
				Statement statement = connection.createStatement();
				customerCount = getRowCount(statement);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return customerCount;
	}

	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format(
				"CREATE TABLE %s(%s BIGINT, %s VARCHAR(%d), %s VARCHAR(%d), %s VARCHAR(%d), %s VARCHAR(%d), %s VARCHAR(%d), %s VARCHAR(%d), "
						+ "%s VARCHAR(%d), %s TIMESTAMP, PRIMARY KEY (%s))",
				TABLE_NAME, Column.ID.name, //
				Column.FIRST_NAME.name, Column.FIRST_NAME.length, //
				Column.LAST_NAME.name, Column.LAST_NAME.length, //
				Column.STREET.name, Column.STREET.length, //
				Column.CITY.name, Column.CITY.length, //
				Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, //
				Column.PHONE.name, Column.PHONE.length, //
				Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, //
				Column.JOINED_DATE.name, //
				Column.ID.name);

		super.create(sqlString);
	}

	public void add(Customer customer) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				customer.getId(), //
				customer.getFirstName(), //
				customer.getLastName(), //
				customer.getStreet(), //
				customer.getCity(), //
				customer.getPostalCode(), //
				customer.getPhone(), //
				customer.getEmailAddress(), //
				customer.getJoinedDate());
		LOG.debug(String.format("Adding %s was %s", customer, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the customer.
	 * 
	 * @param customerForSqlServer
	 * @throws SQLException
	 */
	public void update(Customer customer) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.FIRST_NAME.name, //
				Column.LAST_NAME.name, //
				Column.STREET.name, //
				Column.CITY.name, //
				Column.POSTAL_CODE.name, //
				Column.PHONE.name, //
				Column.EMAIL_ADDRESS.name, //
				Column.JOINED_DATE.name, //
				Column.ID.name);
		LOG.debug("Update statment: " + sqlString);

		@SuppressWarnings("unused")
		boolean result = execute(sqlString, customer.getFirstName(), customer.getLastName(), customer.getStreet(), customer.getCity(),
				customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(), customer.getJoinedDate(), customer.getId());
		LOG.debug(String.format("Updated %s", customer));
	}

	/**
	 * Delete the customer from the database.
	 * 
	 * @param customerForSqlServer
	 * @throws SQLException
	 */
	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, customer.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the customer IDs from the database
	 * 
	 * @return the list of customer IDs
	 * @throws SQLException
	 */
	public List<Long> getCustomerIds() throws SQLException {
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

		LOG.debug(String.format("Loaded %d customer IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public Customer getCustomer(Long customerId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, customerId);
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

				Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
				LocalDate date = timestamp.toLocalDateTime().toLocalDate();

				Customer customer = new Customer.Builder(resultSet.getInt(Column.ID.name), resultSet.getString(Column.PHONE.name)) //
						.setFirstName(resultSet.getString(Column.FIRST_NAME.name)) //
						.setLastName(resultSet.getString(Column.LAST_NAME.name)) //
						.setStreet(resultSet.getString(Column.STREET.name)) //
						.setCity(resultSet.getString(Column.CITY.name)) //
						.setPostalCode(resultSet.getString(Column.POSTAL_CODE.name)) //
						.setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)) //
						.setJoinedDate(date).build();

				return customer;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	public enum Column {
		ID("id", 16), //
		FIRST_NAME("firstName", 20), //
		LAST_NAME("lastName", 20), //
		STREET("street", 40), //
		CITY("city", 20), //
		POSTAL_CODE("postalCode", 7), //
		PHONE("phone", 14), //
		EMAIL_ADDRESS("emailAddress", 40), //
		JOINED_DATE("joinedDate", 8); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}
