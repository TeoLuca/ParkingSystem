package ro.bd.parkingmanagement.UI;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Observable;

import javax.swing.JFrame;

import ro.bd.parkingmanagement.DAO.UserDAO;
import ro.bd.parkingmanagement.DAO.UserDAOImpl;
import ro.bd.parkingmanagement.model.User;

public class LoginForm extends Observable {

	private static UserDAO userDAO = new UserDAOImpl();

	/**
	 * Creates new form LoginForm
	 */
	public LoginForm() {
		initComponents();
	}

	private void initComponents() {
		frame = new JFrame();
		frame.setVisible(false);
		frame.setTitle("Log In");
		loginButton = new javax.swing.JButton();
		passwordField = new javax.swing.JPasswordField();
		usernameField = new javax.swing.JTextField();
		usernameLabel = new javax.swing.JLabel();
		passwordLabel = new javax.swing.JLabel();
		errorLabel = new javax.swing.JLabel();
		errorLabel.setVisible(false);

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

		loginButton.setText("Log In");
		loginButton.setActionCommand("loginButton");
		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginButtonActionPerformed(evt);
			}
		});

		usernameLabel.setText("Username");

		passwordLabel.setText("Password");

		errorLabel.setForeground(new java.awt.Color(255, 0, 0));
		errorLabel.setText("Invalid username or password");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLabel)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(errorLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(loginButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel)
                .addContainerGap(16, Short.MAX_VALUE))
        );
		frame.pack();
		frame.setVisible(true);
	}

	private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String username = usernameField.getText();
		String password = String.valueOf(passwordField.getPassword());
		User user = userDAO.checkCredentials(username, password);
		if (user != null) {
			//TODO: administrator selection
			//boolean admin = userDAO.isAdmin(username, password);
			//user.setAdmin(admin);
			frame.setVisible(false);
			setChanged();
			notifyObservers(user);
		} else {
			errorLabel.setVisible(true);
		}
		usernameField.setText("");
		passwordField.setText("");
	}

	public void setErrorLabelVisibility(boolean visibility) {
		this.errorLabel.setVisible(visibility);
	}

	public void setVisible(boolean flag) {
		frame.setVisible(flag);
	}

	public void setPosition(Point location) {
		frame.setLocation(location);
	}

	public Dimension getLoginWindowSize() {
		return frame.getSize();
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginForm();
			}
		});
	}
	
	private JFrame frame;
	private javax.swing.JButton loginButton;
	private javax.swing.JLabel usernameLabel;
	private javax.swing.JLabel passwordLabel;
	private javax.swing.JLabel errorLabel;
	private javax.swing.JPasswordField passwordField;
	private javax.swing.JTextField usernameField;

}
