/**
 * Project: A00980723_assignment2
 * File: DbConstants.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:45:21
 */

package a00980723.bcmc.data;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public interface DbConstants {

	String DB_PROPERTIES_FILENAME = "db.properties";
	String DB_DRIVER_KEY = "db.driver";
	String DB_URL_KEY = "db.url";
	String DB_USER_KEY = "db.user";
	String DB_PASSWORD_KEY = "db.password";
	String TABLE_ROOT = "S456_";
	String CUSTOMER_TABLE_NAME = TABLE_ROOT + "Customer";
	String MOTORCYCLE_TABLE_NAME = TABLE_ROOT + "Motorcycle";
	String INVENTORY_TABLE_NAME = TABLE_ROOT + "Inventory";

}
