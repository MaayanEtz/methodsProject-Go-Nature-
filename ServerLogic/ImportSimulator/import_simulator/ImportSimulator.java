package import_simulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;




public class ImportSimulator {

	// Connection to modules database - here the source table should exist.
	// In out case since we are importing and exporting data to the same DB.
	// this is a special case in which the src_db_con and go_nature_srv.db_con are actually the same connection but this module allows accepting any connection as source and destination
	private Connection src_db_con; 
	
	// Server to interact with to obtain relevant data for import
	private Connection dest_db_con;
	
	
	public ImportSimulator(Connection src_db_con, Connection dest_db_con) {
		this.src_db_con = src_db_con;
		this.dest_db_con = dest_db_con;
	}
	
	private ResultSet getData() {
		String db_table = "externaluserinfo";
		PreparedStatement prepared_statement;
		try {
			prepared_statement = this.src_db_con.prepareStatement("SELECT * FROM " + db_table + ";");
			return prepared_statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean postData(ArrayList<String> src_row) {
		String db_table = "internaluserinfo";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = this.dest_db_con.prepareStatement("INSERT INTO " + db_table
			        + " (`workerId`, `firstName`, `lastName`, `email`, `type`)"
			        + " VALUES (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, src_row.get(0));
			preparedStatement.setString(2, src_row.get(1));
			preparedStatement.setString(3, src_row.get(2));
			preparedStatement.setString(4, src_row.get(3));
			preparedStatement.setString(5, src_row.get(4));
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected > 0) {
				System.out.println(
						"[ImportSimulator | postData |INFO]: Update successful. " + rowsAffected + " rows updated at " + db_table);
				return true;
			} else {
				System.out.println("[ImportSimulator | postData | ERROR]: No records were updated at " +  db_table);
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	public boolean importData() {
		
		ResultSet src_data = getData();
		if (src_data!=null) {
			try {
				int rowNum = 0;
				while (src_data.next()) {
					ArrayList<String> src_row = new ArrayList<>();
					src_row.add(src_data.getString("workerId"));
					src_row.add(src_data.getString("firstName"));
					src_row.add(src_data.getString("lastName"));
					src_row.add(src_data.getString("email"));
					src_row.add(src_data.getString("type"));
					if (!postData(src_row)) {
						return false;
					}
					rowNum ++;
					System.out.println("[ImportSimulator | importData | INFI]: imported " + rowNum + " rows from source");
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	
}
