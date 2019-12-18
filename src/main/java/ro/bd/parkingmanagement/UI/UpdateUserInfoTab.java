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
	private javax.swing.JTextField emailField;
	private javax.swing.JLabel emailLabel;
	private javax.swing.JButton saveButton;
	private javax.swing.JTabbedPane updateUserInfoPanel;
	private javax.swing.JPanel userInfoPanel;
	private javax.swing.JTextField usernameField;
	private javax.swing.JLabel usernameLabel;
	private javax.swing.JButton addNewBankAccount;
	private static BankAccountDAO bankAccountDAO = new BankAccountDAOImpl();
	private static UserDAO userDAO = new UserDAOImpl();
	private User currentUser = null;

	private void displayCurrentUserInfo() {
		if (currentUser != null) {
			usernameField.setText(currentUser.getUsername());
			emailField.setText(currentUser.getEmail());
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

	@SuppressWarnings("serial")
	private void initComponents() {

		updateUserInfoPanel = new javax.swing.JTabbedPane();
        userInfoPanel = new javax.swing.JPanel();
        usernameField = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        addNewBankAccount = new javax.swing.JButton();
        bankAccountsInfoPanel = new javax.swing.JScrollPane();
        bankAccountsTable = new javax.swing.JTable();

        usernameLabel.setText("Username:");

        emailLabel.setText("Email:");

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        addNewBankAccount.setText("Add bank account");
        addNewBankAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewBankAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userInfoPanelLayout = new javax.swing.GroupLayout(userInfoPanel);
        userInfoPanel.setLayout(userInfoPanelLayout);
        userInfoPanelLayout.setHorizontalGroup(
            userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userInfoPanelLayout.createSequentialGroup()
                .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userInfoPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(emailField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(usernameField)))
                    .addGroup(userInfoPanelLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addNewBankAccount)))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        userInfoPanelLayout.setVerticalGroup(
            userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userInfoPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel))
                .addGap(18, 18, 18)
                .addGroup(userInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(addNewBankAccount))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        updateUserInfoPanel.addTab("User", userInfoPanel);

        bankAccountsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BankAccountId", "UserId", "Card number", "Expiring date", "CVV code"
            }
        ) {
            @SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bankAccountsTable.setColumnSelectionAllowed(true);
        bankAccountsTable.getTableHeader().setReorderingAllowed(false);
        bankAccountsInfoPanel.setViewportView(bankAccountsTable);
        bankAccountsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        updateUserInfoPanel.addTab("Bank accounts", bankAccountsInfoPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateUserInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateUserInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
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
		user.setUserId(currentUser.getUserId());
		user.setUsername(usernameField.getText());
		user.setEmail(emailField.getText());

		if (isUserInfoValid(user)) {
			user = userDAO.updateUser(currentUser.getUserId(), user);
			if (user == null) {
				return;
			}
			currentUser = user;
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