package ro.bd.parkingmanagement.UI;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.SwingConstants;

import ro.bd.parkingmanagement.DAO.ParkingLotDAO;
import ro.bd.parkingmanagement.DAO.ParkingLotDAOImpl;
import ro.bd.parkingmanagement.DAO.PriceDAO;
import ro.bd.parkingmanagement.DAO.PriceDAOImpl;
import ro.bd.parkingmanagement.model.Price;

public class DisplayPanelTab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private javax.swing.JScrollPane displayPanel;
	private javax.swing.JTable displayPanelTable;
	private static ParkingLotDAO parkingSpaceDAO = new ParkingLotDAOImpl();
	private static PriceDAO priceDAO = new PriceDAOImpl();

	public DisplayPanelTab() {
		initComponents();
		updateDisplayPanel();
		addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// do nothing
			}

			@Override
			public void focusGained(FocusEvent e) {
				updateDisplayPanel();

			}
		});
		JTableUtilities.setCellsAlignment(displayPanelTable, SwingConstants.CENTER);// centrare text
		displayPanelTable.setRowHeight(30);
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {

		displayPanelTable.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {
	                {null, null},
	                {null, null}
	            },
	            new String [] {
	                "Available", "Price"
	            }
	        ) {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class [] {
	                java.lang.String.class, java.lang.String.class
	            };
	            boolean[] canEdit = new boolean [] {
	                false, false
	            };

	            @SuppressWarnings("rawtypes")
				public Class getColumnClass(int columnIndex) {
	                return types [columnIndex];
	            }

	            public boolean isCellEditable(int rowIndex, int columnIndex) {
	                return canEdit [columnIndex];
	            }
	        });
	        displayPanelTable.setColumnSelectionAllowed(true);
	        displayPanelTable.getTableHeader().setReorderingAllowed(false);
	        displayPanel.setViewportView(displayPanelTable);
	        displayPanelTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
	        );
	}

	public void updateDisplayPanel() {
		int availableLots = parkingSpaceDAO.getAvailableLots();
		int occupiedLots = parkingSpaceDAO.getOccupiedLots();

		List<Price> priceList = priceDAO.getAll();
		displayPanelTable.getModel().setValueAt(
				"" + availableLots + "/" + (availableLots + occupiedLots), 0, 1);

		displayPanelTable.getModel().setValueAt(priceList.get(0).getDayPrice() + " lei / h", 0, 2);
	}
}