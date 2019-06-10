/**
 * Project: A00980723_assignment2
 * File: Dao.java
 * Date: 1 ���. 2017 �.
 * Time: 13:43:32
 */

package a00980723.bcmc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.data.Database;
import a00980723.gui.GuiController;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public abstract class Dao {
	private static Logger LOG = LogManager.getLogger();
	protected final Database database;
	protected final String tableName;

	protected Dao(String tableName) {
		database = Database.getTheInstance();
		this.tableName = tableName;
	}

	public abstract void create() throws SQLException;

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * Delete the database table
	 * 
	 * @throws SQLException
	 */
	public void drop() throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			if (Database.tableExists(tableName)) {
				LOG.debug("drp table " + tableName);
				statement.executeUpdate("drop table " + tableName);
			}
		} finally {
			close(statement);
		}
	}

	/**
	 * Tell the database we're shutting down.
	 */
	public void shutdown() {
		database.shutdown();
		LOG.debug("database shutdown");
	}

	protected void create(String createStatement) throws SQLException {
		LOG.debug(createStatement);
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
		} finally {
			close(statement);
		}
	}

	protected boolean execute(String preparedStatementString, Object... args) throws SQLException {
		int parameterCount = 0;
		int index = 0;
		int fromIndex = 0;
		do {
			index = preparedStatementString.indexOf("?", fromIndex);
			if (index != -1) {
				parameterCount++;
			}
			fromIndex = index + 1;
		} while (index != -1);

		LOG.debug(preparedStatementString);
		LOG.debug(Arrays.toString(args));
		LOG.debug("parameter count = " + parameterCount);

		boolean result = false;
		PreparedStatement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.prepareStatement(preparedStatementString);
			int i = 1;
			for (Object object : args) {
				LOG.debug("Setting param " + i + ": " + object.toString() + " of type " + object.getClass().getName());
				if (object instanceof String) {
					statement.setString(i, object.toString());
				} else if (object instanceof Boolean) {
					statement.setBoolean(i, (Boolean) object);
				} else if (object instanceof Integer) {
					statement.setInt(i, (Integer) object);
				} else if (object instanceof Long) {
					statement.setLong(i, (Long) object);
				} else if (object instanceof Float) {
					statement.setFloat(i, (Float) object);
				} else if (object instanceof Double) {
					statement.setDouble(i, (Double) object);
				} else if (object instanceof Byte) {
					statement.setByte(i, (Byte) object);
				} else if (object instanceof Timestamp) {
					statement.setTimestamp(i, (Timestamp) object);
				} else if (object instanceof LocalDate) {
					LocalDate date = (LocalDate) object;
					Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());
					statement.setTimestamp(i, timestamp);
				} else if (object instanceof LocalDateTime) {
					statement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) object));
				} else {
					statement.setString(i, object.toString());
				}

				i++;
			}

			result = statement.execute();
		} finally {
			close(statement);
		}

		return result;
	}

	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			GuiController.handle(e);

		}
	}

	public int getRowCount(Statement statement) throws SQLException {
		int count = -1;
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
		while (resultSet.next()) {
			count = resultSet.getInt(1);
		}

		return count;
	}

	public static Timestamp toTimestamp(LocalDate date) {
		return Timestamp.valueOf(LocalDateTime.of(date, LocalTime.now()));
	}

	public static Timestamp toTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
