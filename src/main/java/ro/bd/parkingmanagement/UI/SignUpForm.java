package ro.bd.parkingmanagement.UI;

import java.util.Date;

import ro.bd.parkingmanagement.DAO.BankAccountDAO;
import ro.bd.parkingmanagement.DAO.BankAccountDAOImpl;
import ro.bd.parkingmanagement.DAO.UserDAO;
import ro.bd.parkingmanagement.DAO.UserDAOImpl;
import ro.bd.parkingmanagement.model.BankAccount;
import ro.bd.parkingmanagement.model.User;

public class SignUpForm extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel bankAccountDetailsPanel;
	private javax.swing.JTextField cardNumberField;
	private javax.swing.JLabel cardNumberLabel;
	private javax.swing.JPasswordField confirmPasswordField;
	private javax.swing.JSpinner currentValueField;
	private javax.swing.JPasswordField cvvCodeField;
	private javax.swing.JLabel cvvCodeLabel;
	private javax.swing.JTextField emailField;
	private javax.swing.JLabel emailLabel;
	private javax.swing.JLabel errorLabel;
	private javax.swing.JLabel expiringDateLabel;
	private org.jdesktop.swingx.JXDatePicker expiringDatePicker;
	private javax.swing.JPasswordField passwordField;
	private javax.swing.JLabel passwordLabel;
	private javax.swing.JButton submitButton;
	private javax.swing.JPanel userDetailsPanel;
	private javax.swing.JTextField usernameField;
	private javax.swing.JLabel usernameLabel;
	private javax.swing.JComboBox<String> vehicleTypeComboBox;

	private static UserDAO userDAO = new UserDAOImpl();
	private static BankAccountDAO bankAccountDAO = new BankAccountDAOImpl();

	public static UserDAO getUserDAO() {
		return userDAO;
	}

	public static BankAccountDAO getBankAccountDAO() {
		return bankAccountDAO;
	}

	public SignUpForm() {
		initComponents();
	}

	private void initComponents() {
		this.setVisible(false);
		this.setTitle("Sign up");
		userDetailsPanel = new javax.swing.JPanel();
		usernameLabel = new javax.swing.JLabel();
		passwordLabel = new javax.swing.JLabel();
		emailLabel = new javax.swing.JLabel();
		passwordField = new javax.swing.JPasswordField();
		confirmPasswordField = new javax.swing.JPasswordField();
		usernameField = new javax.swing.JTextField();
		emailField = new javax.swing.JTextField();
		vehicleTypeComboBox = new javax.swing.JComboBox<>();
		bankAccountDetailsPanel = new javax.swing.JPanel();
		cardNumberLabel = new javax.swing.JLabel();
		expiringDateLabel = new javax.swing.JLabel();
		cvvCodeLabel = new javax.swing.JLabel();
		cardNumberField = new javax.swing.JTextField();
		expiringDatePicker = new org.jdesktop.swingx.JXDatePicker();
		cvvCodeField = new javax.swing.JPasswordField();
		currentValueField = new javax.swing.JSpinner();
		submitButton = new javax.swing.JButton();
		errorLabel = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

		userDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User details"));
		userDetailsPanel.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N

		usernameLabel.setText("Username:");

        passwordLabel.setText("Password:");

        emailLabel.setText("Email:");

        javax.swing.GroupLayout userDetailsPanelLayout = new javax.swing.GroupLayout(userDetailsPanel);
        userDetailsPanel.setLayout(userDetailsPanelLayout);
        userDetailsPanelLayout.setHorizontalGroup(
            userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userDetailsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(userDetailsPanelLayout.createSequentialGroup()
                        .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                            .addComponent(emailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(passwordField)))
                    .addGroup(userDetailsPanelLayout.createSequentialGroup()
                        .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        userDetailsPanelLayout.setVerticalGroup(
            userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userDetailsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bankAccountDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bank account"));
        bankAccountDetailsPanel.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N

        cardNumberLabel.setText("Card number:");

        expiringDateLabel.setText("Expiring date:");

        cvvCodeLabel.setText("CVV code:");

        javax.swing.GroupLayout bankAccountDetailsPanelLayout = new javax.swing.GroupLayout(bankAccountDetailsPanel);
        bankAccountDetailsPanel.setLayout(bankAccountDetailsPanelLayout);
        bankAccountDetailsPanelLayout.setHorizontalGroup(
            bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bankAccountDetailsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expiringDateLabel)
                    .addComponent(cvvCodeLabel)
                    .addComponent(cardNumberLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(cvvCodeField))
                .addContainerGap())
        );
        bankAccountDetailsPanelLayout.setVerticalGroup(
            bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bankAccountDetailsPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardNumberLabel)
                    .addComponent(cardNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(expiringDateLabel)
                .addGap(14, 14, 14)
                .addGroup(bankAccountDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cvvCodeLabel)
                    .addComponent(cvvCodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        errorLabel.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(userDetailsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bankAccountDetailsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(submitButton)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(userDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bankAccountDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submitButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
	}// </editor-fold>

	private boolean isBankAccountInfoLegit(String cardNumber, Date expiringDate, int cvvCode) {
		if (!cardNumber.matches("[0-9]+") || cardNumber.length() != 16)
			return false;
		if (cvvCode < 100 || cvvCode > 999)
			return false;
		if (expiringDate.before(new Date(new java.util.Date().getTime())))
			return false;
		return true;
	}

	private void clearForm() {
		usernameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		confirmPasswordField.setText("");
		vehicleTypeComboBox.setSelectedIndex(0);
		cardNumberField.setText("");
		expiringDatePicker.setDate(null);
		cvvCodeField.setText("");
		currentValueField.setValue(0);
	}

	private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String username = usernameField.getText();
		String email = emailField.getText();
		if (userDAO.isUsernameInDB(username)) {
			errorLabel.setText("Username already in the database");
			return;
		}
		if (userDAO.isEmailInDB(email)) {
			errorLabel.setText("Email already in the database");
			return;
		}
		String password;
		if (String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword())))
			password = String.valueOf(passwordField.getPassword());
		else {
			errorLabel.setText("Passwords do not match");
			return;
		}
		if (password.length() <= 5) {
			errorLabel.setText("Password too short (min. 6 char)");
			return;
		}
		User user = new User(username, email, password);

		String cardNumber = cardNumberField.getText();
		Date expiringDate = expiringDatePicker.getDate();
		String cvv = String.valueOf(cvvCodeField.getPassword());
		if (!cvv.matches("[0-9]+") || cvv.length() != 3) {
			errorLabel.setText("Invalid cvv code");
			return;
		}
		int cvvCode = Integer.parseInt(cvv);

		if (!isBankAccountInfoLegit(cardNumber, expiringDate, cvvCode)) {
			errorLabel.setText("Invalid bank account info");
			return;
		}
		if (bankAccountDAO.isCardNumberAlreadyUsed(cardNumber)) {
			errorLabel.setText("Card number already used");
			return;
		}
		int accountBalance = (int) currentValueField.getValue();
		user = userDAO.insertUser(user);
		if (user != null) {
			BankAccount bankAccount = new BankAccount(user.getUserId(), cardNumber,
					new java.sql.Date(expiringDate.getTime()), cvvCode, (double) accountBalance);
			bankAccountDAO.createBankAccount(bankAccount);
			errorLabel.setText("The account was successfully created");
			clearForm();
		}
	}

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SignUpForm().setVisible(true);
			}
		});
	}

}