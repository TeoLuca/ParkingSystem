package ro.bd.parkingmanagement.UI;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ro.bd.parkingmanagement.DAO.PriceDAO;
import ro.bd.parkingmanagement.DAO.PriceDAOImpl;
import ro.bd.parkingmanagement.DAO.TicketDAO;
import ro.bd.parkingmanagement.DAO.TicketDAOImpl;
import ro.bd.parkingmanagement.model.Ticket;

public class PaymentTab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel bankAccountDetailsPanel;
	private javax.swing.JTextField cardNumberField;
	private javax.swing.JLabel cardNumberLabel;
	private javax.swing.JSpinner currentValueField;
	private javax.swing.JLabel currentValueLabel;
	private javax.swing.JButton computeFeeButton;
	private javax.swing.JPasswordField cvvCodeField;
	private javax.swing.JLabel cvvCodeLabel;
	private javax.swing.JLabel expiringDateLabel;
	private org.jdesktop.swingx.JXDatePicker expiringDatePicker;
	private javax.swing.JLabel feeLabel;
	private javax.swing.JPanel insertTicketCodePanel;
	private javax.swing.JButton payButton;
	private javax.swing.JProgressBar paymentProgressBar;
	private javax.swing.JLabel successLabel;
	private javax.swing.JTextField ticketCodeField;
	private javax.swing.JLabel ticketCodeLabel;
	private static PriceDAO priceDAO = new PriceDAOImpl();
	private static TicketDAO ticketDAO = new TicketDAOImpl();
	private double currentFee = 0;

	public PaymentTab() {
		initComponents();
	}

	private void initComponents() {

		bankAccountDetailsPanel = new javax.swing.JPanel();
		cardNumberLabel = new javax.swing.JLabel();
		expiringDateLabel = new javax.swing.JLabel();
		cvvCodeLabel = new javax.swing.JLabel();
		cardNumberField = new javax.swing.JTextField();
		expiringDatePicker = new org.jdesktop.swingx.JXDatePicker();
		cvvCodeField = new javax.swing.JPasswordField();
		currentValueLabel = new javax.swing.JLabel();
		currentValueField = new javax.swing.JSpinner();
		insertTicketCodePanel = new javax.swing.JPanel();
		ticketCodeField = new javax.swing.JTextField();
		ticketCodeLabel = new javax.swing.JLabel();
		computeFeeButton = new javax.swing.JButton();
		feeLabel = new javax.swing.JLabel();
		payButton = new javax.swing.JButton();
		paymentProgressBar = new javax.swing.JProgressBar();
		successLabel = new javax.swing.JLabel();

		// hide:
		successLabel.setVisible(false);
		feeLabel.setVisible(false);
		paymentProgressBar.setVisible(false);
		payButton.setVisible(false);

		bankAccountDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bank account details",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 12))); // NOI18N
		bankAccountDetailsPanel.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N

		cardNumberLabel.setText("Card number:");

		expiringDateLabel.setText("Expiring date:");

		cvvCodeLabel.setText("CVV code:");

		currentValueLabel.setText("Current value:");

		currentValueField.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));

		javax.swing.GroupLayout bankAccountDetailsPanelLayout = new javax.swing.GroupLayout(bankAccountDetailsPanel);
		bankAccountDetailsPanel.setLayout(bankAccountDetailsPanelLayout);
		bankAccountDetailsPanelLayout.setHorizontalGroup(bankAccountDetailsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(bankAccountDetailsPanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(bankAccountDetailsPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(cvvCodeLabel, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(currentValueLabel, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(expiringDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(cardNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(21, 21, 21)
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(cardNumberField)
								.addComponent(expiringDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
										Short.MAX_VALUE)
								.addComponent(cvvCodeField, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
								.addComponent(currentValueField))
						.addContainerGap(20, Short.MAX_VALUE)));
		bankAccountDetailsPanelLayout.setVerticalGroup(bankAccountDetailsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(bankAccountDetailsPanelLayout.createSequentialGroup().addGap(18, 18, 18)
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(cardNumberLabel).addComponent(cardNumberField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(expiringDateLabel)
								.addComponent(expiringDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(12, 12, 12)
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(cvvCodeLabel).addComponent(cvvCodeField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(bankAccountDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(currentValueLabel).addComponent(currentValueField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))));

		insertTicketCodePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Insert ticket code",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 12))); // NOI18N

		ticketCodeField.setToolTipText("ticket code");

		ticketCodeLabel.setText("Ticket code:");

		computeFeeButton.setText("Compute fee");
		computeFeeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				computeFeeButtonActionPerformed(evt);
			}
		});

		feeLabel.setText("Fee:");

		javax.swing.GroupLayout insertTicketCodePanelLayout = new javax.swing.GroupLayout(insertTicketCodePanel);
		insertTicketCodePanel.setLayout(insertTicketCodePanelLayout);
		insertTicketCodePanelLayout.setHorizontalGroup(insertTicketCodePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(insertTicketCodePanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(insertTicketCodePanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										insertTicketCodePanelLayout.createSequentialGroup()
												.addComponent(ticketCodeLabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21,
														Short.MAX_VALUE)
												.addComponent(ticketCodeField, javax.swing.GroupLayout.PREFERRED_SIZE,
														160, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(insertTicketCodePanelLayout.createSequentialGroup()
										.addGroup(insertTicketCodePanelLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(computeFeeButton).addComponent(feeLabel))
										.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		insertTicketCodePanelLayout.setVerticalGroup(insertTicketCodePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(insertTicketCodePanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(insertTicketCodePanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(ticketCodeField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(ticketCodeLabel))
						.addGap(18, 18, 18).addComponent(computeFeeButton).addGap(18, 18, 18).addComponent(feeLabel)
						.addContainerGap(28, Short.MAX_VALUE)));

		payButton.setText("Pay");
		payButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				payButtonActionPerformed(evt);
			}
		});

		successLabel.setForeground(new java.awt.Color(0, 170, 0));
		successLabel.setText("Payment succeded! Drive safe!");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(20, 20, 20)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(insertTicketCodePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(31, 31, 31).addComponent(bankAccountDetailsPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(paymentProgressBar, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
										.addComponent(payButton, javax.swing.GroupLayout.Alignment.LEADING,
												javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
								.addGap(48, 48, 48).addComponent(successLabel)))
				.addContainerGap(23, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(21, 21, 21)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(insertTicketCodePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(bankAccountDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(paymentProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(payButton).addComponent(successLabel))
						.addContainerGap(81, Short.MAX_VALUE)));

	}
	/**
	 * @param ticketCode
	 *            - is used to extract the entry time
	 * @return a Calendar containing the entry time
	 */
	private Calendar getEntryTime(String ticketCode) {
		// ticketCode = time in milliseconds
		long timeInMilliseconds = Long.parseLong(ticketCode);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(timeInMilliseconds);
		return calendar;
	}

	/**
	 * @param entryTime
	 *            is used to compute the elapsed time in hours
	 */
	private int computeElapsedTime(Calendar entryTime) {
		/**
		 * adjustments for UTC + 3
		 */
		LocalDateTime startTime = LocalDateTime.ofInstant(entryTime.toInstant(), ZoneOffset.UTC)
				.plusHours(2);
		LocalDateTime endTime = LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneOffset.UTC)
				.plusHours(3);
		return (int) Duration.between(startTime, endTime).toHours();
	}

	private double computeFee(String ticketCode) {
		Calendar entryTime = getEntryTime(ticketCode);
		double price = priceDAO.getAll().get(0).getDayPrice();
		int elapsedHours = computeElapsedTime(entryTime);
		return price * elapsedHours;
	}

	/**
	 * @param ticketCode
	 *            - is used to check if the ticket code corresponds to a valid
	 *            ticket
	 * @return true if the ticketCode is valid, or false otherwise
	 */
	public boolean isTicketCodeValid(String ticketCode) {
		return ticketDAO.isValid(ticketCode);
	}

	private void computeFeeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String ticketCode = ticketCodeField.getText();
		if (isTicketCodeValid(ticketCode)) {
			double fee = computeFee(ticketCode);
			this.currentFee = fee;
			feeLabel.setText("Taxa: " + fee);
			feeLabel.setVisible(true);
			payButton.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Invalid ticket code");
		}
	}

	private boolean isBankAccountInfoLegit(String cardNumber, Date expiringDate, int cvvCode) {
		if (!cardNumber.matches("[0-9]+") || cardNumber.length() != 16)
			return false;
		if (cvvCode < 100 || cvvCode > 999)
			return false;
		if (expiringDate.before(new Date(new java.util.Date().getTime())))
			return false;
		return true;
	}

	public void clearPaymentTab() {
		ticketCodeField.setText("");
		cardNumberField.setText("");
		expiringDatePicker.setDate(null);
		cvvCodeField.setText("");
		feeLabel.setVisible(false);
		paymentProgressBar.setEnabled(false);
		paymentProgressBar.setValue(0);
		paymentProgressBar.setVisible(false);
		successLabel.setVisible(false);
		payButton.setVisible(false);
		currentValueField.setValue(0);
	}

	private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Update parkingSpace state to available
		String ticketCode = ticketCodeField.getText();
		Ticket ticket = ticketDAO.getTicketByCode(ticketCode);

		String cardNumber = cardNumberField.getText();
		Date expiringDate = expiringDatePicker.getDate();
		String cvv = String.valueOf(cvvCodeField.getPassword());
		double accountBalance = 0.0;
		if (currentValueField.getValue() instanceof Integer)
			accountBalance = (Integer) currentValueField.getValue();
		else
			accountBalance = (Double) currentValueField.getValue();

		// if (currentValue < currentFee) {
		// JOptionPane.showMessageDialog(null, "Not enough money.");
		// return;
		// }

		if (!cvv.matches("[0-9]+") || cvv.length() != 3) {
			JOptionPane.showMessageDialog(null, "invalid cvv code");
			return;
		}
		int cvvCode = Integer.parseInt(cvv);

		if (!isBankAccountInfoLegit(cardNumber, expiringDate, cvvCode)) {
			JOptionPane.showMessageDialog(null, "Invalid bank account info");
			return;
		}
		if (ticket == null) {
			JOptionPane.showMessageDialog(null, "Invalid ticket code");
		}
		if (ticketDAO.payTicket(ticketCode, cardNumber, accountBalance, currentFee)) {
			final double accountBalanceFinal = accountBalance;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					currentValueField.setValue(accountBalanceFinal - currentFee);
					successLabel.setVisible(true);
				}
			});
		}
	}

}
