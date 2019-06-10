/**
 * Project: A00980723_assignment2
 * File: Bcmc.java
 * Date: 1 èþë. 2017 ã.
 * Time: 13:40:56
 */

package a00980723.bcmc;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a00980723.bcmc.data.CustomerDao;
import a00980723.bcmc.data.Database;
import a00980723.bcmc.data.InventoryDao;
import a00980723.bcmc.data.MotorcycleDao;
import a00980723.gui.GuiController;
import a00980723.gui.MainFrame;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class Bcmc {

	private static MotorcycleDao motorcycles;
	private static CustomerDao customers;
	private static InventoryDao inventory;

	private static final String DROP_OPTION = "-drop";
	private Database db;
	private static Instant startTime;
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Configures log4j2 from the external configuration file specified in LOG4J_CONFIG_FILENAME.
	 * If the configuration file isn't found then log4j2's DefaultConfiguration is used.
	 */
	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format("WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

	/**
	 * Entry point to GIS
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		startTime = Instant.now();
		LOG.info("Start: " + startTime);

		if (args.length == 1 && args[0].equalsIgnoreCase(DROP_OPTION)) {
			Database.requestDbTableDrop();
		}

		Bcmc bcmc = null;
		try {
			bcmc = new Bcmc();
			bcmc.run();
		} catch (Exception e) {
			GuiController.handle(e);

		}

		LOG.debug("main thread exiting");
	}

	@SuppressWarnings("static-access")
	public Bcmc() throws FileNotFoundException, IOException, BcmcException {
		db = Database.getTheInstance();
		db.init();
		try {
			customers.init();
			motorcycles.init();
			inventory.init();
		} catch (BcmcException e) {
			GuiController.handle(e);
		}
	}

	/**
	 * Populate the customers and print them out.
	 */
	private void run() {
		try {
			createUI();
		} catch (Exception e) {
			GuiController.handle(e);

		}
	}

	public static void createUI() {
		LOG.debug("Creating the UI");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.debug("Setting the Look & Feel");
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}

					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					GuiController.handle(e);

				}
			}
		});
	}

	/**
	 * @return the startTime
	 */
	public static Instant getStartTime() {
		return startTime;
	}
}