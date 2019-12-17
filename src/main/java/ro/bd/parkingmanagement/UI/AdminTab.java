package ro.bd.parkingmanagement.UI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import ro.bd.parkingmanagement.DAO.ParkingLotDAO;
import ro.bd.parkingmanagement.DAO.ParkingLotDAOImpl;
import ro.bd.parkingmanagement.DAO.PriceDAO;
import ro.bd.parkingmanagement.DAO.PriceDAOImpl;
import ro.bd.parkingmanagement.DAO.SubscriptionDAO;
import ro.bd.parkingmanagement.DAO.SubscriptionDAOImpl;
import ro.bd.parkingmanagement.DAO.TicketDAO;
import ro.bd.parkingmanagement.DAO.TicketDAOImpl;
import ro.bd.parkingmanagement.DAO.UserDAO;
import ro.bd.parkingmanagement.DAO.UserDAOImpl;

public class AdminTab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private javax.swing.JButton addEntryButton;
	private javax.swing.JButton refreshButton;
	private javax.swing.JTabbedPane administrativePanel;
	private javax.swing.JScrollPane parkingLotsTab;
	private javax.swing.JTable parkingLotsTable;
	private javax.swing.JScrollPane pricesTab;
	private javax.swing.JTable pricesTable;
	private javax.swing.JScrollPane subscriptionsTab;
	private javax.swing.JTable subscriptionsTable;
	private javax.swing.JScrollPane ticketsTab;
	private javax.swing.JTable ticketsTable;
	private javax.swing.JScrollPane usersTab;
	private javax.swing.JTable usersTable;
	private static UserDAO userDAO = new UserDAOImpl();
	private static PriceDAO priceDAO = new PriceDAOImpl();
	private static ParkingLotDAO parkingSpaceDAO = new ParkingLotDAOImpl();
	private static SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
	private static TicketDAO ticketDAO = new TicketDAOImpl();

	public static UserDAO getUserDAO() {
		return userDAO;
	}

	public static PriceDAO getPriceDAO() {
		return priceDAO;
	}

	public static ParkingLotDAO getParkingSpaceDAO() {
		return parkingSpaceDAO;
	}

	public static SubscriptionDAO getSubscriptionDAO() {
		return subscriptionDAO;
	}

	public static TicketDAO getTicketDAO() {
		return ticketDAO;
	}

	Action delete = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			System.out.println(table.getColumnName(1));
			((DefaultTableModel) table.getModel()).removeRow(modelRow);
		}
	};

	Action update = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			System.out.println(table.getColumnName(1));
			System.out.println(modelRow);

		}
	};

	/**
	 * Creates new form AdminForm
	 */
	public AdminTab() {
		initComponents();
		administrativePanel.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int tab = administrativePanel.getSelectedIndex();
				switch (tab) {
				case 0:
					usersTable = userDAO.populateJTableFromDB();
					usersTab.setViewportView(usersTable);
					break;
				case 1:
					pricesTable = priceDAO.populateJTableFromDB();
					pricesTab.setViewportView(pricesTable);
					break;
				case 2:
					parkingLotsTable = parkingSpaceDAO.populateJTableFromDB();
					parkingLotsTab.setViewportView(parkingLotsTable);
					break;
				case 3:
					subscriptionsTable = subscriptionDAO.populateJTableFromDB();
					subscriptionsTab.setViewportView(subscriptionsTable);
					break;
				case 4:
					ticketsTable = ticketDAO.populateJTableFromDB();
					ticketsTab.setViewportView(ticketsTable);
					break;

				}
			}
		});

	}

	private void initComponents() {

		administrativePanel = new javax.swing.JTabbedPane();
		usersTab = new javax.swing.JScrollPane();
		usersTable = userDAO.populateJTableFromDB();
		pricesTab = new javax.swing.JScrollPane();
		pricesTable = priceDAO.populateJTableFromDB();
		parkingLotsTab = new javax.swing.JScrollPane();
		parkingLotsTable = parkingSpaceDAO.populateJTableFromDB();
		subscriptionsTab = new javax.swing.JScrollPane();
		subscriptionsTable = subscriptionDAO.populateJTableFromDB();
		ticketsTab = new javax.swing.JScrollPane();
		ticketsTable = ticketDAO.populateJTableFromDB();
		addEntryButton = new javax.swing.JButton();
		refreshButton = new javax.swing.JButton();

		administrativePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Administrative",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Liberation Sans", 0, 12))); // NOI18N
		administrativePanel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N

		usersTab.setViewportView(usersTable);

		administrativePanel.addTab("Users", usersTab);

		pricesTab.setViewportView(pricesTable);

		administrativePanel.addTab("Prices", pricesTab);

		parkingLotsTab.setViewportView(parkingLotsTable);

		administrativePanel.addTab("Parking lots", parkingLotsTab);

		subscriptionsTab.setViewportView(subscriptionsTable);

		administrativePanel.addTab("Subscriptions", subscriptionsTab);

		ticketsTab.setViewportView(ticketsTable);

		administrativePanel.addTab("Tickets", ticketsTab);

		addEntryButton.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
		addEntryButton.setText("Add");
		addEntryButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addEntryButtonActionPerformed(evt);
			}
		});

		refreshButton.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
		refreshButton.setText("Refresh tab");
		refreshButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(addEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(administrativePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(administrativePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEntryButton)
                    .addComponent(refreshButton))
                .addContainerGap())
        );
	}

	// add button from the admin tab
	private void addEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int index = administrativePanel.getSelectedIndex();
		DefaultTableModel model;
		switch (index) {
		case 0:
			model = (DefaultTableModel) usersTable.getModel();
			model.addRow(new Object[] { null, null, null, null, null });
			for (int i = 0; i < usersTable.getRowCount(); i++) {
				model.setValueAt("Update", i, usersTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, usersTable.getColumnCount() - 1);
			}
			break;
		case 1:
			model = (DefaultTableModel) pricesTable.getModel();
			model.addRow(new Object[] { null, null, null });
			for (int i = 0; i < pricesTable.getRowCount(); i++) {
				model.setValueAt("Update", i, pricesTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, pricesTable.getColumnCount() - 1);
			}
			break;
		case 2:
			model = (DefaultTableModel) parkingLotsTable.getModel();
			model.addRow(new Object[] { null, null, null, null });
			for (int i = 0; i < parkingLotsTable.getRowCount(); i++) {
				model.setValueAt("Update", i, parkingLotsTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, parkingLotsTable.getColumnCount() - 1);
			}
			break;
		case 3:
			model = (DefaultTableModel) subscriptionsTable.getModel();
			model.addRow(new Object[] { null, null, null, null });
			for (int i = 0; i < subscriptionsTable.getRowCount(); i++) {
				model.setValueAt("Update", i, subscriptionsTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, subscriptionsTable.getColumnCount() - 1);
			}
			break;
		case 4:
			model = (DefaultTableModel) ticketsTable.getModel();
			model.addRow(new Object[] { null, null, null, null });
			for (int i = 0; i < ticketsTable.getRowCount(); i++) {
				model.setValueAt("Update", i, ticketsTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, ticketsTable.getColumnCount() - 1);
			}
			break;
		}
	}

	//TODO: update DAOs
	private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int tab = administrativePanel.getSelectedIndex();
		switch (tab) {
		case 0:
			usersTable = userDAO.populateJTableFromDB();
			usersTab.setViewportView(usersTable);
			break;
		case 1:
			pricesTable = priceDAO.populateJTableFromDB();
			pricesTab.setViewportView(pricesTable);
			break;
		case 2:
			parkingLotsTable = parkingSpaceDAO.populateJTableFromDB();
			parkingLotsTab.setViewportView(parkingLotsTable);
			break;
		case 3:
			subscriptionsTable = subscriptionDAO.populateJTableFromDB();
			subscriptionsTab.setViewportView(subscriptionsTable);
			break;
		case 4:
			ticketsTable = ticketDAO.populateJTableFromDB();
			ticketsTab.setViewportView(ticketsTable);
			break;

		}
	}
}