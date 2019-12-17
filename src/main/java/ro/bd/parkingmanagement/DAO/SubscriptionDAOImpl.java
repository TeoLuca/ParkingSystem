package ro.bd.parkingmanagement.DAO;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import ro.bd.parkingmanagement.UI.ButtonColumn;
import ro.bd.parkingmanagement.UI.JTableUtilities;
import ro.bd.parkingmanagement.model.ParkingSubscription;
import ro.bd.parkingmanagement.model.User;

public class SubscriptionDAOImpl extends OracleDatabaseConnection implements SubscriptionDAO {

	private Action deleteSubscription = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object obj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id;
			if (obj != null) {
				if (obj instanceof java.math.BigDecimal) {
					id = ((java.math.BigDecimal) obj).intValue();
				} else
					id = (int) obj;
			}

			else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
			Object objUsername = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
			String username = (String) objUsername;
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the subscription for " + username + "?", "Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (deleteSubscription(id) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private Action updateSubscription = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id = -1;
			if (objId != null) {
				// Update subscription
				ParkingSubscription subscription = checkTableEntry(table, modelRow, true);
				if (subscription != null) {
					if (objId instanceof java.math.BigDecimal) {
						id = ((java.math.BigDecimal) objId).intValue();
					} else
						id = (int) objId;
					subscription.setSubscriptionId(id);
					updateSubscription(id, subscription);
				}
			} else {
				// Create subscription
				ParkingSubscription subscription = checkTableEntry(table, modelRow, false);
				if (subscription != null) {
					subscription = insertSubscription(subscription);
					((DefaultTableModel) table.getModel()).setValueAt(subscription.getSubscriptionId(), modelRow, 0);
				}
			}
		}
	};

	private ParkingSubscription checkTableEntry(JTable table, int modelRow, boolean update) {
		Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
		Object objUserId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objUsername = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
		Object objLotNo = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 3));
		Object objExpiringDate = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 4));
		String username;
		int lotNo;
		Date expiringDate;
		int userId;
		if (objUsername != null)
			username = (String) objUsername;
		else {
			JOptionPane.showMessageDialog(null, "Username is null");
			return null;
		}
		if (objUserId != null)
			if (objUserId instanceof java.math.BigDecimal) {
				userId = ((java.math.BigDecimal) objUserId).intValue();
			} else
				userId = (int) objUserId;
		else {

			User user = getUserByUsername(username);
			if (user == null) {
				JOptionPane.showMessageDialog(null, "Username not found");
				return null;
			}
			userId = user.getUserId();
			table.getModel().setValueAt(userId, modelRow, 1);

			// JOptionPane.showMessageDialog(null, "UserId is null");
			// return null;
		}

		if (objLotNo != null)
			lotNo = (int) objLotNo;
		else {
			JOptionPane.showMessageDialog(null, "LotNo is null");
			return null;
		}
		if (objExpiringDate != null) {
			// Sometimes it's string, sometimes it's sql.Date
			if (objExpiringDate instanceof String) {
				String date = (String) objExpiringDate;
				try {
					expiringDate = new Date(format.parse(date).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			} else if (objExpiringDate instanceof Date) {
				expiringDate = (Date) objExpiringDate;
			} else if (objExpiringDate instanceof java.sql.Timestamp) {
				java.sql.Timestamp ts = (java.sql.Timestamp) objExpiringDate;
				expiringDate = new Date(ts.getTime());
			} else {
				JOptionPane.showMessageDialog(null, "Unknown type for the expiring date");
				return null;
			}
		} else {
			JOptionPane.showMessageDialog(null, "ExpiringDate is null");
			return null;
		}
		if (!isUserIdValid(userId)) {
			JOptionPane.showMessageDialog(null, "Invalid user id");
			return null;
		}

		if (update && !isParkingLotValid(lotNo)) {
			int subscriptionId = -1;
			if (objId instanceof java.math.BigDecimal) {
				subscriptionId = ((java.math.BigDecimal) objId).intValue();
			} else
				subscriptionId = (int) objId;
			ParkingSubscription subscription = getSubscription(subscriptionId);
			// check if the parkingSpaceNumber is the same. If it is, then it's ok.
			// Otherwise, unavailable parkingLot
			if (subscription.getLotNo() != lotNo) {
				JOptionPane.showMessageDialog(null, "Invalid or unavailable parking lot");
				return null;
			}
		}
		if (expiringDate.before(new Date(new java.util.Date().getTime()))) {
			JOptionPane.showMessageDialog(null, "Invalid expiring date");
			return null;
		}
		return new ParkingSubscription(userId, lotNo, expiringDate);
	}

	private synchronized boolean isUserIdValid(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE user_id=?");
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	private synchronized User getUserByUsername(String username) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE username='" + username + "'");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setEmail(resultSet.getString(4));
				return user;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	/**
	 * @param id
	 * @return true if parkingLot exists and is available, false otherwise
	 */
	private synchronized boolean isParkingLotValid(int lotNo) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM Parking_Lot WHERE lot_id=?");
			preparedStatement.setInt(1, lotNo);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				// parking lot exists
				boolean availability = resultSet.getBoolean(3);
				return availability;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	@Override
	public synchronized JTable populateJTableFromDB() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT s.subs_id, s.Users_user_id, u.username, s.Parking_Lot_lot_id, s.expiring_date FROM Parking_Subscription s, users u WHERE s.Users_user_id=u.user_id";
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class,
						java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class,
						java.lang.Object.class };
				boolean[] canEdit = new boolean[] { false, false, true, true, true, true, true };

				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			};
			table.setColumnSelectionAllowed(true);
			table.getTableHeader().setReorderingAllowed(false);

			JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER);// centrare text
			new ButtonColumn(table, updateSubscription, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deleteSubscription, "Delete", table.getColumnCount() - 1);

			// hide SubscriptionId column
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);
			// hide UserId column
			table.getColumnModel().getColumn(1).setMinWidth(0);
			table.getColumnModel().getColumn(1).setMaxWidth(0);
			table.getColumnModel().getColumn(1).setPreferredWidth(0);

			return table;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized JTable populateJTableFromDB(int userId) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT s.subs_id, s.Users_user_id, u.username, s.Parking_Lot_lot_id, s.expiring_date FROM Parking_Subscription s, users u WHERE (s.Users_user_id=u.user_id AND u.user_id="
					+ userId + " AND s.expiring_date >= SYSDATE)";
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, false)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class,
						java.lang.Integer.class, java.lang.Object.class };
				boolean[] canEdit = new boolean[] { false, false, false, false, false };

				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			};
			table.setColumnSelectionAllowed(true);
			table.getTableHeader().setReorderingAllowed(false);

			JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER);// centrare text
			// new ButtonColumn(table, updateSubscription, "Update", table.getColumnCount()
			// - 2);
			// new ButtonColumn(table, deleteSubscription, "Delete", table.getColumnCount()
			// - 1);

			// hide SubscriptionId column
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);
			// hide UserId column
			table.getColumnModel().getColumn(1).setMinWidth(0);
			table.getColumnModel().getColumn(1).setMaxWidth(0);
			table.getColumnModel().getColumn(1).setPreferredWidth(0);

			return table;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized ParkingSubscription insertSubscription(ParkingSubscription subscription) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "insert into Parking_Subscription(Users_user_id, Parking_Lot_lot_id, expiring_date) values (?, ?, ?)";
			preparedStatement = connect.prepareStatement(sql, new String[] { "subs_id" });
			/** Parameters start with 1 */
			preparedStatement.setInt(1, subscription.getUserId());
			preparedStatement.setInt(2, subscription.getLotNo());
			preparedStatement.setDate(3, subscription.getExpiringDate());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				// throw new SQLException("Creating subscription failed, no rows affected.");
				return null;
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					subscription.setSubscriptionId(generatedKeys.getInt(1));
				} else {
					// throw new SQLException("Creating subscription failed, no ID obtained.");
					return null;
				}
			}
			return subscription;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	
	@Override
	public synchronized ParkingSubscription subscribe(ParkingSubscription subscription, String cardNumber, double fee) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.subscribe(?,?,?,?, ?, ?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.setInt(1, subscription.getUserId());
			cstmt.setInt(2, subscription.getLotNo());
			cstmt.setDate(3, subscription.getExpiringDate());
			cstmt.registerOutParameter(4, OracleTypes.NUMBER);
			cstmt.setString(5, cardNumber);
			cstmt.setDouble(6, fee);
			cstmt.execute();
			subscription.setSubscriptionId(cstmt.getInt(4));
			System.out.println(subscription.getSubscriptionId());
			return subscription;
		} catch (SQLException exception) {
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized ParkingSubscription getSubscription(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM Parking_Subscription WHERE subs_id=" + id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ParkingSubscription subscription = new ParkingSubscription();
				subscription.setSubscriptionId(resultSet.getInt(1));
				subscription.setExpiringDate(resultSet.getDate(2));
				subscription.setUserId(resultSet.getInt(3));
				subscription.setLotNo(resultSet.getInt(4));

				return subscription;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized ParkingSubscription updateSubscription(int id, ParkingSubscription subscription) {
		if (subscription.getSubscriptionId() == id) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				preparedStatement = connect.prepareStatement(
						"UPDATE Parking_Subscription SET Users_user_id=?, Parking_Lot_lot_id=?, expiring_date=? WHERE subs_id="
								+ id);
				preparedStatement.setInt(1, subscription.getUserId());
				preparedStatement.setInt(2, subscription.getLotNo());
				preparedStatement.setDate(3, subscription.getExpiringDate());
				preparedStatement.executeUpdate();
				return subscription;
			} catch (SQLException exception) {
				exception.printStackTrace();
			} finally {
				close();
			}
		}
		return null;
	}

	@Override
	public synchronized List<ParkingSubscription> getAll() {
		try {
			List<ParkingSubscription> subscriptionList = new ArrayList<ParkingSubscription>();
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM Parking_Subscription");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ParkingSubscription subscription = new ParkingSubscription();
				subscription.setSubscriptionId(resultSet.getInt(1));
				subscription.setExpiringDate(resultSet.getDate(2));
				subscription.setUserId(resultSet.getInt(3));
				subscription.setLotNo(resultSet.getInt(4));
				subscriptionList.add(subscription);
			}
			if (!subscriptionList.isEmpty())
				return subscriptionList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean deleteSubscription(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("DELETE FROM Parking_Subscription WHERE subs_id=" + id);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public synchronized boolean paySubscription(String cardNumber, double fee) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.paySubscription(?,?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.setString(1, cardNumber);
			cstmt.setDouble(2, fee);
			int affectedRows = cstmt.executeUpdate();
			return (affectedRows != 0) ? true : false;
		} catch (SQLException exception) {
			showOracleSqlErrors(exception);
			return false;
		} finally {
			close();
		}
	}

	@Override
	public synchronized Date getExpiringDate(int months) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT ADD_MONTHS(SYSDATE, " + months + ") FROM DUAL");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getDate(1);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
}
