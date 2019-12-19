package ro.bd.parkingmanagement.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OracleDatabaseConnection {
	protected Connection connect = null;
	protected PreparedStatement preparedStatement = null;
	protected ResultSet resultSet = null;
	protected static final String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
	protected static final String databaseURL = "jdbc:oracle:thin:@localhost:1521:teo";
	protected static final String user = "\"c##teo\"";
	protected static final String password = "teo";

	public OracleDatabaseConnection() {
		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * @param rs
	 *            resultSet
	 * @return tableModel to build a JTable
	 * @throws SQLException
	 */
	public static DefaultTableModel buildTableModel(ResultSet rs, boolean enableButtons)
			throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}
		if (enableButtons) {
			columnNames.add("Update");
			columnNames.add("Delete");
		}
		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				Object obj = rs.getObject(columnIndex);
				vector.add(obj);
			}
			data.add(vector);
		}
		return new DefaultTableModel(data, columnNames);
	}

	public static void showOracleSqlErrors(SQLException exception) {
		int errorCode = 0;

		String[] errors = exception.getMessage().split("\n"); // multiple exceptions can be raised. Custom exceptions
		// ORA-12899: value too large for column "BANKACCOUNT"."CARD_NUMBER"
		// >=20000 ==> custom errors
		for (String errorMsg : errors) {
			errorCode = Integer.valueOf(errorMsg.substring(4, 9));
			if (errorCode >= 12000) {
				System.err.println(errorMsg);
				JOptionPane.showMessageDialog(null, errorMsg);
			}
		}
	}

	/**
	 * close the connection to the database and the result set
	 */
	protected void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}