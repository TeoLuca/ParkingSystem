package ro.bd.parkingmanagement.DAO;

import java.awt.event.ActionEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import ro.bd.parkingmanagement.UI.ButtonColumn;
import ro.bd.parkingmanagement.UI.JTableUtilities;
import ro.bd.parkingmanagement.model.User;

public class UserDAOImpl extends OracleDatabaseConnection implements UserDAO {

	private Action deleteUser = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object obj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id;
			if (obj != null)
				if (obj instanceof java.math.BigDecimal) {
					id = ((java.math.BigDecimal) obj).intValue();
				} else {
					id = (int) obj;
				}
			else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
			String username = String.valueOf(((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + username + "?",
					"Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (deleteUser(id) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private User checkTableEntry(JTable table, int modelRow, boolean update) {
		Object objUsername = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objPassword = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
		Object objEmail = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 3));
		String username, email, password;
		if (objUsername != null)
			username = (String) objUsername;
		else {
			JOptionPane.showMessageDialog(null, "Username is null");
			return null;
		}
		if (objPassword != null)
			password = (String) objPassword;
		else {
			JOptionPane.showMessageDialog(null, "Password is null");
			return null;
		}
		if (objEmail != null)
			email = (String) objEmail;
		else {
			JOptionPane.showMessageDialog(null, "Email is null");
			return null;
		}
		if (!update && isUsernameInDB(username)) {
			JOptionPane.showMessageDialog(null, "Username already in the database");
			return null;
		}
		if (!update && isEmailInDB(email)) {
			JOptionPane.showMessageDialog(null, "Email already in the database");
			return null;
		}
		if (password.length() <= 5) {
			JOptionPane.showMessageDialog(null, "Password too short (min, 6 char)");
			return null;
		}

		return new User(username, email, password);
	}

	private Action updateUser = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id = -1;
			if (objId != null) {
				// Update user
				User user = checkTableEntry(table, modelRow, true);
				if (user != null) {
					if (objId instanceof java.math.BigDecimal) {
						id = ((java.math.BigDecimal) objId).intValue();
					} else {
						id = (int) objId;
					}
					user.setUserId(id);
					updateUser(id, user);
				}
			} else {
				// Create user
				User user = checkTableEntry(table, modelRow, false);
				if (user != null) {
					user = insertUser(user);
					((DefaultTableModel) table.getModel()).setValueAt(user.getUserId(), modelRow, 0);
				}
			}
		}
	};

	@Override
	public synchronized JTable populateJTableFromDB() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT * FROM users";
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
						java.lang.String.class, java.lang.Object.class, java.lang.Object.class};
				boolean[] canEdit = new boolean[] { false, true, true, true, true, true, true };

				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			};
			table.setColumnSelectionAllowed(true);
			table.setMaximumSize(new java.awt.Dimension(2147483647, 128));
			table.getTableHeader().setReorderingAllowed(false);

			JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER);// centrare text
			new ButtonColumn(table, updateUser, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deleteUser, "Delete", table.getColumnCount() - 1);

			// hide id column
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);

			// JOptionPane.showMessageDialog(null, new JScrollPane(table));
			return table;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized User insertUser(User newUser) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "insert into users(username, email, password) values (?, ?, ?)";
			preparedStatement = connect.prepareStatement(sql, new String[] { "user_id" });
			/** Parameters start with 1 */
			preparedStatement.setString(1, newUser.getUsername());
			preparedStatement.setString(2, newUser.getEmail());
			preparedStatement.setString(3, newUser.getPassword());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				// throw new SQLException("Creating user failed, no rows affected.");
				return null;
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					newUser.setUserId(generatedKeys.getInt(1));
				} else {
					// throw new SQLException("Creating user failed, no ID obtained.");
					return null;
				}
			}
			return newUser;
		} catch (SQLException exception) {
			// exception.printStackTrace();
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized User getUser(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE user_id=?");
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setEmail(resultSet.getString(3));
				user.setPassword(resultSet.getString(4));
				return user;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized User updateUser(int id, User updatedUser) {
		if (updatedUser.getUserId() == id) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				preparedStatement = connect.prepareStatement(
						"UPDATE users SET username=?, email=?, password=? WHERE user_id= ?");
				preparedStatement.setString(1, updatedUser.getUsername());
				preparedStatement.setString(2, updatedUser.getEmail());
				preparedStatement.setString(3, updatedUser.getPassword());
				preparedStatement.setInt(4, id);
				preparedStatement.executeUpdate();
				return updatedUser;
			} catch (SQLException exception) {
				// exception.printStackTrace();
				showOracleSqlErrors(exception);
				return null;
			} finally {
				close();
			}
		}
		return null;
	}

	@Override
	public synchronized List<User> getAll() {
		try {
			List<User> userList = new ArrayList<User>();
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setEmail(resultSet.getString(4));
				user.setPassword(resultSet.getString(3));
				userList.add(user);
			}
			if (!userList.isEmpty())
				return userList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean deleteUser(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("DELETE FROM users WHERE user_id= ?");
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	@Override
	public synchronized User getUserByEmail(String email) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE email='" + email + "'");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setEmail(resultSet.getString(4));
				user.setPassword(resultSet.getString(3));
				return user;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean isAdmin(String username) {
		return username.equals("Teo");
	}

	@Override
	public synchronized User checkCredentials(String usernameOrEmail, String userPassword) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE (email='" + usernameOrEmail
					+ "' OR username='" + usernameOrEmail + "') AND password='" + userPassword + "'");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User res = new User();
				res.setUserId(resultSet.getInt(1));
				res.setUsername(resultSet.getString(2));
				res.setPassword(userPassword);
				res.setEmail(resultSet.getString(4));
				return res;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean isUsernameInDB(String username) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect
					.prepareStatement("SELECT COUNT(*) FROM users WHERE username='" + username + "'");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count > 0)
					return true;
				else
					return false;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	@Override
	public synchronized boolean isEmailInDB(String email) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT COUNT(*) FROM users WHERE email='" + email + "'");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count > 0)
					return true;
				else
					return false;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}
}
