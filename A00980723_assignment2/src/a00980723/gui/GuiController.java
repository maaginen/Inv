/**
 * Project: A00980723_assignment2
 * File: GGuiController.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:08:16
 */

package a00980723.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.Bcmc;
import a00980723.bcmc.data.Database;
import a00980723.bcmc.data.Inventory;
import a00980723.bcmc.data.InventoryDao;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

public class GuiController {

	private static final Logger LOG = LogManager.getLogger();
	private static MainFrame mainFrame;
	private static InventoryDialog inventoryDialog;
	private static InventoryDao inventoryDao;
	private static boolean inDescending;
	private static boolean byCount;
	private static boolean byDescription;
	private static String make = "";

	protected GuiController(MainFrame mainFrame) {
		GuiController.setMainFrame(mainFrame);
		inventoryDao = InventoryDao.getTheinstance();
	}

	public static void handle(Exception e) {
		JOptionPane.showMessageDialog(GuiController.getMainFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		LOG.error(e.getMessage());
	}

	public static void informUnfinishedFiture(String message, String feture) {
		JOptionPane.showMessageDialog(GuiController.getMainFrame(), message, feture, JOptionPane.INFORMATION_MESSAGE);
		LOG.debug(message);
	}

	protected static class QuitMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			LOG.debug("Exit menu item pressed.");
			exit(0);
		}
	}

	protected static class CustomerMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			LOG.debug("Customer menu item pressed.");
			GuiController.informUnfinishedFiture("Not ready yet", "Customer");
		}
	}

	protected static class ServiceMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			LOG.debug("Service menu item pressed.");
			GuiController.informUnfinishedFiture("Not ready yet", "Service");
		}
	}

	protected static class InventoryMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				LOG.debug("InventoryForSqlServer menu item pressed.");

				InventoryDialog dialog = new InventoryDialog(getMainFrame());
				JList<InventoryListItem> inventoryList = dialog.getInventoryList();
				inventoryList.getSelectionModel().addListSelectionListener(new GuiController.InventoryListSelectionHandler(inventoryList));
				dialog.setVisible(true);
			} catch (Exception e) {
				GuiController.handle(e);
			}
		}
	}

	protected static class TotalMenuItemHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			int total = 0;
			InventoryDao inventoryDao = InventoryDao.getTheinstance();
			try {
				total = inventoryDao.getTotalValue();
			} catch (Exception e) {
				GuiController.handle(e);
			}
			LOG.debug("Total menu item pressed.");
			String message = String.format("The total value of current inventory: $%,.2f", total / 100.0f);
			JOptionPane.showMessageDialog(GuiController.getMainFrame(), message, "Reprt 'total value'", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected static class DescendingCheckBoxHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				if (!inDescending) {
					LOG.debug("DescendingForSqlServer checkBox pressed.");
					setInDescending(true);
				} else {
					LOG.debug("DescendingForSqlServer checkBox unpressed.");
					setInDescending(false);
				}
			} catch (Exception e) {
				GuiController.handle(e);
			}
		}
	}

	protected static class ByDescriptionMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				LOG.debug("ByDescriptionForSqlServer menu item pressed.");
				byDescription = true;
				ReportFrame dialog = new ReportFrame(getMainFrame());
				dialog.setVisible(true);
			} catch (Exception e) {
				GuiController.handle(e);
			}
		}
	}

	protected static class ByCountMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				LOG.debug("ByCountForSqlServer menu item pressed.");
				byCount = true;
				ReportFrame dialog = new ReportFrame(getMainFrame());
				dialog.setVisible(true);
			} catch (Exception e) {
				GuiController.handle(e);
			}
		}
	}

	protected static class MakeMenuItemHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			JFrame frame = new JFrame("InputDialog Make");
			// make = JOptionPane.showInputDialog(frame, "Type your motorcycle make name:");
			try {
				LOG.debug("ByDescriptionForSqlServer menu item pressed.");
				make = JOptionPane.showInputDialog(frame, "Type your motorcycle make name:");
				ReportFrame dialog = new ReportFrame(getMainFrame());
				dialog.setVisible(true);
			} catch (Exception e) {
				GuiController.handle(e);
			}

		}
	}

	protected static class AboutMenuItemHandler implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			LOG.debug("About menu item pressed.");
			String message = "Assignment_2 \nVolodymyr Protsenko \nA00980723";
			JOptionPane.showMessageDialog(GuiController.getMainFrame(), message, "About Bcmc", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected static class InventoryListSelectionHandler implements ListSelectionListener {
		private JList<InventoryListItem> inventoryList;
		private InventoryListModel inventoryModel;

		/**
		 * @param customersList
		 */
		public InventoryListSelectionHandler(JList<InventoryListItem> inventoryList) {
			this.inventoryList = inventoryList;
			// sorry about the cast!
			inventoryModel = (InventoryListModel) inventoryList.getModel();
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				return;
			}

			InventoryListItem part = inventoryList.getSelectedValue();
			if (part != null) {
				LOG.debug("InventoryForSqlServer selected: " + inventoryList.getSelectedIndex());
				updatePart(part, inventoryList.getSelectedIndex());
			}
		}

		protected void updatePart(InventoryListItem item, int index) {
			try {
				InventoryItemDialog dialog = new InventoryItemDialog(inventoryDialog, item.getPart());
				dialog.setVisible(true);
				Inventory part = dialog.getItem();
				if (part != null) {
					LOG.debug("Updating CustomerForSqlServer: " + part.getPartNumber());
					inventoryDao.update(part);
					item.setPart(part);
					inventoryModel.update(index, item);
				}

				inventoryList.clearSelection();
			} catch (Exception e) {
				GuiController.handle(e);
			}
		}
	}

	protected static class MainFrameWindowEevntHandler extends WindowAdapter {

		/**
		 * Invoked when a window is in the process of being closed.
		 * The close operation can be overridden at this point.
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			Database.getTheInstance().shutdown();
			exit(0);
		}
	}

	public static void exit(int exitCode) {
		Instant endTime = Instant.now();
		LOG.info("End: " + endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(Bcmc.getStartTime(), endTime).toMillis()));
		System.exit(exitCode);
	}

	/**
	 * @return the mainFrame
	 */
	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @param mainFrame
	 *            the mainFrame to set
	 */
	public static void setMainFrame(MainFrame mainFrame) {
		GuiController.mainFrame = mainFrame;
	}

	/**
	 * @return the byCount
	 */
	public static boolean isByCount() {
		return byCount;
	}

	/**
	 * @param byCount
	 *            the byCount to set
	 */
	public static void setByCount(boolean byCount) {
		GuiController.byCount = byCount;
	}

	/**
	 * @return the byDescription
	 */
	public static boolean isByDescription() {
		return byDescription;
	}

	/**
	 * @param byDescription
	 *            the byDescription to set
	 */
	public static void setByDescription(boolean byDescription) {
		GuiController.byDescription = byDescription;
	}

	/**
	 * @return the make
	 */
	public static String getMake() {
		return make;
	}

	/**
	 * @param make
	 *            the make to set
	 */
	public static void setMake(String make) {
		GuiController.make = make;
	}

	/**
	 * @return the inDescending
	 */
	public boolean isInDescending() {
		return inDescending;
	}

	/**
	 * @param inDescending
	 *            the inDescending to set
	 */
	public static void setInDescending(boolean inDescending) {
		GuiController.inDescending = inDescending;
	}

	public static boolean getInDescending() {
		return inDescending;
	}
}
