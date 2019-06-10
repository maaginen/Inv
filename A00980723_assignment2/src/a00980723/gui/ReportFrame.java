/**
 * Project: A00980723_assignment2
 * File: ReportFrame.java
 * Date: 1 èþë. 2017 ã.
 * Time: 14:25:26
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
public class ReportFrame extends JDialog {
	JTextArea textArea;
	protected String makeQuery;

	public static final String REPORT_FILENAME = "inventory_report.txt";
	public static final String HORIZONTAL_LINE = "================================================================================================";
	public static final String HEADER_FORMAT = "%-25s %-28s %-15s %8s %11s";
	public static final String ROW_FORMAT = "%-25s %-28s %-15s %,8.2f %,11d";

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ReportFrame(JFrame frame) {
		super(frame, true);
		setTitle("Report 'inventory'");

		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		getContentPane().add(textArea, BorderLayout.CENTER);

		setBounds(200, 200, 900, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setEditable(false);

		setReport();
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		getContentPane().add(new JScrollPane(areaScrollPane));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// updatePart();
						ReportFrame.this.dispose();
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
						ReportFrame.this.dispose();
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
	private void setReport() {
		LOG.debug("prepering report");
		String header = String.format(HEADER_FORMAT, "Motorcycle ID", "Description", "PartNumber", "Price", "Quantity") + "\n" + HORIZONTAL_LINE
				+ "\n";
		textArea.append(header);
		try {
			InventoryDao inventoryDao = InventoryDao.getTheinstance();
			List<String> partNumbers = inventoryDao.getInventorySorted();
			for (String partNumber : partNumbers) {
				Inventory part = inventoryDao.getPart(partNumber);

				String partData = String.format(ROW_FORMAT, part.getMotorcycleId(), part.getDescription(), part.getPartNumber(),
						part.getPrice() / 100.0f, part.getQuantity()) + "\n";
				textArea.append(partData);
			}
		} catch (Exception e) {
			GuiController.handle(e);
		}

	}

}
