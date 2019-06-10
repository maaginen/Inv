/**
 * Project: A00980723_assignment2
 * File: InventoryDialog.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:13:57
 */

package a00980723.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.data.Inventory;
import a00980723.bcmc.data.InventoryDao;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

@SuppressWarnings("serial")
public class InventoryDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();
	private JList<InventoryListItem> inventoryList;
	private InventoryListModel inventoryModel;

	/**
	 * Create the dialog.
	 */
	public InventoryDialog(JFrame frame) {
		super(frame, true);
		setTitle("Inventory");

		setBounds(200, 200, 800, 400);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		inventoryModel = new InventoryListModel();
		setInventoryList(new JList<>(inventoryModel));
		getInventoryList().setSelectedIndex(2);
		getInventoryList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(new JScrollPane(getInventoryList()));
		setData();

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						InventoryDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						InventoryDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * set the data
	 */
	private void setData() {
		LOG.debug("setting inventory data");

		try {
			InventoryDao inventoryDao = InventoryDao.getTheinstance();
			List<String> partNumbers = inventoryDao.getInventoryPartsNums();
			for (String partNumber : partNumbers) {
				Inventory part = inventoryDao.getPart(partNumber);
				inventoryModel.add(part);
			}
		} catch (Exception e) {
			GuiController.handle(e);
		}
	}

	/**
	 * @return the inventoryList
	 */
	public JList<InventoryListItem> getInventoryList() {
		return inventoryList;
	}

	/**
	 * @param inventoryList
	 *            the inventoryList to set
	 */
	public void setInventoryList(JList<InventoryListItem> inventoryList) {
		this.inventoryList = inventoryList;
		inventoryList.setBackground(Color.LIGHT_GRAY);
		inventoryList.setFont(new Font("Monospaced", Font.PLAIN, 11));
	}

}
