/**
 * Project: A00980723_assignment2
 * File: BcmcException.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:40:40
 */

package a00980723.bcmc;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

@SuppressWarnings("serial")
public class BcmcException extends Exception {

	/**
	 * Construct an ApplicationException
	 * 
	 * @param message
	 *            the exception message.
	 */
	public BcmcException(String message) {
		super(message);
	}

	/**
	 * Construct an ApplicationException
	 * 
	 * @param throwable
	 *            a Throwable.
	 */
	public BcmcException(Throwable throwable) {
		super(throwable);
	}

}
