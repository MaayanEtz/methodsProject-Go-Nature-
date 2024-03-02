package GoNatureServer;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import gui.ServerPortFrameController;
import jdbc.MysqlConnection;
import ocsf.server.*;

public class GoNatureServer extends AbstractServer {
	private Connection db_con;
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */

	public GoNatureServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		// Test connection validity
		try {
			System.out.println("db conn is valid: " + this.db_con.isValid(getNumberOfClients()));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if (msg.equals("connect")) { // THIS IS NEW
			System.out.println("[handleMessageFromClient]: Client asked - " + msg);
			try {
				client.sendToClient("connected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("client connected");
			return;
		}
		
		// Extract the client command
		String str_msg = msg.toString().split(",")[0].toString();
		System.out.println("[handleMessageFromClient]: Client asked - " + str_msg);

		// OREN TODELL:
		System.out.println();

		boolean unkown_request = false; // If user sends bad request

		ArrayList<String> responseToClient = new ArrayList<>();

		// Deal with client request
		switch (str_msg) {

		case "[get":
			// Access DB and get the data

			// Send the data back to the client
			// this.sendToAllClients(); - We need to upgrade this to send for a spesific
			// client
			try {
				System.out.println("[SERVER]: Executing [get");
				PreparedStatement ps_get = db_con.prepareStatement("SELECT * FROM orderTbl WHERE ordernumber=?;");
				// PreparedStatement ps_get = this.db_con.prepareStatement("SELECT * FROM
				// gonature.order;");
				String orderNumFromClient = msg.toString().split(",")[1]
						.substring(0, msg.toString().split(",")[1].length() - 1).trim();
				ps_get.setString(1, orderNumFromClient);
				ResultSet rs = ps_get.executeQuery();


				/////////// Send Back To Client (OREN: refactor this)
				ArrayList<String> resultList = new ArrayList<>();

				//////////// TESTING //////////////////////////////////////
				// Get Column data:
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = rsmd.getColumnCount();

				// Create an array to store column names
				String[] db_cols = new String[numColumns];

				// Extract column names and store them in the array
				for (int i = 1; i <= numColumns; i++) {
					db_cols[i - 1] = rsmd.getColumnName(i);
				}
				////////////////////////////////////////////////////////////

				// Iterate through the ResultSet and add data to the ArrayList
				rs.beforeFirst();
				try {
					System.out.println("trying select on order");
					while (rs.next()) {
						// Print out the values
						System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  "
								+ rs.getString(4) + "  " + rs.getString(5) + "  " + rs.getString(6));
						resultList.addAll(Arrays.asList(rs.getString(1), rs.getString(2), rs.getString(3),
								rs.getString(4), rs.getString(5), rs.getString(6)));
					}

				} catch (SQLException e) {
					System.out.println("sql err");
					rs.close();
					e.printStackTrace();
				}

				// OREN: TODEL - just for debugging
				System.out.println("[SERVER]: Printing result which is sent to client:");
				for (String item : resultList) {
					System.out.println(item);
				}

				// Send data
				try {
					client.sendToClient(resultList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}

			break;
		case "[update":
			try {
				PreparedStatement ps_update = db_con.prepareStatement(
						"UPDATE orderTbl SET telephonenumber = ?, parkname = ? WHERE ordernumber = ? ;");
				String orderNumFromClient = msg.toString().split(",")[1].trim();
				String parkNameFromClient = msg.toString().split(",")[2].trim();
				String phoneNumFromClient = msg.toString().split(",")[3]
						.substring(0, msg.toString().split(",")[3].length() - 1).trim();
				System.out.println(
						"[SERVER]: Client asked to change key: " + orderNumFromClient + " phone with the value: "
								+ phoneNumFromClient + " and name with the value: " + parkNameFromClient);
				ps_update.setString(3, orderNumFromClient);
				ps_update.setString(2, parkNameFromClient);
				ps_update.setString(1, phoneNumFromClient);
				int rowsAffected = ps_update.executeUpdate();

				ArrayList<String> resultList = new ArrayList<>();
				// Check result
				if (rowsAffected > 0) {
					System.out.println("[SERVER]: Rows were updated successfully.");
					try {
						client.sendToClient(resultList.add("Updated"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("[SERVER]: No rows were updated.");
					try {
						client.sendToClient(resultList.add("Not Updated"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			} catch (SQLException e) {
				e.printStackTrace();

			}
			// additional cases as needed
		default:
			unkown_request = true;

		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {

		System.out.println("Server listening for connections on port " + getPort());

		// Connect to Data Base
		System.out.println("[SERVER]: trying to connect to DataBase");
		this.db_con = MysqlConnection.connectDB();
		if (db_con != null) {
			System.out.println("[SERVER]: Succesfully connected DB");
		} else {
			System.out.println("[SERVER]: failed to connect DB");
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	public static void main(String args[]) throws IOException {
		GoNatureServer s = new GoNatureServer(5555);
		s.listen();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("clientConnected was called");
		System.out.println(client.getInetAddress().getHostAddress());
		System.out.println(client.getInetAddress().getHostName());
		System.out.println(String.valueOf(client.isAlive()));
		ServerPortFrameController.client_conn_data.add(client);
	}

}
//End of EchoServer class
