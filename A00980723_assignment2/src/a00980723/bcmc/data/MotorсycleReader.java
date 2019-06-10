/**
 * Project: A00980723_assignment2
 * File: MotorCycleReader.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:58:31
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

public class MotorcycleReader extends Reader {

	private static Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	private MotorcycleReader() {
	}

	/**
	 * Read the motorcycle input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of motorcycles.
	 * @throws BcmcException
	 */
	public static int read(File motorcycleDataFile, MotorcycleDao dao) throws BcmcException {
		BufferedReader motorcycleReader = null;
		int motorcycleCount = 0;

		try {
			motorcycleReader = new BufferedReader(new FileReader(motorcycleDataFile));

			String line = null;
			line = motorcycleReader.readLine(); // skip the header line
			while ((line = motorcycleReader.readLine()) != null) {
				Motorcycle motorcycle = readMotorcycleString(line);
				try {
					dao.add(motorcycle);
					motorcycleCount++;
				} catch (SQLException e) {
					GuiController.handle(e);
				}
			}
		} catch (IOException e) {
			LOG.debug(e.getMessage());
		} finally {
			try {
				if (motorcycleReader != null) {
					motorcycleReader.close();
				}
			} catch (IOException e1) {
				LOG.debug(e1.getMessage());
			}
		}

		return motorcycleCount;
	}

	/**
	 * Parse a motorcycle data string into a Motorcycle object;
	 * 
	 * @param row
	 * @throws BcmcException
	 */
	private static Motorcycle readMotorcycleString(String data) throws BcmcException {
		String[] elements = getElements(data, Motorcycle.ATTRIBUTE_COUNT);
		int index = 0;
		long id = Integer.parseInt(elements[index++]);
		String make = elements[index++];
		String model = elements[index++];
		int year = Integer.parseInt(elements[index++]);
		String serialNumber = elements[index++];
		int mileage = Integer.parseInt(elements[index++]);
		long customerId = Integer.parseInt(elements[index++]);

		return new Motorcycle.Builder(id).setMake(make).setModel(model).setYear(year).setSerialNumber(serialNumber).setMileage(mileage)
				.setCustomerId(customerId).build();
	}

}
