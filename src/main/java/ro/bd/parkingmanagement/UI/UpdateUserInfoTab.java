package ro.bd.parkingmanagement.UI;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import ro.bd.parkingmanagement.DAO.BankAccountDAO;
import ro.bd.parkingmanagement.DAO.BankAccountDAOImpl;
import ro.bd.parkingmanagement.DAO.UserDAO;
import ro.bd.parkingmanagement.DAO.UserDAOImpl;
import ro.bd.parkingmanagement.model.User;

public class UpdateUserInfoTab extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JScrollPane bankAccountsInfoPanel;
	private javax.swing.JTable bankAccountsTable;
	private javax.swing.JButton changePasswordButton;
	private javax.swing.JPasswordField confirmPasswordField;
	private javax.swing.JLabel confirmPasswordLabel;
	private javax.swing.JTextField emailField;
	private javax.swing.JLabel emailLabel;
	private javax.swing.JPasswordField newPasswordField;
	private javax.swing.JLabel newPasswordLabel;
	private javax.swing.JPasswordField oldPasswordField;
	private javax.swing.JLabel oldPasswordLabel;
	private javax.swing.JButton saveButton;
	private javax.swing.JTabbedPane updateUserInfoPanel;
	private javax.swing.JPanel userInfoPanel;
	private javax.swing.JTextField usernameField;
	private javax.swing.JLabel usernameLabel;
	private javax.swing.JComboBox<String> vehicleTypeComboBox;
	private javax.swing.JLabel vehicleTypeLabel;
	private javax.swing.JButton addNewBankAccount;
	private static BankAccountDAO bankAccountDAO = new BankAccountDAOImpl();
	private static UserDAO userDAO = new UserDAOImpl();
	private boolean passwordVisibility = false;
	private User currentUser = null;

	private void displayCurrentUserInfo() {
		if (currentUser != null) {
			usernameField.setText(currentUser.getUsername());
			emailField.setText(currentUser.getEmail());
			oldPasswordField.setText(currentUser.getPassword());
		}
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
		displayCurrentUserInfo();
		if (currentUser != null)
			bankAccountsTable = bankAccountDAO.populateJTableFromDB(currentUser.getUserId());
		else
			bankAccountsTable = null;
		bankAccountsInfoPanel.setViewportView(bankAccountsTable);
	}

	/**
	 * Creates new form UpdateUserInfo
	 */
	public UpdateUserInfoTab() {
		initComponents();
		updateUserInfoPanel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int tab = updateUserInfoPanel.getSelectedIndex();
				if (tab == 0) {
					addNewBankAccount.setVisible(false);
					displayCurrentUserInfo();
				} else {
					if (currentUser != null)
						bankAccountsTable = bankAccountDAO.populateJTableFromDB(currentUser.getUserId());
					else
						bankAccountsTable = null;
					bankAccountsInfoPanel.setViewportView(bankAccountsTable);
					addNewBankAccount.setVisible(true);
				}
			}
		});
	}

	public void refreshTab() {
		int tab = updateUserInfoPanel.getSelectedIndex();
		if (tab == 1) {
			if (currentUser != null)
				bankAccountsTable = bankAccountDAO.populateJTableFromDB(currentUser.getUserId());
			else
				bankAccountsTable = null;
			bankAccountsInfoPanel.setViewportView(bankAccountsTable);
		}
	}

	private void setPasswordVisibility(boolean flag) {
		passwordVisibility = flag;
		oldPasswordField.setVisible(flag);
		oldPasswordLabel.setVisible(flag);
		newPasswordField.setVisible(flag);
		newPasswordLabel.setVisible(flag);
		confirmPasswordField.setVisible(flag);
		confirmPasswordLabel.setVisible(flag);
	}

	private void initComponents() {

		updateUserInfoPanel = new javax.swing.JTabbedPane();
		userInfoPanel = new javax.swing.JPanel();
		usernameField = new javax.swing.JTextField();
		usernameLabel = new javax.swing.JLabel();
		emailLabel = new javax.swing.JLabel();
		emailField = new javax.swing.JTextField();
		newPasswordField = new javax.swing.JPasswordField();
		newPasswordLabel = new javax.swing.JLabel();
		confirmPasswordLabel = new javax.swing.JLabel();
		confirmPasswordField = new javax.swing.JPasswordField();
		vehicleTypeComboBox = new javax.swing.JComboBox<>();
		vehicleTypeLabel = new javax.swing.JLabel();
		changePasswordButton = new javax.swing.JButton();
		oldPasswordLabel = new javax.swing.JLabel();
		oldPasswordField = new javax.swing.JPasswordField();
		bankAccountsInfoPanel = new javax.swing.JScrollPane();
		bankAccountsTable = null;
		saveButton = new javax.swing.JButton();
		addNewBankAccount = new javax.swing.JButton();
		addNewBankAccount.setVisible(false);

		setPasswordVisibility(false);

		usernameLabel.setText("Username:");

		emailLabel.setText("Email:");

		newPasswordLabel.setText("New password:");

		confirmPasswordLabel.setText("Confirm password:");

		vehicleTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "motorcycle", "car", "van", "truck", "trailer" }));

		vehicleTypeLabel.setText("Vehicle type:");

		changePasswordButton.setText("Change password");
		changePasswordButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changePasswordButtonActionPerformed(evt);
			}
		});

		oldPasswordLabel.setText("Old password:");

		saveButton.setText("Save");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout userInfoPanelLayout = new javax.swing.GroupLayout(userInfoPanel);
		userInfoPanel.setLayout(userInfoPanelLayout);
		userInfoPanelLayout.setHorizontalGroup(userInfoPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(userInfoPanelLayout.createSequentialGroup().addGroup(userInfoPanelLayout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(userInfoPanelLayout.createSequentialGroup().addGap(88, 88, 88)
								.addComponent(changePasswordButton))
						.addGroup(userInfoPanelLayout.createSequentialGroup().addGap(20, 20, 20)
								.addGroup(userInfoPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(userInfoPanelLayout.createSequentialGroup()
												.addGroup(userInfoPanelLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(vehicleTypeLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE, 150,
																Short.MAX_VALUE)
														.addComponent(emailLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(usernameLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(userInfoPanelLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(emailField,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(vehicleTypeComboBox,
																javax.swing.GroupLayout.Alignment.LEADING, 0, 200,
																Short.MAX_VALUE)
														.addComponent(usernameField)))
										.addGroup(userInfoPanelLayout.createSequentialGroup()
												.addGroup(userInfoPanelLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(confirmPasswordLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(oldPasswordLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(newPasswordLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(userInfoPanelLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(newPasswordField,
																javax.swing.GroupLayout.DEFAULT_SIZE, 200,
																Short.MAX_VALUE)
														.addComponent(oldPasswordField)
														.addComponent(confirmPasswordField)))
										.addComponent(saveButton))))
						.addContainerGap(355, Short.MAX_VALUE)));
		userInfoPanelLayout.setVerticalGroup(userInfoPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(userInfoPanelLayout.createSequentialGroup().addGap(21, 21, 21)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(usernameLabel).addComponent(usernameField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(emailLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(vehicleTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(vehicleTypeComboBox))
						.addGap(14, 14, 14).addComponent(changePasswordButton)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(oldPasswordLabel).addComponent(oldPasswordField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(newPasswordLabel).addComponent(newPasswordField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(confirmPasswordLabel).addComponent(confirmPasswordField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
						.addComponent(saveButton).addContainerGap()));

		updateUserInfoPanel.addTab("User", userInfoPanel);

		bankAccountsInfoPanel.setViewportView(bankAccountsTable);

		updateUserInfoPanel.addTab("Bank accounts", bankAccountsInfoPanel);

		addNewBankAccount.setText("Add bank account");
		addNewBankAccount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addNewBankAccountActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(updateUserInfoPanel)
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(33, 33, 33).addComponent(addNewBankAccount)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(updateUserInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(addNewBankAccount)));
	}// </editor-fold>

	private boolean isUserInfoValid(User user) {
		boolean usernameInDB = userDAO.isUsernameInDB(user.getUsername());
		boolean emailInDB = userDAO.isEmailInDB(user.getEmail());
		boolean sameUsername = currentUser.getUsername().equals(user.getUsername());
		boolean sameEmail = currentUser.getEmail().equals(user.getEmail());
		if (usernameInDB && !sameUsername) {
			JOptionPane.showMessageDialog(null, "Username already used");
			return false;
		}
		if (emailInDB && !sameEmail) {
			JOptionPane.showMessageDialog(null, "Email already used");
			return false;
		}
		return true;
	}

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
		User user = new User();
		String oldPassword, newPassword, confirmPassword;
		user.setUserId(currentUser.getUserId());
		user.setUsername(usernameField.getText());
		user.setEmail(emailField.getText());
		if (passwordVisibility == true) {
			oldPassword = String.valueOf(oldPasswordField.getPassword());
			newPassword = String.valueOf(newPasswordField.getPassword());
			confirmPassword = String.valueOf(confirmPasswordField.getPassword());
			if (!oldPassword.equals(currentUser.getPassword())) {
				JOptionPane.showMessageDialog(null, "Wrong old password");
				return;
			}
			if (!newPassword.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(null, "Passwords don't match");
				return;
			}
			if (newPassword.length() < 6) {
				JOptionPane.showMessageDialog(null, "Password too short (min, 6 char)");
				return;
			}
			user.setPassword(newPassword);
		} else
			user.setPassword(currentUser.getPassword());

		if (isUserInfoValid(user)) {
			user = userDAO.updateUser(currentUser.getUserId(), user);
			if (user == null) {
				return;
			}
			oldPasswordField.setText(user.getPassword());
			currentUser = user;
			newPasswordField.setText("");
			confirmPasswordField.setText("");
		}
	}

	private void changePasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (passwordVisibility == true) {
			setPasswordVisibility(false);
			passwordVisibility = false;
		} else {
			setPasswordVisibility(true);
			passwordVisibility = true;
		}
	}

	private void addNewBankAccountActionPerformed(java.awt.event.ActionEvent evt) {
		int index = updateUserInfoPanel.getSelectedIndex();
		DefaultTableModel model;
		if (index == 1) {
			model = (DefaultTableModel) bankAccountsTable.getModel();
			model.addRow(new Object[] { null, null, null, null, null, currentUser.getUserId() });
			for (int i = 0; i < bankAccountsTable.getRowCount(); i++) {
				model.setValueAt("Update", i, bankAccountsTable.getColumnCount() - 2);
				model.setValueAt("Delete", i, bankAccountsTable.getColumnCount() - 1);
			}
		}
	}

}