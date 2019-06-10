/**
 * Project: A00980723_assignment2
 * File: MainFrame.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:07:57
 */

package a00980723.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger LOG = LogManager.getLogger();

	private JPanel contentPane;
	private JMenuItem mntmExit;
	private JMenuItem mntmAbout;
	private JMenu mnData;
	private JMenu mnReports;
	private JMenuItem mntmCustomers;
	private JSeparator separator_1;
	private JMenuItem mntmService;
	private JMenuItem mntmInventory;
	private JSeparator separator_2;
	private JMenuItem mntmTotal;
	private JMenuItem mntmByDescription;
	private JCheckBox chckbxDescending;
	private JSeparator separator;
	private JSeparator separator_3;
	private JMenuItem mntmByCount;
	private JMenuItem mntmMake;
	private JSeparator separator_4;
	private JLabel lblNewLabel;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		LOG.debug("Creating the MainFrame");

		createUi();

		addEventHandlers();

	}

	/**
	 * 
	 */
	private void createUi() {
		LOG.debug("createUi");
		setTitle("BCMC");

		setBounds(100, 100, 640, 480);

		// BufferedImage myImage = ImageIO.read(n);
		// JFrame myJFrame = new JFrame("Image pane");
		// myJFrame.setContentPane(new ImagePanel(myImage));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Quit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);

		mnData = new JMenu("Data");
		menuBar.add(mnData);

		mntmCustomers = new JMenuItem("Customers");
		mntmCustomers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.SHIFT_MASK));
		mnData.add(mntmCustomers);

		separator_1 = new JSeparator();
		mnData.add(separator_1);

		mntmService = new JMenuItem("Service");
		mntmService.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK));
		mnData.add(mntmService);

		separator_2 = new JSeparator();
		mnData.add(separator_2);

		mntmInventory = new JMenuItem("Inventory");
		mntmInventory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_MASK));
		mnData.add(mntmInventory);

		mnReports = new JMenu("Reports");
		menuBar.add(mnReports);

		mntmTotal = new JMenuItem("Total");
		mnReports.add(mntmTotal);

		separator = new JSeparator();
		mnReports.add(separator);

		chckbxDescending = new JCheckBox("Descending");
		chckbxDescending.setHorizontalAlignment(SwingConstants.LEFT);
		mnReports.add(chckbxDescending);

		separator_3 = new JSeparator();
		mnReports.add(separator_3);

		mntmByDescription = new JMenuItem("By Description");
		mnReports.add(mntmByDescription);

		mntmByCount = new JMenuItem("By Count");
		mnReports.add(mntmByCount);

		separator_4 = new JSeparator();
		mnReports.add(separator_4);

		mntmMake = new JMenuItem("Make");
		mnReports.add(mntmMake);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmAbout);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/images/moto.jpg")));
		lblNewLabel.setBounds(0, 0, 624, 421);
		contentPane.add(lblNewLabel);

	}

	/**
	 * 
	 */
	private void addEventHandlers() {
		LOG.debug("addEventHandlers");

		new GuiController(this);

		addWindowListener(new GuiController.MainFrameWindowEevntHandler());

		mntmTotal.addActionListener(new GuiController.TotalMenuItemHandler());
		mntmCustomers.addActionListener(new GuiController.CustomerMenuItemHandler());
		mntmService.addActionListener(new GuiController.ServiceMenuItemHandler());
		mntmInventory.addActionListener(new GuiController.InventoryMenuItemHandler());
		mntmExit.addActionListener(new GuiController.QuitMenuItemHandler());
		mntmAbout.addActionListener(new GuiController.AboutMenuItemHandler());
		mntmByDescription.addActionListener(new GuiController.ByDescriptionMenuItemHandler());
		mntmByCount.addActionListener(new GuiController.ByCountMenuItemHandler());
		chckbxDescending.addActionListener(new GuiController.DescendingCheckBoxHandler());
		mntmMake.addActionListener(new GuiController.MakeMenuItemHandler());
	}
}
