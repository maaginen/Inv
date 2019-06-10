/**
 * Project: A00980723_assignment2
 * File: CustomerReader.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:48:29
 */

package a00980723.bcmc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.BcmcException;
import a00980723.gui.GuiController;
import a00980723.io.Reader;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class CustomerReader extends Reader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	private CustomerReader() {
	}

	/**
	 * Read the customer input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of customers.
	 * @throws BcmcException
	 */
	public static int read(File customerDataFile, CustomerDao dao) throws BcmcException {
		LOG.debug("reading data from " + customerDataFile + "file, and setting the dao object");
		BufferedReader customerReader = null;
		int customerCount = 0;

		try {
			customerReader = new BufferedReader(new FileReader(customerDataFile));

			String line = null;
			line = customerReader.readLine(); // skip the header line
			while ((line = customerReader.readLine()) != null) {
				Customer customer = readCustomerString(line);
				try {
					dao.add(customer);
					customerCount++;
				} catch (SQLException e) {
					GuiController.handle(e);
				}
			}
		} catch (IOException e) {
			LOG.debug(e.getMessage());
		} finally {
			try {
				if (customerReader != null) {
					customerReader.close();
				}
			} catch (IOException e) {
				LOG.debug(e.getMessage());
			}
		}
		LOG.debug("red " + customerCount + " customers from " + customerDataFile + "file");
		return customerCount;
	}

	/**
	 * Parse a Customer data string into a CUstomer object;
	 * 
	 * @param row
	 * @throws BcmcException
	 */
	private static Customer readCustomerString(String data) throws BcmcException {

		String[] elements = getElements(data, Customer.ATTRIBUTE_COUNT);

		int index = 0;
		long id = Integer.parseInt(elements[index++]);
		String firstName = elements[index++];
		String lastName = elements[index++];
		String street = elements[index++];
		String city = elements[index++];
		String postalCode = elements[index++];
		String phone = elements[index++];

		String emailAddress = elements[index++];
		if (!Validator.validateEmail(emailAddress)) {
			GuiController.handle(new BcmcException(String.format("Invalid email: %s", emailAddress)));
		}
		String yyyymmdd = elements[index];
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			GuiController.handle(new BcmcException(String.format("Invalid joined date: %s for customer %d", yyyymmdd, id)));
		}
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(4, 6)) - 1;
		int day = Integer.parseInt(yyyymmdd.substring(6, 8));

		return new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city).setPostalCode(postalCode)
				.setEmailAddress(emailAddress).setJoinedDate(year, month, day).build();
	}

	private static class Validator {

		private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		private static final String YYYYMMDD_PATTERN = "(20\\d{2})(\\d{2})(\\d{2})"; // valid for years 2000-2099

		private static Pattern emailPattern;
		private static Pattern yyyymmddPattern;

		private Validator() {
		}

		/**
		 * Validate an email string.
		 * 
		 * @param email
		 *            the email string.
		 * @return true if the email address is valid, false otherwise.
		 */
		public static boolean validateEmail(final String email) {
			if (emailPattern == null) {
				emailPattern = Pattern.compile(EMAIL_PATTERN);
			}

			Matcher matcher = emailPattern.matcher(email);
			return matcher.matches();
		}

		public static boolean validateJoinedDate(String yyyymmdd) {
			if (yyyymmddPattern == null) {
				yyyymmddPattern = Pattern.compile(YYYYMMDD_PATTERN);
			}

			Matcher matcher = yyyymmddPattern.matcher(yyyymmdd);
			return matcher.matches();
		}

	}
}
