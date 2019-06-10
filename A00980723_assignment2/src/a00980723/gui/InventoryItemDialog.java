/**
 * Project: A00980723_assignment2
 * File: InventoryItemDialog.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:19:39
 */

package a00980723.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00980723.bcmc.data.Inventory;
import net.miginfocom.swing.MigLayout;

/**
 * @author Volodymyr Protsenko, A00980723
 *
 */

@SuppressWarnings("serial")
public class InventoryItemDialog extends JDialog {
	private static final Logger LOG = LogManager.getLogger();

	public InventoryItemDialog() {
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField motorcycleIDField;
	private JTextField descriptionField;
	private JTextField itemNumberField;
	private JTextField priceField;
	private JTextField quantityField;

	private Inventory item;

	/**
	 * Create the dialog.
	 */
	public InventoryItemDialog(JDialog dialog, Inventory item) {
		super(dialog, true);
		LOG.debug("Item Dialog Start");
		setTitle("item(" + item.getPartNumber() + ")");
		this.item = item;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 360);
		setLocationRelativeTo(dialog);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][]"));

		JLabel lblMotId = new JLabel("Motorcycle ID");
		contentPanel.add(lblMotId, "cell 0 0,alignx trailing");

		motorcycleIDField = new JTextField();
		contentPanel.add(motorcycleIDField, "cell 1 0,growx");
		motorcycleIDField.setColumns(10);

		JLabel lblDescription = new JLabel("Description");
		contentPanel.add(lblDescription, "cell 0 1,alignx trailing");

		descriptionField = new JTextField();
		contentPanel.add(descriptionField, "cell 1 1,growx");
		descriptionField.setColumns(10);

		JLabel lblitemNum = new JLabel("item number");
		contentPanel.add(lblitemNum, "cell 0 2,alignx trailing");

		itemNumberField = new JTextField();
		itemNumberField.setEnabled(false);
		itemNumberField.setEditable(false);
		contentPanel.add(itemNumberField, "cell 1 2,growx");
		itemNumberField.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		contentPanel.add(lblPrice, "cell 0 3,alignx trailing");

		priceField = new JTextField();
		contentPanel.add(priceField, "cell 1 3,growx");
		priceField.setColumns(10);

		JLabel lblQuantity = new JLabel("Quantity");
		contentPanel.add(lblQuantity, "cell 0 4,alignx trailing");

		quantityField = new JTextField();
		contentPanel.add(quantityField, "cell 1 4,growx");
		quantityField.setColumns(10);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						updateItem();
						InventoryItemDialog.this.dispose();
					} catch (Exception e1) {
						GuiController.handle(e1);
					}
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					InventoryItemDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		setItem(item);
	}

	/**
	 * @param customerForSqlServer
	 */
	public void setItem(Inventory item) {
		motorcycleIDField.setText(item.getMotorcycleId());
		descriptionField.setText(item.getDescription());
		itemNumberField.setText(item.getPartNumber());
		priceField.setText(Integer.toString(item.getPrice()));
		quantityField.setText(Integer.toString(item.getQuantity()));

	}

	protected void updateItem() {
		// the item number can't change
		item.setMotorcycleId(motorcycleIDField.getText());
		item.setDescription(descriptionField.getText());
		item.setPrice(Integer.parseInt(priceField.getText()));
		item.setQuantity(Integer.parseInt(quantityField.getText()));

	}

	public Inventory getItem() {
		return item;
	}
}
