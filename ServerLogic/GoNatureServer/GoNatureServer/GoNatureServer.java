package GoNatureServer;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import GoNatureServer.SerialMessage.*;
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

	public GoNatureServer(int port, String db_name, String db_host, String db_user, String db_pass) {
		super(port);
		this.db_name= db_name;
		this.db_host= db_host;
		this.db_user= db_user;
		this.db_pass= db_pass;
	}
	
	
	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("YEY");
		//////////////////// Test DB Connection ///////////////////////////
		try {
			System.out.println("db conn is valid: " + this.db_con.isValid(db_conn_validation_timeout_milisecs));
		} catch (SQLException e1) {
			e1.printStackTrace();
			// ORENB TODO: client asked something but we are not connected to DB for some
			// reason - should we terminate?? for now print error
		}

		///////////////////////////////// Accept Client Message
		///////////////////////////////// //////////////////////////////////////////////////
		// This section is implemented with SerialMessage (Read SerialMessage
		///////////////////////////////// information in the file related to the class)
		SerialMessage s_msg = null;
		try {
			s_msg = (SerialMessage) msg;
		} catch (ClassCastException e) {
			e.printStackTrace();
			try { // ORENB TODO - send full SerialMessage
				SerialMessage message_with_error = new SerialMessage();
				message_with_error.setE(e);
				client.sendToClient(message_with_error);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Endpoint ep = s_msg.getEndPoint();
		PreparedStatement prepared_statement = null;
		String db_table = null;
		ResultSet result_set = null;

		///////////////////////////////////// Analyze SerialMessage's Endpoint
		///////////////////////////////////// //////////////////////////////////////
		switch (ep) {
		// Client requested to connect the server - we just inform the Client that he
		// succeeded
		case ConnectToServer:
			System.out.println("[ConnectToServer|INFO]: ConnectToServer enpoint trigered");
			try {
				client.sendToClient(new SerialMessage(ActionType.INFORM, TypeOfObject.StringMessage,
						Endpoint.ConnectToServer, new String("Connected Succesfully")));
			} catch (IOException e) {
				System.out
						.println("[ConnectToServer|ERROR]: Failed to inform the client that he succesfully connected!");
				e.printStackTrace();
			}
			System.out.println("[ConnectToServer|INFO]: Client connected");
			return;

		case loginUser:
			System.out.println("[loginUser|INFO]: loginUser enpoint trigered");
			db_table = "users";
			try {
				ArrayList<String> payLoad = (ArrayList<String>) s_msg.getPayload(); // ANNA: here i am assuming payload
																					// is ArrayList<String> =
																					// {"username_value", "password
																					// value"}
				String user_name = payLoad.get(0);
				String password = payLoad.get(1);
				System.out.println("[loginUser|INFO]: extracted username: " + user_name + " and password: " + password
						+ " from clients payLoad");

				try {

					prepared_statement = db_con
							.prepareStatement("SELECT username FROM " + db_table + " WHERE username=?, password=?;");
					prepared_statement.setString(1, user_name);
					prepared_statement.setString(2, password);
					result_set = prepared_statement.executeQuery();

					SerialMessage return_message_to_client = null;

					if (!result_set.next()) {
						// ResultSet is empty
						System.out.println(
								"[loginUser|INFO]: ResultSet is empty - didnt not find the username: " + user_name);
						return_message_to_client = new SerialMessage(ActionType.INFORM, TypeOfObject.StringMessage,
								Endpoint.loginUser, false); // ANNA: here is send fasle inside the payload indicating
															// that user is not in db
					} else {
						// ResultSet is not empty
						System.out.println("[loginUser|INFO]:ResultSet is not empty " + user_name + " was found");
						return_message_to_client = new SerialMessage(ActionType.INFORM, TypeOfObject.StringMessage,
								Endpoint.loginUser, true);
					}

					// Send message back to Client
					try {
						client.sendToClient(return_message_to_client);
					} catch (IOException e) {
						System.out.println("[loginUser|ERROR]: Failed sending data back to client");
						e.printStackTrace();
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("[loginUser|ERROR]: Failed MySQL querry");
					e1.printStackTrace();
				}

			} catch (ClassCastException e) {
				System.out.println("[loginUser|ERROR]: Bad Payload from Client");
				e.printStackTrace();
			}
			return;

		case createNewOrder:
			System.out.println("[handleMessageFromClient|INFO]: loginUser enpoint trigered");
			return;
		case cancelOrder:
			return;
		case editOrder:
			return;
		case getOrder:
			return;
		case getPark:
			return;

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
		ms_conn = new MysqlConnection(db_name, db_host, db_user, db_pass);
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

		System.out.println(client.getInetAddress().getHostAddress());
		System.out.println(client.getInetAddress().getHostName());
		System.out.println(String.valueOf(client.isAlive()));
		ServerPortFrameController.client_conn_data.add(client);
		
		System.out.println("[clientConnected|INFO]: clientConnected was called");
		try {
			//client.sendToClient(new SerialMessage(ActionType.INFORM, TypeOfObject.StringMessage,
					//Endpoint.ConnectToServer, new String("Connected Succesfully")));
			client.sendToClient("Server: client was connected");
		} catch (IOException e) {
			System.out
					.println("[clientConnected|ERROR]: Failed to inform the client that he succesfully connected!");
			e.printStackTrace();
		}
		return;
	}

}
//End of EchoServer class
