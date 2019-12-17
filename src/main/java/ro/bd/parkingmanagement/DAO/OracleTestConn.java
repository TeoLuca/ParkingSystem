package ro.bd.parkingmanagement.DAO;

import java.sql.*;

public class OracleTestConn {
	public static void main(String args[]) {
		try {
			// step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// step2 create the connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:teo", "\"c##teo\"",
					"teo");

			// step3 create the statement object
			Statement stmt = con.createStatement();

			// step4 execute query
			ResultSet rs = stmt.executeQuery("SELECT * FROM prices");
			System.out.println("begin\n");
			System.out.println(con != null);
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getInt(2));
			System.out.println("end\n");
			// step5 close the connection object
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}