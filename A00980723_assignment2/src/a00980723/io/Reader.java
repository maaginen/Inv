/**
 * Project: A00980723_assignment2
 * File: Reader.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:06:10
 */

package a00980723.io;

import java.util.Arrays;

import org.omg.CORBA.portable.ApplicationException;

import a00980723.bcmc.BcmcException;
import a00980723.bcmc.data.Inventory;
import a00980723.gui.GuiController;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class Reader {
	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	/**
	 * Given a FIELD_DELIMITER delimited input string, parts the string into a String array.
	 * 
	 * @param row
	 *            input string
	 * @return the parsed String array
	 * @throws ApplicationException
	 *             if the element count doesn't match the expected count.
	 */
	public static String[] getElements(String row, int attributeCount) throws BcmcException {
		String[] elements = row.split(FIELD_DELIMITER);
		if (elements.length != attributeCount) {
			GuiController.handle(new BcmcException(
					String.format("Expected %d elements, \n but got %d: %s", Inventory.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements))));
		}

		return elements;
	}
}
