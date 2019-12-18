package ro.bd.parkingmanagement.UI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ro.bd.parkingmanagement.DAO.BankAccountDAO;
import ro.bd.parkingmanagement.DAO.BankAccountDAOImpl;
import ro.bd.parkingmanagement.DAO.ParkingLotDAO;
import ro.bd.parkingmanagement.DAO.ParkingLotDAOImpl;
import ro.bd.parkingmanagement.DAO.PriceDAO;
import ro.bd.parkingmanagement.DAO.PriceDAOImpl;
import ro.bd.parkingmanagement.DAO.SubscriptionDAO;
import ro.bd.parkingmanagement.DAO.SubscriptionDAOImpl;
import ro.bd.parkingmanagement.model.ParkingLot;
import ro.bd.parkingmanagement.model.ParkingSubscription;
import ro.bd.parkingmanagement.model.Price;
import ro.bd.parkingmanagement.model.User;

public class SubscribeTab extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JComboBox<String> bankAccountComboBox;
	private javax.swing.JLabel bankAccountLabel;
	private javax.swing.JButton payButton;
	private javax.swing.JLabel paymentLabel;
	private javax.swing.JPanel selectSubscriptionPanel;
	private javax.swing.JTabbedPane subscriptionPanel;
	private javax.swing.JScrollPane subscriptionsTab;
	private javax.swing.JTable subscriptionsTable;
	private javax.swing.JComboBox<String> timeComboBox;
	private javax.swing.JLabel timeLabel;
	private javax.swing.JScrollPane userSubscriptionsPanel;
	private javax.swing.JLabel totalCostLabel;
	private static SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
	private static PriceDAO priceDAO = new PriceDAOImpl();
	private static BankAccountDAO bankAccountDAO = new BankAccountDAOImpl();
	private static ParkingLotDAO parkingSpaceDAO = new ParkingLotDAOImpl();
	private User currentUser;
	private double amountToPay = 0;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
		subscriptionsTable = subscriptionDAO.populateJTableFromDB(currentUser.getUserId());
		subscriptionsTab.setViewportView(subscriptionsTable);
		String[] cardNumberArray = bankAccountDAO.getAllCardNumbers(currentUser.getUserId());
		bankAccountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(cardNumberArray));
		displayAmountToPay();
	}

	public SubscribeTab() {
		initComponents();
		subscriptionPanel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				paymentLabel.setVisible(false);
				int tab = subscriptionPanel.getSelectedIndex();
				if (tab == 1) {
					subscriptionsTable = subscriptionDAO.populateJTableFromDB(currentUser.getUserId());
					subscriptionsTab.setViewportView(subscriptionsTable);
				}

			}
		});
		timeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					displayAmountToPay();
				}
			}
		});

	}
	
	public void cleanSubscribeTabOnUserLogin() {
		paymentLabel.setVisible(false);
	}

	private void displayAmountToPay() {
		String timePeriod = (String) timeComboBox.getSelectedItem();
		Price p = priceDAO.getAll().get(0);

		if (timePeriod == null)
			timePeriod = "1 month";
		double price = p.getDayPrice();
		double cost = 0.0;
		switch (timePeriod) {
		case "1 month":
			cost = 30 * 1 * price;
			break;
		case "6 months":
			cost = 30 * 6 * price;
			break;
		case "12 months":
			cost = 30 * 12 * price;
			break;
		default:
			break;
		}
		// 20% disccount
		cost -= 0.2 * cost;
		this.amountToPay = cost;
		totalCostLabel.setText("Total cost: " + cost + " lei");

	}

	@SuppressWarnings("serial")
	private void initComponents() {

		subscriptionPanel = new javax.swing.JTabbedPane();
        selectSubscriptionPanel = new javax.swing.JPanel();
        timeComboBox = new javax.swing.JComboBox<>();
        timeLabel = new javax.swing.JLabel();
        bankAccountComboBox = new javax.swing.JComboBox<>();
        bankAccountLabel = new javax.swing.JLabel();
        payButton = new javax.swing.JButton();
        paymentLabel = new javax.swing.JLabel();
        totalCostLabel = new javax.swing.JLabel();
        userSubscriptionsPanel = new javax.swing.JScrollPane();
        subscriptionsTab = new javax.swing.JScrollPane();
        subscriptionsTable = new javax.swing.JTable();

        timeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 month", "6 months", "12 months" }));

        timeLabel.setText("Time period:");

        bankAccountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bank account 1", "Bank account 2", "Bank account 3", "Bank account 4" }));

        bankAccountLabel.setText("Bank account:");

        payButton.setText("Pay");
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        paymentLabel.setForeground(new java.awt.Color(0, 180, 0));
        paymentLabel.setText("Payment succeded. Switch to the next tab.");

        totalCostLabel.setText("Total cost:");

        javax.swing.GroupLayout selectSubscriptionPanelLayout = new javax.swing.GroupLayout(selectSubscriptionPanel);
        selectSubscriptionPanel.setLayout(selectSubscriptionPanelLayout);
        selectSubscriptionPanelLayout.setHorizontalGroup(
            selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalCostLabel)
                    .addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
                        .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(payButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(timeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bankAccountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(paymentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(bankAccountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        selectSubscriptionPanelLayout.setVerticalGroup(
            selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankAccountLabel)
                    .addComponent(bankAccountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(totalCostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addGroup(selectSubscriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(payButton)
                    .addComponent(paymentLabel))
                .addGap(30, 30, 30))
        );

        subscriptionPanel.addTab("Select subscription", selectSubscriptionPanel);

        subscriptionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "User Id", "Lot no", "Expiring date", "Update", "Delete"
            }
        ) {
            @SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subscriptionsTable.setColumnSelectionAllowed(true);
        subscriptionsTable.getTableHeader().setReorderingAllowed(false);
        subscriptionsTab.setViewportView(subscriptionsTable);
        subscriptionsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        userSubscriptionsPanel.setViewportView(subscriptionsTab);

        subscriptionPanel.addTab("Subscriptions", userSubscriptionsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(subscriptionPanel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );
	}

	private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String cardNumber = (String) bankAccountComboBox.getSelectedItem();
		ParkingLot parkingLot = parkingSpaceDAO.getFirstAvailable();
		if (parkingLot == null) {
			JOptionPane.showMessageDialog(null, "Sorry! There is no parking lot available. Try again later!");
			return;
		}
		String timePeriod = (String) timeComboBox.getSelectedItem();
		int months = 0;
		switch (timePeriod) {
		case "1 month":
			months = 1;
			break;
		case "6 months":
			months = 6;
			break;
		case "12 months":
			months = 12;
			break;
		default:
			break;
		}
		Date expiringDate = subscriptionDAO.getExpiringDate(months);
		ParkingSubscription subscription = new ParkingSubscription(currentUser.getUserId(), parkingLot.getLotNo(), expiringDate);

		subscription = subscriptionDAO.subscribe(subscription, cardNumber, amountToPay);
		if (subscription != null) {
			paymentLabel.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Sorry, you don't have enough money...");
		}
	}

}