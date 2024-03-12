package GoNatureServer;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import gui.ServerPortFrameController;
import jdbc.MysqlConnection;
import ocsf.server.*;

public class GoNatureServer extends AbstractServer {
	// Let GoNatureServer keep DB connection objects to allow more flexibility
	private Connection db_con;
	private MysqlConnection ms_conn;
	private String db_name;
	private String db_host;
	private String db_user;
	private String db_pass;

	/// time outs ///
	int db_conn_validation_timeout_milisecs = 10000;

	public GoNatureServer(int port) {
		super(port);
	}

	public GoNatureServer(int port, String db_name, String db_host, String db_user, String db_pass) {
		super(port);
		this.db_name = db_name;
		this.db_host = db_host;
		this.db_user = db_user;
		this.db_pass = db_pass;
	}

	private void send_response(ConnectionToClient client, String endpoint, String payload_type, Object payload)
			throws IOException {
		ArrayList<Object> response_arrlst;
		try {
			response_arrlst = new ArrayList<>();
			response_arrlst.add(endpoint); // Add Endpoint
			response_arrlst.add(payload_type); // Add Payload-type
			response_arrlst.add(payload); // Add Payload
			client.sendToClient(response_arrlst);
			return;
		} catch (IOException e) {
			System.out.println("[send_response|ERROR]:Failed to inform the client: " + client.getInetAddress()
					+ " server failed endpoint: " + endpoint);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		//// DEBUG /////////
		// System.out.println("[handleMessageFromClient|DEBUG]: was called");
		///////////////////////////

		// Message Variables
		String endpoint;
		String payload_type;
		ArrayList<Object> arr_msg;

		// Convert to ArrayList test
		try {
			arr_msg = (ArrayList<Object>) msg;
			System.out.println("[handleMessageFromClient | DEBUG]: converted msg to arr list");
		} catch (ClassCastException e_clas) {
			ServerPortFrameController.client_conn_data.remove(client);
			System.out.println(
					"[handleMessageFromClient | ERROR]: msg from client is not ArrayList<Object> removing client from list");
			// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		////////////// Deal With Client Message ////////////////
		endpoint = (String) arr_msg.get(0);
		payload_type = (String) arr_msg.get(1);
		String db_table = ""; // Table name to be used
		PreparedStatement prepared_statement = null;
		ResultSet result_set = null;
		String error = "";

		// choose Endpoint
		switch (endpoint) {
		case "ConnectToServer":
			System.out.println("[ConnectToServer|INFO]: ConnectToServer enpoint trigered");
			try {
				send_response(client, new String("ConnectToServer"), new String("Boolean"), new Boolean(true));
				return;
			} catch (IOException e) {
				System.out.println(
						"[ConnectToServer_ep |ERROR ]: Failed to inform the client that he succesfully connected!");
				e.printStackTrace();
				return;
			}

		case "UserLogin":
			System.out.println("[UserLogin|INFO]: UserLogin enpoint trigered");
			if (payload_type.equals("ArrayList<String>")) {
				boolean user_test_succeeded = false;
				db_table = "users";
				try {
					ArrayList<String> payload = (ArrayList<String>) arr_msg.get(2);

					// TODO ORENB: delete logging user data
					String username_from_client = payload.get(0);
					System.out.println(
							"[UserLogin|DEBUG]: extracted username: " + username_from_client + " from clients payload");
					String password_from_client = payload.get(1);
					System.out.println(
							"[UserLogin|DEBUG]: extracted password: " + password_from_client + " from clients payload");

					// prepare MySQL query prepare
					prepared_statement = db_con
							.prepareStatement("SELECT username FROM " + db_table + " WHERE username=? AND password=?;");
					prepared_statement.setString(1, username_from_client);
					prepared_statement.setString(2, password_from_client);
					result_set = prepared_statement.executeQuery();

					// Check MySql Result
					if (!result_set.next()) { // ResultSet is empty
						System.out.println("[loginUser|INFO]: ResultSet is empty - didnt not find the username: "
								+ username_from_client);
						user_test_succeeded = false;

					} else {
						System.out.println(
								"[loginUser|INFO]:ResultSet is not empty " + username_from_client + " was found");
						user_test_succeeded = true;
					}

					// Catch problematic Payload
				} catch (ClassCastException e_clas) {
					System.out.println(
							"[UserLogin | ERROR]: Client sent payload for UserLogin ep which is not an ArrayList<String>");
					// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
					return;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				// Response to client
				try {
					send_response(client, new String("UserLogin"), new String("Boolean"),
							new Boolean(user_test_succeeded));
				} catch (IOException e) {
					System.out.println("[UserLogin_ep |ERROR ]: Failed UserLogin");
					e.printStackTrace();
					e.printStackTrace();
				}

			} else {
				// Client asked UserLogin end-point but sent bad payload-type
				try {
					send_response(client, new String("UserLogin"), new String("ErrorString"),
							new String("Client asked UserLogin end point but payload-type was not ArrayList<String>!"));
				} catch (IOException e) {
					System.out.println("[UserLogin_ep |ERROR ]: Failed UserLogin");
					e.printStackTrace();
					e.printStackTrace();
				}
			}
			return;

		case "OrderCreate":
			if (payload_type.equals("ArrayList<String>")) {
				boolean create_order_test_succeeded = false;
				db_table = "orders";
				try {
					// Extract pay-load
					ArrayList<String> payload = (ArrayList<String>) arr_msg.get(2);
					String visitor_id = payload.get(0);
					String park_name = payload.get(1);
					String time_of_visit = payload.get(2);
					String visitor_number = payload.get(3);
					String visitor_email = payload.get(4);
					String visitor_phone = payload.get(5);
					System.out.println("[OrderCreate | DEBUG]: extracted: " + payload);

					//
					if (true) { // TODO ORENB: Here will be algorithm for problematic time instead of true
						// prepare MySQL query prepare
						prepared_statement = db_con.prepareStatement("INSERT INTO " + db_table
								+ " (`visitor_id`, `park_name`, `time_of_visit`,`visitor_number`,`visitor_email`, `visitor_phone`)"
								+ "VALUES (?,?, ?, ?, ?, ?);");
						prepared_statement.setString(1, visitor_id);
						prepared_statement.setString(2, park_name);
						prepared_statement.setString(3, time_of_visit);
						prepared_statement.setString(4, visitor_number);
						prepared_statement.setString(5, visitor_email);
						prepared_statement.setString(6, visitor_phone);
						prepared_statement.executeUpdate();
						create_order_test_succeeded = true;
					}

					// Catch Problems
				} catch (ClassCastException e_clas) {
					System.out.println(
							"[UserLogin | ERROR]: Client sent payload for UserLogin ep which is not an ArrayList<String>");
					error = e_clas.getMessage();
					// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
				} catch (SQLException e_sql) {
					System.out.println("[OrderCreate | ERROR]: MySQL query execution error");
					e_sql.printStackTrace();
					error = e_sql.getMessage();
				} catch (Exception e) {
					e.printStackTrace();
					error = e.getMessage();
				}

				// Response to client
				try {
					send_response(client, new String("OrderCreate"), new String("Boolean"),
							new Boolean(create_order_test_succeeded));
				} catch (IOException e) {
					System.out.println("[OrderCreate_ep |ERROR ]: Failed OrderCreate response");
					e.printStackTrace();
				}

			} else {
				// Client asked UserLogin end-point but sent bad payload-type
				try {
					send_response(client, new String("UserLogin"), new String("ErrorString"),
							new String("Client asked UserLogin end point but payload-type was not ArrayList<String>!"));
				} catch (IOException e) {
					System.out.println("[UserLogin_ep |ERROR]: Failed UserLogin error response");
					e.printStackTrace();
				}

			}
			return;

		case "OrderCancel":
			// "DELETE FROM orders WHERE orderId = 5;"
			if (payload_type.equals("String")) {
				boolean cancel_order_test_succeeded = false;
				db_table = "orders";
				try {
					// Extract pay-load
					String payload = (String) arr_msg.get(2);
					System.out.println("[OrderCancel | DEBUG]: extracted: " + payload);

					// MySQL
					prepared_statement = db_con.prepareStatement("DELETE FROM " + db_table + " WHERE orderId = ?");
					prepared_statement.setInt(1, Integer.parseInt(payload));
					int rowsAffected = prepared_statement.executeUpdate();

					// Cancellation result update check
					if (rowsAffected != 0) {
						cancel_order_test_succeeded = true;
					}

				}
				// Catch Problems
				catch (ClassCastException e_clas) {
					System.out.println(
							"[UserLogin | ERROR]: Client sent payload for UserLogin ep which is not an ArrayList<String>");
					error = e_clas.getMessage();
					// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
				} catch (SQLException e_sql) {
					System.out.println("[OrderCreate | ERROR]: MySQL query execution error");
					e_sql.printStackTrace();
					error = e_sql.getMessage();
				} catch (Exception e) {
					e.printStackTrace();
					error = e.getMessage();
				}
				
				// Response to client
				try {
					send_response(client, new String("OrderCancel"), new String("Boolean"),
							new Boolean(cancel_order_test_succeeded));
				} catch (IOException e) {
					System.out.println("[OrderCreate_ep |ERROR ]: Failed OrderCreate response");
					e.printStackTrace();
				}


			} else {
				// Client asked OrderCancel end-point but sent bad pay-load-type
				try {
					send_response(client, new String("OrderCancel"), new String("ErrorString"),
							new String("Client asked OrderCancel end point but payload-type was not String!"));
				} catch (IOException e) {
					System.out.println("[OrderCancel_ep |ERROR]: Failed OrderCancel error response");
					e.printStackTrace();
				}
			}
			return;
			
		default:
			System.out.println("[handleMessageFromClient|info]: default enpoint");
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		//////////// Connect to Data Base ///////////////////
		// ORENB_TODO: DB should stay connected all the time?
		// ms_conn = new MysqlConnection(db_name, db_host, db_user, db_pass);
		System.out.println("[SERVER]: trying to connect to DataBase");
		this.db_con = ms_conn.connectDB();
		if (db_con != null) {
			System.out.println("[SERVER]: Succesfully connected DB");
		} else {
			System.out.println("[SERVER]: failed to connect DB");
		}
		/////////////////////////////////////////////////////////////////
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

//	public static void main(String args[]) throws IOException {
//		GoNatureServer s = new GoNatureServer(5555);
//		s.listen();
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		// DEBUG//////////////
		// System.out.println(client.getInetAddress().getHostAddress());
		// System.out.println(client.getInetAddress().getHostName());
		// System.out.println(String.valueOf(client.isAlive()));
		//////////////////////
		System.out.println("[clientConnected|INFO]: adding client to gui list");
		ServerPortFrameController.client_conn_data.add(client);
		return;
	}
}
