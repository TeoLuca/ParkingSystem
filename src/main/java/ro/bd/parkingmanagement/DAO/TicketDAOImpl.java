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

import oracle.jdbc.OracleCallableStatement;
import ro.bd.parkingmanagement.UI.ButtonColumn;
import ro.bd.parkingmanagement.UI.JTableUtilities;
import ro.bd.parkingmanagement.model.Ticket;

public class TicketDAOImpl extends OracleDatabaseConnection implements TicketDAO {

	private Action deleteTicket = new AbstractAction() {
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
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the ticket " + id + "?", "Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (deleteTicket(id) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private Action updateTicket = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id = -1;
			if (objId != null) {
				// Update ticket
				Ticket ticket = checkTableEntry(table, modelRow, true);
				if (ticket != null) {
					if (objId instanceof java.math.BigDecimal) {
						id = ((java.math.BigDecimal) objId).intValue();
					} else
						id = (int) objId;
					ticket.setTicketId(id);
					updateTicket(id, ticket);
				}
			} else {
				// Create ticket
				Ticket ticket = checkTableEntry(table, modelRow, false);
				if (ticket != null) {
					ticket = insertTicket(ticket);
					if (ticket != null) { // no exception was raised
						((DefaultTableModel) table.getModel()).setValueAt(ticket.getTicketId(), modelRow, 0);
					}
				}
			}
		}
	};

	private Ticket checkTableEntry(JTable table, int modelRow, boolean update) {
		Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
		Object objCode = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objFloor = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
		Object objLotId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 3));

		String ticketCode;
		int lotNo, floorNo, ticketId;
		if (objCode != null)
			ticketCode = (String) objCode;
		else {
			JOptionPane.showMessageDialog(null, "Code is null");
			return null;
		}
		if (objFloor != null)
			if (objFloor instanceof java.math.BigDecimal) {
				floorNo = ((java.math.BigDecimal) objFloor).intValue();
			} else
				floorNo = (Integer) objFloor;
		else {
			JOptionPane.showMessageDialog(null, "Floor is null");
			return null;
		}
		if (objLotId != null)
			lotNo = (int) objLotId;
		else {
			JOptionPane.showMessageDialog(null, "ParkingLotNo is null");
			return null;
		}

		if (!isParkingLotValid(lotNo) && update) {
			ticketId = -1;
			if (objId instanceof java.math.BigDecimal) {
				ticketId = ((java.math.BigDecimal) objId).intValue();
			} else
				ticketId = (int) objId;
			Ticket ticket = getTicket(ticketId);
			// check if the parkingSpaceNumber is the same. If it is, then it's ok.
			// Otherwise, unavailable parkingLot
			if (ticket.getLotNo() != lotNo) {
				JOptionPane.showMessageDialog(null, "Invalid/unavailable parking lot");
				return null;
			}
		}
		return new Ticket(ticketCode, lotNo, floorNo);
	}

	@Override
	public synchronized JTable populateJTableFromDB() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT * FROM tickets";
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class,
						java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class };
				boolean[] canEdit = new boolean[] { false, true, true, true, true, true };

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
			new ButtonColumn(table, updateTicket, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deleteTicket, "Delete", table.getColumnCount() - 1);

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
				boolean available = resultSet.getBoolean(3);
				return available;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	@Override
	public synchronized Ticket insertTicket(Ticket ticket) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "insert into tickets(ticket_code, floor_no, Parking_Lot_lot_id) values (?, ?, ?)";
			preparedStatement = connect.prepareStatement(sql, new String[] { "ticket_id" });
			/** Parameters start with 1 */
			preparedStatement.setString(1, ticket.getTicketCode());
			preparedStatement.setInt(2, ticket.getFloorNo());
			preparedStatement.setInt(3, ticket.getLotNo());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				// throw new SQLException("Creating ticket failed, no rows affected.");
				return null;
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					ticket.setTicketId(generatedKeys.getInt(1));
				} else {
					// throw new SQLException("Creating ticket failed, no ID obtained.");
					return null;
				}
			}
			return ticket;
		} catch (SQLException exception) {
			// exception.printStackTrace();
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized Ticket getTicket(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM tickets WHERE ticket_id=?");
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(resultSet.getInt(1));
				ticket.setTicketCode(resultSet.getString(2));
				ticket.setFloorNo(resultSet.getInt(3));
				ticket.setLotNo(resultSet.getInt(4));

				return ticket;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized Ticket getTicketByCode(String code) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM tickets WHERE ticket_code=?");
			preparedStatement.setString(1, code);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(resultSet.getInt(1));
				ticket.setTicketCode(resultSet.getString(2));
				ticket.setFloorNo(resultSet.getInt(3));
				ticket.setLotNo(resultSet.getInt(4));

				return ticket;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean isValid(String ticketCode) {
		return (getTicketByCode(ticketCode) != null) ? true : false;
	}

	@Override
	public synchronized Ticket updateTicket(int id, Ticket ticket) {
		if (ticket.getTicketId() == id) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				preparedStatement = connect.prepareStatement(
						"UPDATE tickets SET ticket_code=?, floor_no=?, Parking_Lot_lot_id=? WHERE ticket_id= ?");
				preparedStatement.setString(1, ticket.getTicketCode());
				preparedStatement.setInt(2, ticket.getFloorNo());
				preparedStatement.setInt(3, ticket.getLotNo());
				preparedStatement.setLong(4, id);
				preparedStatement.executeUpdate();
				return ticket;
			} catch (SQLException exception) {
				// exception.printStackTrace();
				showOracleSqlErrors(exception);
			} finally {
				close();
			}
		}
		return null;
	}

	@Override
	public synchronized List<Ticket> getAll() {
		try {
			List<Ticket> ticketList = new ArrayList<Ticket>();
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM tickets");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Ticket ticket = new Ticket();
				ticket.setTicketId(resultSet.getInt(1));
				ticket.setTicketCode(resultSet.getString(2));
				ticket.setFloorNo(resultSet.getInt(3));
				ticket.setLotNo(resultSet.getInt(4));
				ticketList.add(ticket);
			}
			if (!ticketList.isEmpty())
				return ticketList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean deleteTicket(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("DELETE FROM tickets WHERE ticket_id= ? ");
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

	public synchronized boolean payTicket(String ticketCode, String cardNumber, double bankAccountValue, double fee) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.payTicket(?,?,?,?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.setString(1, ticketCode);
			cstmt.setString(2, cardNumber);
			cstmt.setDouble(3, bankAccountValue);
			cstmt.setDouble(4, fee);
			cstmt.executeUpdate();
			return true;
		} catch (SQLException exception) {
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return false;
	}
}
