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
			System.out.println("[handleMessageFromClient | ERROR]: msg from client is not ArrayList<Object> removing client from list");
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

		// choose Endpoint
		switch (endpoint) {
		case "ConnectToServer":
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
			if (payload_type.equals("ArrayList<String>")) {
				boolean user_test_succeeded = false;
				System.out.println("[UserLogin|INFO]: UserLogin enpoint trigered");
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
