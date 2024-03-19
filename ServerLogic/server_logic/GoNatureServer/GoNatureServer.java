package GoNatureServer;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	public static ServerPortFrameController controller;

	private Map<String, ConnectionToClient> logged_in_clients = new HashMap<>();

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
		ArrayList<String> arr = new ArrayList<String>();

		// Convert to ArrayList test
		try {
			arr_msg = (ArrayList<Object>) msg;
			System.out.println("[handleMessageFromClient | DEBUG]: converted msg to arr list");
		} catch (ClassCastException e_clas) {
			// controller.removeClient(client);
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

		// choose End-point
		switch (endpoint) {

		// -------------------------------------------------------------------------------------
		// SERVER CONNECT
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

			// -------------------------------------------------------------------------------------
			// USER LOGIN
		case "UserLogin":
			System.out.println("[UserLogin|INFO]: UserLogin enpoint trigered");
			String user_type = "null";
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
							.prepareStatement("SELECT type FROM " + db_table + " WHERE username=? AND password=?;");
					prepared_statement.setString(1, username_from_client);
					prepared_statement.setString(2, password_from_client);
					result_set = prepared_statement.executeQuery();

					// Check MySql Result
					if (!result_set.next()) { // ResultSet is empty
						System.out.println("[loginUser|INFO]: ResultSet is empty - didnt not find the username: "
								+ username_from_client);
						user_test_succeeded = false;

					} else {
						user_type = result_set.getString("type");
						System.out.println("[loginUser|INFO]:ResultSet is not empty " + username_from_client
								+ " was found returning to client: " + user_type);

						/// LOGIN and already logged in test
						if (logged_in_clients.get(username_from_client) == null) {
							logged_in_clients.put(username_from_client, client);
							System.out.println("[UserLogin | info]: logged-in: " + username_from_client);
							user_test_succeeded = true;
						} else {
							System.out.println("[UserLogin | ERROR]: User alrady logged-in");
							try {
								send_response(client, new String("UserLogin"), new String("ErrorString"),
										new String("Client already logged-in"));
							} catch (IOException e) {
								System.out.println("[UserLogin_ep |ERROR ]: Failed UserLogin");
								e.printStackTrace();
							}
						}
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
					send_response(client, new String("UserLogin"), new String("String"), user_type);
				} catch (IOException e) {
					System.out.println("[UserLogin_ep |ERROR ]: Failed UserLogin");
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
				}
			}
			return;

		// -------------------------------------------------------------------------------------
		// ORDER Create
		case "OrderCreate":
			int orderId = -1;
			if (payload_type.equals("ArrayList<String>")) {
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
					if (checkOrderTime(payload)) { // TODO ORENB: Here will be algorithm for problematic time instead of
													// true
						// prepare MySQL query prepare
						prepared_statement = db_con.prepareStatement("INSERT INTO " + db_table
								+ " (`visitor_id`, `park_name`, `time_of_visit`,`visitor_number`,`visitor_email`, `visitor_phone`, `status`)"
								+ "VALUES (?, ?, ?, ?, ?, ?, 'active');");
						prepared_statement.setString(1, visitor_id);
						prepared_statement.setString(2, park_name);
						prepared_statement.setString(3, time_of_visit);
						prepared_statement.setString(4, visitor_number);
						prepared_statement.setString(5, visitor_email);
						prepared_statement.setString(6, visitor_phone);
						prepared_statement.executeUpdate();
						// -------------------------------------------------

						prepared_statement = null;
						prepared_statement = db_con.prepareStatement("SELECT MAX(orderId) AS max FROM orders");
						result_set = prepared_statement.executeQuery();
						if (!result_set.next()) {
							System.out.println("[OrderCreate | ERROR]: couldnt get max id!");
							send_response(client, new String("OrderCreate"), new String("ErrorString"),
									new String("couldnt get max from DB"));
						}
						orderId = result_set.getInt("max");
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
					send_response(client, new String("OrderCreate"), new String("Integer"), new Integer(orderId));
				} catch (IOException e) {
					System.out.println("[OrderCreate_ep |ERROR ]: Failed OrderCreate response");
					e.printStackTrace();
				}

			} else {
				// Client asked UserLogin end-point but sent bad payload-type
				try {
					send_response(client, new String("OrderCreate"), new String("ErrorString"), new String(
							"Client asked OrderCreate end point but payload-type was not ArrayList<String>!"));
				} catch (IOException e) {
					System.out.println("[OrderCreate_ep |ERROR]: Failed UserLogin error response");
					e.printStackTrace();
				}
			}
			return;

		// -------------------------------------------------------------------------------------
		// ORDER UPDATE
		case "OrderUpdate":
			boolean orderUpdated = false;
			if (payload_type.equals("ArrayList<String>")) {
				db_table = "orders";
				try {
					// Extract pay-load (Pay attention order_id as oppose to Order Create with
					// visitor_id)
					ArrayList<String> payload = (ArrayList<String>) arr_msg.get(2);
					String order_id = payload.get(0);
					String visitor_id = payload.get(1);
					String park_name = payload.get(2);
					String time_of_visit = payload.get(3);
					String visitor_number = payload.get(4);
					String visitor_email = payload.get(5);
					String visitor_phone = payload.get(6);
					System.out.println("[OrderUpdate | DEBUG]: extracted: " + payload);

					// Check if order_id exists:
					// prepare MySQL query prepare
					prepared_statement = db_con
							.prepareStatement("SELECT orderId FROM " + db_table + " WHERE orderId=?;");
					prepared_statement.setString(1, order_id);
					result_set = prepared_statement.executeQuery();

					// Check MySql Result for orderId Client sent
					if (!result_set.next()) { // ResultSet is empty
						System.out.println(
								"[OrderUpdate|INFO]: ResultSet is empty - didnt not find the orderId: " + order_id);
						// Response to client
						try {
							send_response(client, new String("OrderUpdate"), new String("ErrorString"),
									new String("order_id not in db: " + order_id));

						} catch (IOException e) {
							System.out.println("[OrderUpdate_ep |ERROR ]: Failed OrderUpdate response");
							e.printStackTrace();
						}
						return;
					} else {
						System.out.println("[OrderUpdate|INFO]:ResultSet is not empty " + order_id + " was found");
					}

					// Update payload since it has order_id at index 0
					System.out.println("[OrderUpdate |DEBUG ]: payload before update: " + payload);
					payload.remove(0);
					System.out.println("[OrderUpdate |DEBUG ]: payload updated: " + payload);

					if (checkOrderTime(payload)) { // TODO ORENB: Here will be algorithm for problematic time instead of
													// true
						// prepare MySQL query prepare
						prepared_statement = db_con.prepareStatement("UPDATE " + db_table
								+ " SET `visitor_id`=?, `park_name`=?, `time_of_visit`=?, `visitor_number`=?, `visitor_email`=?, `visitor_phone`=?"
								+ " WHERE `orderId`=?;");

						prepared_statement.setString(1, visitor_id);
						prepared_statement.setString(2, park_name);
						prepared_statement.setString(3, time_of_visit);
						prepared_statement.setString(4, visitor_number);
						prepared_statement.setString(5, visitor_email);
						prepared_statement.setString(6, visitor_phone);
						prepared_statement.setString(7, order_id);
						int rowsAffected = prepared_statement.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println(
									"[OrderUpdate|INFO] Update successful. " + rowsAffected + " rows updated.");
							orderUpdated = true;
						} else {
							System.out.println("[OrderUpdate|ERROR] No records were updated.");
							try {
								send_response(client, new String("OrderUpdate"), new String("ErrorString"),
										new String("Capacity check passed but db failed to udpate"));

							} catch (IOException e) {
								System.out.println("[OrderUpdate_ep |ERROR ]: Failed OrderUpdate response");
								e.printStackTrace();

							}
							return;
						}
					}

					// Catch Problems
				} catch (ClassCastException e_clas) {
					System.out.println(
							"[OrderUpdate | ERROR]: Client sent payload for OrderUpdate ep which is not an ArrayList<String>");
					error = e_clas.getMessage();
					// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
				} catch (SQLException e_sql) {
					System.out.println("[OrderUpdate | ERROR]: MySQL query execution error");
					e_sql.printStackTrace();
					error = e_sql.getMessage();
				} catch (Exception e) {
					e.printStackTrace();
					error = e.getMessage();
				}

				// Response to client
				try {
					send_response(client, new String("OrderUpdate"), new String("Boolean"), orderUpdated);
				} catch (IOException e) {
					System.out.println("[OrderUpdate_ep |ERROR ]: Failed OrderUpdate response");
					e.printStackTrace();
				}

			} else {
				// Client asked OrderUpdate end-point but sent bad payload-type
				try {
					send_response(client, new String("OrderUpdate"), new String("ErrorString"), new String(
							"Client asked OrderUpdate end point but payload-type was not ArrayList<String>!"));
				} catch (IOException e) {
					System.out.println("[OrderUpdate_ep |ERROR]: Failed OrderUpdate error response");
					e.printStackTrace();
				}

			}
			return;

		// -------------------------------------------------------------------------------------
		// ORDER CANCEL
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

		// -------------------------------------------------------------------------------------
		// ORDER- GET
		case "OrderGet":
			System.out.println("[OrderGet|INFO]: OrderGet enpoint trigered");
			if (!payload_type.equals("String")) {
				System.out.println("[OrderGet | ERROR]: Client sent payload for OrderGet ep which is not a String");
				try {
					// send error to client
					send_response(client, new String("OrderGet"), new String("ErrorString"),
							new String("Client asked OrderGet end point but payload-type was not String!"));
				} catch (IOException e) {
					System.out.println("[OrderCancel_ep |ERROR]: Failed OrderCancel error response");
					e.printStackTrace();
				}
				return;
			}
			try {
				PreparedStatement ps = db_con.prepareStatement("SELECT * FROM orders WHERE orderId = ?");
				ps.setString(1, (String) arr_msg.get(2));
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) { // order not found in DB
					System.out.println(
							"[OrderGet | ERROR]: order_id not found in DB, sending order not found response to client");
					send_response(client, new String("OrderGet"), new String("String"), new String("null"));
					return;
				}
				// add order parameters to ArrayList to send to client
