package ro.bd.parkingmanagement.DAO;

import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import ro.bd.parkingmanagement.UI.ButtonColumn;
import ro.bd.parkingmanagement.UI.JTableUtilities;
import ro.bd.parkingmanagement.model.BankAccount;

public class BankAccountDAOImpl extends OracleDatabaseConnection implements BankAccountDAO {

	private Action deleteBankAccount = new AbstractAction() {
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
			} else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
			Object objCardNumber = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
			String cardNumber = (String) objCardNumber;
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the bank account " + cardNumber + "?", "Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				Object objUserId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 5));
				int userId = -1;
				if (objUserId instanceof java.math.BigDecimal) {
					userId = ((java.math.BigDecimal) objUserId).intValue();
				} else
					userId = (int) objUserId;
				if (getNumberOfBankAccounts(userId) <= 1) {
					JOptionPane.showMessageDialog(null, "Sorry! At least one bank account must remain in the table.");
					return;
				}
				if (deleteBankAccount(id) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private int getNumberOfBankAccounts(int userId) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT COUNT(*) FROM bankaccount WHERE Users_user_id=" + userId + ";");

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
 				return resultSet.getInt(1);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	private Action updateBankAccount = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id = -1;
			if (objId != null) {
				BankAccount bankAccount = checkTableEntry(table, modelRow);
				if (bankAccount != null) {
					if (objId instanceof java.math.BigDecimal) {
						id = ((java.math.BigDecimal) objId).intValue();
					} else {
						id = (int) objId;
					}
					bankAccount.setBankAccountId(id);
					updateBankAccount(id, bankAccount);
				} else { 
					JOptionPane.showMessageDialog(null, "Invalid bank account info");
				}
			} else {
				// Create bankAccount
				BankAccount bankAccount = checkTableEntry(table, modelRow);
				if (bankAccount != null) {
					bankAccount = createBankAccount(bankAccount);
					if (bankAccount != null) // if no exception was raised
						((DefaultTableModel) table.getModel()).setValueAt(bankAccount.getBankAccountId(), modelRow, 0);
				}
			}
		}
	};

	private BankAccount checkTableEntry(JTable table, int modelRow) {
		Object objUserId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 5));
		Object objCardNumber = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objExpiringDate = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
		Object objCvvCode = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 3));
		Object objAccountBalance = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 4));

		String cardNumber;
		int cvvCode, userId;
		Date expiringDate;
		double accountBalance = -1.0;
		if (objUserId != null)
			if (objUserId instanceof java.math.BigDecimal) {
				userId = ((java.math.BigDecimal) objUserId).intValue();
			} else {
				userId = (int) objUserId;
			}
		else {
			JOptionPane.showMessageDialog(null, "UserId is null");
			return null;
		}
		if (objCardNumber != null)
			cardNumber = (String) objCardNumber;
		else {
			JOptionPane.showMessageDialog(null, "CardNumber is null");
			return null;
		}
		if (objExpiringDate != null) {
			// Sometimes it's string, sometimes it's sql.Date
			if (objExpiringDate instanceof String) {
				String date = (String) objExpiringDate;
				try {
					expiringDate = new Date(format.parse(date).getTime());
				} catch (ParseException e) {
					JOptionPane.showMessageDialog(null, "Invalid expiring date");
					e.printStackTrace();
					return null;
				}
			} else if (objExpiringDate instanceof Date) {
				expiringDate = (Date) objExpiringDate;
			} else if (objExpiringDate instanceof Timestamp) {
				Timestamp ts = (Timestamp) objExpiringDate;
				expiringDate = new Date(ts.getTime());
			} else {
				JOptionPane.showMessageDialog(null, "Unknown type for the expiring date");
				return null;
			}
		} else {
			JOptionPane.showMessageDialog(null, "ExpiringDate is null");
			return null;
		}
		if (objCvvCode != null) {
			if (objCvvCode instanceof java.math.BigDecimal) {
				cvvCode = ((java.math.BigDecimal) objCvvCode).intValue();
			} else {
				cvvCode = (int) objCvvCode;
			}
		} else {
			JOptionPane.showMessageDialog(null, "CvvCode is null");
			return null;
		}
		if (cvvCode < 100 || cvvCode > 999) {
			JOptionPane.showMessageDialog(null, "CvvCode is invalid");
			return null;
		}
		if (expiringDate.before(new Date(new java.util.Date().getTime()))) {
			JOptionPane.showMessageDialog(null, "Invalid expiring date");
			return null;
		}
		if (objAccountBalance != null) {
			if (objAccountBalance instanceof java.math.BigDecimal) {
				accountBalance = ((java.math.BigDecimal) objAccountBalance).doubleValue();
			} else
				accountBalance = ((double) objAccountBalance) + 0.0;
		}
		return new BankAccount(userId, cardNumber, expiringDate, cvvCode, accountBalance);
	}

	@Override
	public synchronized JTable populateJTableFromDB(int userId) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT * FROM bankaccount WHERE Users_user_id=" + userId;
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Long.class, java.lang.String.class, java.lang.Object.class,
						java.lang.Integer.class, java.lang.Integer.class, java.lang.Long.class, java.lang.Object.class,
						java.lang.Object.class };
				boolean[] canEdit = new boolean[] { false, true, true, true, true, false, true, true };

				@SuppressWarnings({ "rawtypes", "unchecked" })
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
			new ButtonColumn(table, updateBankAccount, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deleteBankAccount, "Delete", table.getColumnCount() - 1);

			// hide BankAccountId column
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);
			// hide UserId column
			table.getColumnModel().getColumn(5).setMinWidth(0);
			table.getColumnModel().getColumn(5).setMaxWidth(0);
			table.getColumnModel().getColumn(5).setPreferredWidth(0);

			return table;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized BankAccount createBankAccount(BankAccount bankAccount) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "insert into bankaccount(Users_user_id, card_number, expiring_date, cvv_code, account_balance) values (?, ?, ?, ?, ?)";
			preparedStatement = connect.prepareStatement(sql, new String[] { "account_id" });
			/** Parameters start with 1 */
			preparedStatement.setLong(1, bankAccount.getUserId());
			preparedStatement.setString(2, bankAccount.getCardNumber());
			preparedStatement.setDate(3, bankAccount.getExpiringDate());
			preparedStatement.setInt(4, bankAccount.getCvvCode());
			preparedStatement.setInt(5, (int) bankAccount.getAccountBalance());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				// throw new SQLException("Creating bankAccount failed, no rows affected.");
				return null;
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					bankAccount.setBankAccountId(generatedKeys.getInt(1));
				} else {
					// throw new SQLException("Creating bankAccount failed, no ID obtained.");
					return null;
				}
			}
			return bankAccount;
		} catch (SQLException exception) {
			showOracleSqlErrors(exception);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized BankAccount getBankAccount(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM bankaccount WHERE account_id=" + id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setBankAccountId(resultSet.getInt(1));
				bankAccount.setCardNumber(resultSet.getString(2));
				bankAccount.setExpiringDate(resultSet.getDate(3));
				bankAccount.setCvvCode(resultSet.getInt(4));
				bankAccount.setAccountBalance(resultSet.getInt(5));
				bankAccount.setUserId(resultSet.getInt(6));
				return bankAccount;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized BankAccount updateBankAccount(int id, BankAccount bankAccount) {
		if (bankAccount.getBankAccountId() == id) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				preparedStatement = connect.prepareStatement(
						"UPDATE bankaccount SET Users_user_id=?, card_number=?, expiring_date=?, cvv_code=?, account_balance=? WHERE account_id="
								+ id);
				preparedStatement.setLong(1, bankAccount.getUserId());
				preparedStatement.setString(2, bankAccount.getCardNumber());
				preparedStatement.setDate(3, bankAccount.getExpiringDate());
				preparedStatement.setInt(4, bankAccount.getCvvCode());
				preparedStatement.setInt(5, (int) bankAccount.getAccountBalance());
				preparedStatement.executeUpdate();
				return bankAccount;
			} catch (SQLException exception) {
				showOracleSqlErrors(exception);
			} finally {
				close();
			}
		}
		return null;
	}

	@Override
	public synchronized List<BankAccount> getAll() {
		try {
			List<BankAccount> bankAccountList = new ArrayList<BankAccount>();
			connect = DriverManager.getConnection(databaseURL, user, password);

			preparedStatement = connect.prepareStatement("SELECT * FROM bankaccount");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setBankAccountId(resultSet.getInt(1));
				bankAccount.setCardNumber(resultSet.getString(2));
				bankAccount.setExpiringDate(resultSet.getDate(3));
				bankAccount.setCvvCode(resultSet.getInt(4));
				bankAccount.setAccountBalance(resultSet.getInt(5));
				bankAccount.setUserId(resultSet.getInt(6));
				bankAccountList.add(bankAccount);
			}
			if (!bankAccountList.isEmpty())
				return bankAccountList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized String[] getAllCardNumbers(int userId) {
		try {
			List<String> cardNumberList = new ArrayList<String>();
			connect = DriverManager.getConnection(databaseURL, user, password);

			preparedStatement = connect
					.prepareStatement("SELECT card_number FROM bankaccount WHERE Users_user_id=" + userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String cardNumber = resultSet.getString(1);
				cardNumberList.add(cardNumber);
			}
			if (!cardNumberList.isEmpty()) {
				String[] array = new String[cardNumberList.size()];
				array = cardNumberList.toArray(array);
				return array;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean deleteBankAccount(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("DELETE FROM bankaccount WHERE account_id=" + id);
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
	public synchronized boolean isCardNumberAlreadyUsed(String cardNumber) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect
					.prepareStatement("SELECT COUNT(*) FROM bankaccount WHERE card_number='" + cardNumber + "'");
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

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
}
