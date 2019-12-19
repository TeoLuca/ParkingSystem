package ro.bd.parkingmanagement.DAO;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
import ro.bd.parkingmanagement.model.Price;

public class PriceDAOImpl extends OracleDatabaseConnection implements PriceDAO {

	private Action deletePrice = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object priceIdObj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			Object dayPriceObj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
			Object nightPriceObj = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
			double dayPrice, nightPrice;
			int id;
			if (priceIdObj != null) {
				if (priceIdObj instanceof java.math.BigDecimal) {
					id = ((java.math.BigDecimal) priceIdObj).intValue();
				} else
					id = (int) priceIdObj;
			} else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
			if (dayPriceObj != null) {
				if (dayPriceObj instanceof java.math.BigDecimal) {
					dayPrice = ((java.math.BigDecimal) dayPriceObj).doubleValue();
				} else
					dayPrice = (double) dayPriceObj;
			} else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
			if (nightPriceObj != null) {
				if (nightPriceObj instanceof java.math.BigDecimal) {
					nightPrice = ((java.math.BigDecimal) nightPriceObj).doubleValue();
				} else
					nightPrice = (double) nightPriceObj;
			} else {
				((DefaultTableModel) table.getModel()).removeRow(modelRow);
				return;
			}
		
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete dayPrice=" + dayPrice + " and nightPrice="+nightPrice+" ?", "Warning", dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (deletePrice(id) == true)
					((DefaultTableModel) table.getModel()).removeRow(modelRow);
			}
		}
	};

	private Action updatePrice = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			JTable table = (JTable) e.getSource();
			int modelRow = Integer.valueOf(e.getActionCommand());
			Object objId = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0));
			int id = -1;
			if (objId != null) {
				// Update price
				Price price = checkTableEntry(table, modelRow);
				if (price != null) {
					if (objId instanceof java.math.BigDecimal) {
						id = ((java.math.BigDecimal) objId).intValue();
					} else
						id = (int) objId;
					if (price.getDayPrice() <5 || price.getDayPrice() > 10 || price.getNightPrice() < 1 || price.getNightPrice() > 5 ){
						JOptionPane.showMessageDialog(null, "DayPrice not in [5,10] or NightPrice not in [1,5]");
						return;
					}
					price.setPriceId(id);
					updatePrice(id, price);
				}
			} else {
				// Create price
				Price price = checkTableEntry(table, modelRow);
				if (price != null) {
					price = insertPrice(price);
					if (price == null){
						JOptionPane.showMessageDialog(null, "DayPrice not in [5,10] or NightPrice not in [1,5]");
					} else {
					((DefaultTableModel) table.getModel()).setValueAt(price.getPriceId(), modelRow, 0);
					}
				}
			}
		}
	};

	private Price checkTableEntry(JTable table, int modelRow) {
		Object objDayPrice = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 1));
		Object objNightPrice = (((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2));
		double dayPrice, nightPrice;
		if (objDayPrice != null)
			if (objDayPrice instanceof BigDecimal)
				dayPrice = ((BigDecimal) objDayPrice).doubleValue();
			else if (objDayPrice instanceof Double)
				dayPrice = (double) objDayPrice;
			else
				dayPrice =-1;
		else {
			JOptionPane.showMessageDialog(null, "DayPrice is null");
			return null;
		}
		if (objNightPrice != null)
			if (objNightPrice instanceof BigDecimal)
				nightPrice = ((BigDecimal) objNightPrice).doubleValue();
			else if (objNightPrice instanceof Double)
				nightPrice = (double) objNightPrice;
			else
				nightPrice =-1;
		else {
			JOptionPane.showMessageDialog(null, "NightPrice is null");
			return null;
		}
		
		if (dayPrice < 0 || nightPrice < 0) {
			JOptionPane.showMessageDialog(null, "Unknown type for dayPrice or nightPrice");
			return null;
		}
		return new Price(dayPrice, nightPrice);
	}

	@Override
	public synchronized JTable populateJTableFromDB() {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "SELECT * FROM prices";
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			// It creates the table
			JTable table = new JTable(buildTableModel(resultSet, true)) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] types = new Class[] { java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class,
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
			new ButtonColumn(table, updatePrice, "Update", table.getColumnCount() - 2);
			new ButtonColumn(table, deletePrice, "Delete", table.getColumnCount() - 1);

			// hide id column
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);

			return table;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized Price insertPrice(Price price) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			String sql = "insert into prices(day_price, night_price) values (?, ?)";
			preparedStatement = connect.prepareStatement(sql, new String[] { "price_id" });
			/** Parameters start with 1 */
			preparedStatement.setDouble(1, price.getDayPrice());
			preparedStatement.setDouble(2, price.getNightPrice());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				return null;
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					price.setPriceId(generatedKeys.getInt(1));
				} else {
					return null;
				}
			}
			return price;
		} catch (SQLException exception) {
			// exception.printStackTrace();
			showOracleSqlErrors(exception);
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized Price getPrice(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM prices WHERE price_id=" + id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Price price = new Price();
				price.setPriceId(resultSet.getInt(1));
				price.setDayPrice(resultSet.getDouble(2));
				price.setNightPrice(resultSet.getDouble(3));
				return price;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized Price updatePrice(int id, Price price) {
		if (price.getPriceId() == id) {
			try {
				connect = DriverManager.getConnection(databaseURL, user, password);
				preparedStatement = connect.prepareStatement(
						"UPDATE prices SET day_price=?, night_price=? WHERE price_id=" + id);
				preparedStatement.setDouble(1, price.getDayPrice());
				preparedStatement.setDouble(2, price.getNightPrice());
				preparedStatement.executeUpdate();
				return price;
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
	public synchronized List<Price> getAll() {
		try {
			List<Price> pricePerCategoryList = new ArrayList<Price>();
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("SELECT * FROM prices");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Price price = new Price();
				price.setPriceId(resultSet.getInt(1));
				price.setDayPrice(resultSet.getDouble(2));
				price.setNightPrice(resultSet.getDouble(3));
				pricePerCategoryList.add(price);
			}
			if (!pricePerCategoryList.isEmpty())
				return pricePerCategoryList;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	@Override
	public synchronized boolean deletePrice(int id) {
		try {
			connect = DriverManager.getConnection(databaseURL, user, password);
			preparedStatement = connect.prepareStatement("DELETE FROM prices WHERE price_id=" + id);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

}