//				arr.add(new String("" + rs.getInt("orderId")));
//				arr.add(new String(rs.getString("park_name")));
//				arr.add(new String(rs.getString("time_of_visit")));
//				arr.add(new String("" + rs.getInt("visitor_number")));
//				arr.add(new String(rs.getString("visitor_email")));
//				arr.add(new String(rs.getString("visitor_phone")));
				// send Order to Client
				send_response(client, new String("OrderGet"), new String("ArrayList<String>"),
						new ArrayList<String>(Arrays.asList("yossi")));
				System.out.println("[OrderGet|INFO]: OrderGet sent response of ArrayList<>");
				System.out.println(arr);

			} catch (SQLException e) {
				System.out.println("[OrderGet | ERROR]: SQLException was thrown!");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("[OrderGet | ERROR]: IOException was thrown!");
				e.printStackTrace();
			}
			return;

		// -------------------------------------------------------------------------------------
		// PARK
		case "ParksListGet":
			try {
				PreparedStatement ps = db_con.prepareStatement("Select parkName FROM parks");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					arr.add(rs.getString("parkName"));
				}
				if (arr.isEmpty()) {
					// no Parks???
				}
				send_response(client, new String("ParksListGet"), new String("ArrayList<String>"), arr);
			} catch (SQLException e) {
				System.out.println("[ParksListGet | ERROR]: SQLException was thrown!");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("[ParksListGet | ERROR]: IOException was thrown!");
				e.printStackTrace();
			}

			// -------------------------------------------------------------------------------------
			// GUIDE
		case "GroupGuideCheck":
			System.out.println("[GroupGuideCheck|INFO]: GroupGuideCheck enpoint trigered");
			if (payload_type.equals("String")) {
				boolean guide_test_succeeded = false;
				db_table = "guides";
				try {
					String payload = (String) arr_msg.get(2);

					// prepare MySQL query prepare
					prepared_statement = db_con.prepareStatement("SELECT * FROM " + db_table + " WHERE visitor_id=?;");
					prepared_statement.setString(1, payload);
					result_set = prepared_statement.executeQuery();

					// Check MySql Result
					if (!result_set.next()) { // ResultSet is empty
						System.out
								.println("[GroupGuideCheck|INFO]: ResultSet is empty - didnt not find the GroupGuide: "
										+ payload);

					} else {
						System.out.println("[GroupGuideCheck|INFO]:ResultSet is not empty " + payload + " was found");
						guide_test_succeeded = true;
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
					send_response(client, new String("GroupGuideCheck"), new String("Boolean"), guide_test_succeeded);
				} catch (IOException e) {
					System.out.println("[GroupGuideCheck |ERROR ]: Failed GroupGuideCheck");
					e.printStackTrace();
				}

			} else {
				// Client asked GroupGuideCheck end-point but sent bad payload-type
				try {
					send_response(client, new String("GroupGuideCheck"), new String("ErrorString"),
							new String("Client asked GroupGuideCheck end point but payload-type was not String!"));
				} catch (IOException e) {
					System.out.println("[GroupGuideCheck_ep |ERROR ]: Failed GroupGuideCheck");
					e.printStackTrace();
				}
			}
			return;

		case "OrderedEnter":
			System.out.println("[OrderedEnter|INFO]: OrderedEnter enpoint trigered");
			if (payload_type.equals("String")) {
				boolean ordered_enterance_test_succeeded = false;
				db_table = "orders";
				try {
					String payload = (String) arr_msg.get(2);

					// prepare MySQL query prepare
					prepared_statement = db_con
							.prepareStatement("SELECT park_name,visitor_number  FROM " + db_table + " WHERE orderId=?;");
					prepared_statement.setString(1, payload);
					result_set = prepared_statement.executeQuery();

					// Check MySql Result
					if (!result_set.next()) { // ResultSet is empty
						System.out.println("[OrderedEnter|INFO]: ResultSet is empty - didnt not find the orderId: "
								+ payload);

					} else {
						System.out.println("[OrderedEnter|INFO]:ResultSet is not empty " + payload + " was found");
						String parkNameExtracted = result_set.getString(1);
						String visitor_number_extracted =result_set.getString(2);
						System.out.println("[OrderedEnter|DEBUG]:extacted park name: " + parkNameExtracted);
						db_table = "parks";

						// prepare MySQL query
						PreparedStatement preparedStatement = db_con.prepareStatement(
								"UPDATE " + db_table + " SET currentVisitors = currentVisitors + ? WHERE parkName=?;");
						preparedStatement.setInt(1, Integer.parseInt(visitor_number_extracted));
						preparedStatement.setString(2, parkNameExtracted);
						int rowsAffected = preparedStatement.executeUpdate();
						if (rowsAffected > 0) {
							System.out
									.println("[OrderedEnter|INFO]: updated parks, rows effected: " + rowsAffected);
							ordered_enterance_test_succeeded = true;

						} else {
							System.out.println(
									"[OrderedEnter|ERROR]:failed to update parks, rows effected: " + rowsAffected);
						}
					}

					// Catch problematic Payload
				} catch (ClassCastException e_clas) {
					System.out.println(
							"[OrderedEnter | ERROR]: Client sent payload for OrderedEnter ep which is not an String");
					// ORENB_TODO: send client an error that he sent bad msg (not arrlist)
					return;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				// Response to client
				try {
					send_response(client, new String("OrderedEnter"), new String("Boolean"), ordered_enterance_test_succeeded);
				} catch (IOException e) {
					System.out.println("[OrderedEnter |ERROR ]: Failed sending message to client");
					e.printStackTrace();
				}

			} else {
				// Client asked GroupGuideCheck end-point but sent bad payload-type
				try {
					send_response(client, new String("OrderedEnter"), new String("ErrorString"),
							new String("Client asked OrderedEnter end point but payload-type was not String!"));
				} catch (IOException e) {
					System.out.println("[OrderedEnter_ep |ERROR ]: Failed sending ErrorString to client");
					e.printStackTrace();
				}
			}

			return;
			
		case "SurpriseEnter":
			System.out.println("[SurpriseEnter|INFO]: SurpriseEnter enpoint trigered");
			// TODO - maayan should implement a function which returns if suprise visit is possible
			return;
			
			
			
		

		// ---------------------- DEFUALT CASE ---------------------------
		default:
			System.out.println("[handleMessageFromClient|info]: default enpoint");
		}
		return;

	}

	/**
	 * method that checks db that order is valid
	 * 
	 * @param arr - array list of string containing order parameters
	 * @return boolean - order is valid
	 */
	private boolean checkOrderTime(ArrayList<String> arr) {
		try {
			System.out.println("Debug: " + arr);
			int visitorNum = Integer.valueOf(arr.get(3));
			// get parks time of visit, and capacity of park
			PreparedStatement ps = db_con
					.prepareStatement("SELECT visitTimeInMinutes, capacity, diff FROM parks WHERE parkName = ?");
			ps.setString(1, arr.get(1));
			ResultSet rs = ps.executeQuery();
			System.out.println("Debug: " + rs);
			if (!rs.next()) {
				System.out.println("Debug: couldnt find park?");
				throw new IllegalArgumentException("invalid park name");
			}
			// found park
			int timeToAdd = rs.getInt("visitTimeInMinutes");
			int parkCapacity = rs.getInt("capacity");
			int capacityDiff = rs.getInt("diff");
			int actual = parkCapacity - capacityDiff;
			System.out.println("Debug: actual capacity is: " + actual);

			// get amount of orders in timeframe
			String startTime = arr.get(2);
			ps = db_con.prepareStatement(
					"SELECT SUM(visitor_number) AS sum FROM orders WHERE time_of_visit BETWEEN ? AND ?");
			ps.setString(1, startTime);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime endTime = LocalDateTime.parse(startTime, f);
			endTime = endTime.plusMinutes(timeToAdd);
			String endTimeFormatted = endTime.format(f);
			ps.setString(2, endTimeFormatted);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("couldnt sum?");
				throw new Exception("couldnt sum");
			}
			int sum = rs.getInt("sum");
			System.out.println("Debug: sum came to " + sum);
			int availableSpace = actual - sum;
			return ((availableSpace - visitorNum) > 0);

		} catch (Exception e) {
			System.out.println("error in checkOrderTime(): ");
			e.printStackTrace();
		}
		return false;
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
		// controller.addClient(client);
		return;
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) { // supposed to be called when client
																				// disconnects...
		System.out.println("hiiii client disconnected");
//		// controller.removeClient(client);
//		String username_to_loggout = null;
//		System.out.println("[clientConnected|DEBUG]:logged-in map before loggingout" + logged_in_clients);
//		for ( Entry<String, ConnectionToClient>  logged_in_clients : logged_in_clients.entrySet()) {
//			String username = logged_in_clients.getKey();
//            ConnectionToClient client_of_username = logged_in_clients.getValue();
//			if (client_of_username.equals(client)) {
//				username_to_loggout = username;
//				break;
//			}
//		}
//		logged_in_clients.remove(username_to_loggout);
//		System.out.println("[clientConnected|INFO]: removing client logged-in map");
//		
//		System.out.println("[clientConnected|DEBUG]:logged-in map" + logged_in_clients);

		return;
	}
}