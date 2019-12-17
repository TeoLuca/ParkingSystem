package ro.bd.parkingmanagement.UI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
import ro.bd.parkingmanagement.model.Price;
import ro.bd.parkingmanagement.model.ParkingLot;
import ro.bd.parkingmanagement.model.ParkingSubscription;
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
	private javax.swing.JProgressBar paymentProgressBar;
	private javax.swing.JPanel selectSubscriptionPanel;
	private javax.swing.JTabbedPane subscriptionPanel;
	private javax.swing.JScrollPane subscriptionsTab;
	private javax.swing.JTable subscriptionsTable;
	private javax.swing.JComboBox<String> timeComboBox;
	private javax.swing.JLabel timeLabel;
	private javax.swing.JScrollPane userSubscriptionsPanel;
	private javax.swing.JComboBox<String> vehicleTypeComboBox;
	private javax.swing.JLabel vehicleTypeLabel;
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
				paymentProgressBar.setEnabled(false);
				paymentProgressBar.setVisible(false);
				paymentProgressBar.setValue(0);
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
		vehicleTypeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					displayAmountToPay();
				}
			}
		});

	}
	
	public void cleanSubscribeTabOnUserLogin() {
		paymentProgressBar.setEnabled(false);
		paymentProgressBar.setVisible(false);
		paymentProgressBar.setValue(0);
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

	private void initComponents() {

		subscriptionPanel = new javax.swing.JTabbedPane();
		selectSubscriptionPanel = new javax.swing.JPanel();
		vehicleTypeComboBox = new javax.swing.JComboBox<>();
		timeComboBox = new javax.swing.JComboBox<>();
		vehicleTypeLabel = new javax.swing.JLabel();
		timeLabel = new javax.swing.JLabel();
		bankAccountComboBox = new javax.swing.JComboBox<>();
		bankAccountLabel = new javax.swing.JLabel();

		paymentProgressBar = new javax.swing.JProgressBar();
		paymentProgressBar.setValue(0);
		paymentProgressBar.setStringPainted(true);

		payButton = new javax.swing.JButton();
		paymentLabel = new javax.swing.JLabel();
		totalCostLabel = new javax.swing.JLabel();
		totalCostLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
		userSubscriptionsPanel = new javax.swing.JScrollPane();
		subscriptionsTab = new javax.swing.JScrollPane();

		if (currentUser != null)
			subscriptionsTable = subscriptionDAO.populateJTableFromDB(currentUser.getUserId());
		else
			subscriptionsTable = null;

		paymentProgressBar.setVisible(false);
		paymentLabel.setVisible(false);

		vehicleTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "motorcycle", "car", "van", "truck", "trailer" }));

		timeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "1 month", "2 months", "3 months", "6 months", "12 months" }));

		vehicleTypeLabel.setText("Vehicle type:");

		timeLabel.setText("Time period:");

		String[] cardNumberArray;
		if (currentUser != null)
			cardNumberArray = bankAccountDAO.getAllCardNumbers(currentUser.getUserId());
		else
			cardNumberArray = new String[1];
		bankAccountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(cardNumberArray));

		bankAccountLabel.setText("Bank account:");

		payButton.setText("Pay");
		payButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				payButtonActionPerformed(evt);
			}
		});

		paymentLabel.setForeground(new java.awt.Color(0, 180, 0));
		paymentLabel.setText("Payment succeded. You can see your subscriptions in the next tab.");

		totalCostLabel.setText("Total cost:");

		javax.swing.GroupLayout selectSubscriptionPanelLayout = new javax.swing.GroupLayout(selectSubscriptionPanel);
		selectSubscriptionPanel.setLayout(selectSubscriptionPanelLayout);
		selectSubscriptionPanelLayout.setHorizontalGroup(selectSubscriptionPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(selectSubscriptionPanelLayout.createSequentialGroup().addGap(20, 20, 20)
						.addGroup(selectSubscriptionPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(totalCostLabel)
								.addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
										.addGroup(selectSubscriptionPanelLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(vehicleTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
														120, Short.MAX_VALUE)
												.addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 120,
														Short.MAX_VALUE)
												.addComponent(payButton, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(selectSubscriptionPanelLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
														.addGap(27, 27, 27)
														.addGroup(selectSubscriptionPanelLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(timeComboBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 170,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(vehicleTypeComboBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 170,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(bankAccountComboBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 170,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addGroup(selectSubscriptionPanelLayout.createSequentialGroup()
														.addGap(28, 28, 28).addComponent(paymentLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 485,
																javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addGroup(selectSubscriptionPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(paymentProgressBar, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
										.addComponent(bankAccountLabel, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
						.addContainerGap(156, Short.MAX_VALUE)));
		selectSubscriptionPanelLayout.setVerticalGroup(selectSubscriptionPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(selectSubscriptionPanelLayout.createSequentialGroup().addGap(16, 16, 16)
						.addGroup(selectSubscriptionPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(vehicleTypeLabel)
								.addComponent(vehicleTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(selectSubscriptionPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(timeLabel)
								.addComponent(timeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(selectSubscriptionPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(bankAccountLabel).addComponent(bankAccountComboBox,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18).addComponent(totalCostLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
						.addComponent(paymentProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(selectSubscriptionPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(payButton)
								.addComponent(paymentLabel))
						.addGap(30, 30, 30)));

		subscriptionPanel.addTab("Select subscription", selectSubscriptionPanel);

		subscriptionsTab.setViewportView(subscriptionsTable);

		userSubscriptionsPanel.setViewportView(subscriptionsTab);

		subscriptionPanel.addTab("Subscriptions", userSubscriptionsPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addGap(21, 21, 21).addComponent(subscriptionPanel).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(subscriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
						.addContainerGap()));
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
			paymentProgressBar.setVisible(true);
			paymentProgressBar.setEnabled(true);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i <= 100; i+=10) {
						paymentProgressBar.setValue(i);
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					paymentLabel.setVisible(true);
				}
			});
		}
	}

}