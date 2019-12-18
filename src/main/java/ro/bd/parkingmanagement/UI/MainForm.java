package ro.bd.parkingmanagement.UI;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ro.bd.parkingmanagement.model.User;

public class MainForm extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private LoginForm loginForm;
	private javax.swing.JTabbedPane workspacePanel;
	
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu userMenu;
	private javax.swing.JMenuItem loginMenuItem;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JMenuItem registerMenuItem;

	private SignUpForm signUpForm;
	private DisplayPanelTab displayPanelTab;
	private TicketTab ticketTab;
	private PaymentTab paymentTab;
	private UpdateUserInfoTab updateUserInfoTab;
	private SubscribeTab subscribeTab;
	private AdminTab adminTab;
	private javax.swing.JScrollPane updateTab;

	public MainForm() {
		initComponents();
		workspacePanel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int tab = workspacePanel.getSelectedIndex();
				switch (tab) {
				case 0:
					displayPanelTab.updateDisplayPanel();
					break;
				case 1:
					ticketTab.clearTicketDetails();
					break;
				case 2:
					paymentTab.clearPaymentTab();
					break;
				case 3:
					updateUserInfoTab.refreshTab();
					break;
				default:
					break;
				}
			}
		});
	}

	private void initComponents() {
		loginForm = new LoginForm();
		loginForm.addObserver(this);
		signUpForm = new SignUpForm();
		signUpForm.setVisible(false);

		workspacePanel = new javax.swing.JTabbedPane();
        updateTab = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        userMenu = new javax.swing.JMenu();
        loginMenuItem = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();
        registerMenuItem = new javax.swing.JMenuItem();

		displayPanelTab = new DisplayPanelTab();
		ticketTab = new TicketTab();
		paymentTab = new PaymentTab();
		updateUserInfoTab = new UpdateUserInfoTab();
		subscribeTab = new SubscribeTab();
		adminTab = new AdminTab();

		this.setTitle("Parking System");
		loginForm.setVisible(false);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(760, 360));
        setPreferredSize(new java.awt.Dimension(800, 500));

        workspacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        workspacePanel.addTab("Display panel", displayPanelTab);
        workspacePanel.addTab("Ticket", ticketTab);
        workspacePanel.addTab("Payment", paymentTab);
        workspacePanel.addTab("Update", updateTab);
        workspacePanel.addTab("Subscribe", subscribeTab);
        workspacePanel.addTab("Administrator", adminTab);
        
		workspacePanel.setEnabledAt(3, false);
		workspacePanel.setEnabledAt(4, false);
		workspacePanel.setEnabledAt(5, false);

        userMenu.setText("User");

        loginMenuItem.setText("Login");
        loginMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loginMenuItemMouseClicked();
			}
        	
        });
//        loginMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                loginMenuItemMouseClicked(evt);
//            }
//        });
        userMenu.add(loginMenuItem);

        logoutMenuItem.setText("Logout");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				logoutMenuItemMouseClicked();
			}
        	
        });

//        logoutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                logoutMenuItemMouseClicked(evt);
//            }
//        });
        userMenu.add(logoutMenuItem);

        registerMenuItem.setText("Register");
        registerMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registerMenuItemMouseClicked();
			}
        	
        });
//        registerMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                registerMenuItemMouseClicked(evt);
//            }
//        });
        userMenu.add(registerMenuItem);

        menuBar.add(userMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(workspacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(workspacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();

		
	}// </editor-fold>

	@Override
	public void update(Observable arg0, Object arg1) {
		User user = (User) arg1;
		if (user != null) {
			updateUserInfoTab.setCurrentUser(user);
			subscribeTab.setCurrentUser(user);

			workspacePanel.setEnabledAt(3, true);
			workspacePanel.setEnabledAt(4, true);
			subscribeTab.cleanSubscribeTabOnUserLogin();
			if (user.isAdmin()) {
				workspacePanel.setEnabledAt(5, true);
			}
			
		}
	}

	/*private void displayPanelControlActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(0);
	}

	private void ticketControlActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(1);
	}

	private void paymentControlActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(2);
	}

	private void updateControlActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(3);
	}

	private void subscribeControlActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(4);
	}

	private void adminButtonActionPerformed(java.awt.event.ActionEvent evt) {
		workspacePanel.setSelectedIndex(5);
	}*/

	private void loginMenuItemMouseClicked() {
		Point currentLocation = this.getLocationOnScreen();
		Dimension loginWindowDimension = loginForm.getLoginWindowSize();
		currentLocation.x += this.getWidth() / 2 - loginWindowDimension.getWidth() / 2;
		currentLocation.y += this.getHeight() / 2 - loginWindowDimension.getHeight() / 2;
		loginForm.setPosition(currentLocation);
		loginForm.setErrorLabelVisibility(false);
		loginForm.setVisible(true);
    }

    private void logoutMenuItemMouseClicked() {
    	updateUserInfoTab.setCurrentUser(null);

		workspacePanel.setEnabledAt(3, false);
		workspacePanel.setEnabledAt(4, false);
		workspacePanel.setEnabledAt(5, false);
		/**
		 * ensure that the unlogged user doesn't access other pages
		 */
		workspacePanel.setSelectedIndex(0);
    }

    private void registerMenuItemMouseClicked() {
    	Point currentLocation = this.getLocationOnScreen();
		Dimension loginWindowDimension = signUpForm.getSize();
		currentLocation.x += this.getWidth() / 2 - loginWindowDimension.getWidth() / 2;
		currentLocation.y += this.getHeight() / 2 - loginWindowDimension.getHeight() / 2;
		signUpForm.setLocation(currentLocation);
		signUpForm.setVisible(true);
    }
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		// SET NIMBUS STYLE
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				// System.out.println(info.getName());

				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainForm().setVisible(true);
			}
		});
	}

}