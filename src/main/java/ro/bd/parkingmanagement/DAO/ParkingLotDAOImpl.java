package ro.bd.parkingmanagement.DAO;

import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.DriverManager;
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
import oracle.jdbc.OracleTypes;
import ro.bd.parkingmanagement.UI.ButtonColumn;
import ro.bd.parkingmanagement.UI.JTableUtilities;
import ro.bd.parkingmanagement.model.ParkingLot;

public class ParkingLotDAOImpl extends OracleDatabaseConnection implements ParkingLotDAO {

	private Action deleteParkingSpace = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object obj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int lotNo;
			if (obj != null) {
				if (obj instanceof java.math.BigDecimal) {
					lotNo = ((java.math.BigDecimal) obj).intValue();
				} else
					lotNo = (int) obj;
			}
			else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the " + lotNo + " parking lot?", "Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (deleteParkingLot(lotNo) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private Action updateParkingLot = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objLotId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int lotId=-1;
			if (objLotId != null) {
				// Update parkingLot
				ParkingLot parkingLot = checkTableEntry(table, modelRow);
				if (parkingLot != null) {
					if (objLotId instanceof java.math.BigDecimal) {
						lotId = ((java.math.BigDecimal) objLotId).intValue();
					} else
						lotId = (int) objLotId;
					parkingLot.setLotNo(lotId);;
					updateParkingAvailability(lotId, parkingLot.isAvailability());
				}
			} else {
				// Create parkingLot
				ParkingLot parkingLot = checkTableEntry(table, modelRow);
				if (parkingLot != null) {
					parkingLot = insertParkingLot(parkingLot);
					((DefaultTableModel) table.getModel()).setValueAt(parkingLot.getLotNo(), modelRow, 0);
				}
			}
		}
	};

	private ParkingLot checkTableEntry(JTable table, int modelRow) {
		Object objLotId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
		Object objFloor = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objAvailable = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));

		boolean available;
		int floor;
		int lotId = -1;
		if (objLotId != null) {
			if (objLotId instanceof java.math.BigDecimal) {
				lotId = ((java.math.BigDecimal) objLotId).intValue();
			} else
				lotId = (int) objLotId;
		}
		
		
		if (objAvailable != null)
			if (objAvailable instanceof String) {
				String av = (String) objAvailable;
				available = (av.equals("1")) ? true : false;
			} else
				available = (boolean) objAvailable;
		else {
			JOptionPane.showMessageDialog(null, "Available is not true/false");
			return null;
		}
		if (objFloor != null) {
			if (objFloor instanceof java.math.BigDecimal) {
				floor = ((java.math.BigDecimal) objFloor).intValue();
			} else
				floor = (int) objFloor;
		} else {
			JOptionPane.showMessageDialog(null, "Floor is null");
			return null;
		}

		return new ParkingLot(lotId, floor, available);
	}

	@Override
	public synchronized JTable populateJTableFromDB() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.getAllParkingLots(?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();
			resultSet = cstmt.getCursor(1);

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class,
						java.lang.Object.class, java.lang.Object.class };
				boolean[] canEdit = new boolean[] { false, true, true, true, true };

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
			new ButtonColumn(table, updateParkingLot, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deleteParkingSpace, "Delete", table.getColumnCount() - 1);

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
	public synchronized List<ParkingLot> getAll() {
		try {
			List<ParkingLot> parkingLotList = new ArrayList<ParkingLot>();
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.getAllParkingLots(?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();
			resultSet = cstmt.getCursor(1);
			while (resultSet.next()) {				
				ParkingLot parkingLot = new ParkingLot();
				parkingLot.setLotNo(resultSet.getInt(1));
				parkingLot.setFloorNo(resultSet.getInt(2));
				parkingLot.setAvailability(resultSet.getBoolean(3));
				parkingLotList.add(parkingLot);
			}
			if (!parkingLotList.isEmpty())
				return parkingLotList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized ParkingLot getFirstAvailable() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.getFirstAvailable(?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.execute();
			int parkingLotId = cstmt.getInt(1);
			if (parkingLotId > 0) {
				return getParkingLot(parkingLotId);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	
	@Override
	public synchronized int getAvailableLots() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.countAvailableLots(?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.execute();
			int count = cstmt.getInt(1);
			return count;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	@Override
	public synchronized int getOccupiedLots() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.countOccupiedLots(?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.execute();
			int count = cstmt.getInt(1);
			return count;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	@Override
	public ParkingLot insertParkingLot(ParkingLot parkingLot) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.insertParkingLot(?,?,?)}";
	
			CallableStatement cstmt = connect.prepareCall(call);
	
			cstmt.setInt(1, parkingLot.getFloorNo());
			cstmt.setBoolean(2, parkingLot.isAvailability());
			cstmt.registerOutParameter(3, OracleTypes.NUMBER);
			int affectedRows = cstmt.executeUpdate();
			if (affectedRows == 0)
				return null;
			parkingLot.setLotNo(cstmt.getInt(3));
			return parkingLot;
		} catch (SQLException exception) {
			// exception.printStackTrace();
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return null;
	}

	@Override
	public ParkingLot getParkingLot(int lotId) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.getParkingLot(?,?)}";
			OracleCallableStatement cstmt = (OracleCallableStatement) connect.prepareCall(call);
			cstmt.setInt(1, lotId);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.execute();
			resultSet = cstmt.getCursor(2);
			if (resultSet.next()) {
				ParkingLot parkingLot = new ParkingLot();
				parkingLot.setLotNo(resultSet.getInt(1));
				parkingLot.setFloorNo(resultSet.getInt(2));

				String available = resultSet.getString(3);
				boolean av = (available.equals("1")) ? true : false;
				parkingLot.setAvailability(av);
				return parkingLot;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public ParkingLot updateParkingAvailability(int lotId, boolean availability) {
		ParkingLot parkingLot = getParkingLot(lotId);
		if (parkingLot != null) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				String call = "{call Parking_Lot_pkg.updateParkingAvailability(?,?)}";

				CallableStatement cstmt = connect.prepareCall(call);
				cstmt.setInt(1, parkingLot.getLotNo());
				String available = parkingLot.isAvailability()? "1": "0";
				cstmt.setString(2, available);
				cstmt.executeUpdate();
				return parkingLot;
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
	public boolean deleteParkingLot(int lotId) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String call = "{call Parking_Lot_pkg.deleteParkingLot(?)}";
			CallableStatement cstmt = connect.prepareCall(call);
			cstmt.setInt(1, lotId);
			cstmt.executeUpdate();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

}
