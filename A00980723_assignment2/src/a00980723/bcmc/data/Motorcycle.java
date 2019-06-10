/**
 * Project: A00980723_assignment2
 * File: Motorcycle.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:58:08
 */

package a00980723.bcmc.data;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class Motorcycle {

	public static final int ATTRIBUTE_COUNT = 7;

	private long id;
	private String make;
	private String model;
	private int year;
	private String serialNumber;
	private int mileage;
	private long customerId;

	public static class Builder {
		// Required parameters
		private long id;

		// Optional parameters
		private String make;
		private String model;
		private int year;
		private String serialNumber;
		private int mileage;
		private long customerId;

		public Builder(long id) {
			this.id = id;
		}

		/**
		 * @param make
		 *            the make to set
		 */
		public Builder setMake(String make) {
			this.make = make;

			return this;
		}

		/**
		 * @param model
		 *            the model to set
		 */
		public Builder setModel(String model) {
			this.model = model;

			return this;
		}

		/**
		 * @param year
		 *            the year to set
		 */
		public Builder setYear(int year) {
			this.year = year;

			return this;
		}

		/**
		 * @param serialNumber
		 *            the serialNumber to set
		 */
		public Builder setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;

			return this;
		}

		/**
		 * @param mileage
		 *            the mileage to set
		 */
		public Builder setMileage(int mileage) {
			this.mileage = mileage;

			return this;
		}

		/**
		 * @param customerId
		 *            the customerId to set
		 */
		public Builder setCustomerId(long customerId) {
			this.customerId = customerId;

			return this;
		}

		public Motorcycle build() {
			return new Motorcycle(this);
		}

	}

	/**
	 * Default constructor
	 */
	private Motorcycle(Builder builder) {
		this.id = builder.id;
		this.make = builder.make;
		this.model = builder.model;
		this.year = builder.year;
		this.serialNumber = builder.serialNumber;
		this.mileage = builder.mileage;
		this.customerId = builder.customerId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * Get the attribute count used for input validation.
	 *
	 * @return the attribute count
	 */
	public static int getAttributeCount() {
		return ATTRIBUTE_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Motorcycle{" + "id='" + id + '\'' + ", make='" + make + '\'' + ", model='" + model + '\'' + ", year=" + year + ", serialNumber='"
				+ serialNumber + '\'' + ", mileage=" + mileage + '}';
	}
}
