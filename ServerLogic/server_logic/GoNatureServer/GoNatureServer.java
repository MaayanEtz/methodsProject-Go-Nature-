package GoNatureServer;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
			controller.removeClient(client);
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
					if (checkOrderTime(payload)) {
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
					send_response(client, new String("UserLogin"), new String("ErrorString"),
							new String("Client asked UserLogin end point but payload-type was not ArrayList<String>!"));
				} catch (IOException e) {
					System.out.println("[UserLogin_ep |ERROR]: Failed UserLogin error response");
					e.printStackTrace();
				}

			}
			return;

		case "OrderCancel":
			if (payload_type.equals("String")) {
				boolean cancel_order_test_succeeded = false;
				db_table = "orders";
				try {
					// Extract pay-load
					String payload = (String) arr_msg.get(2);
					System.out.println("[OrderCancel | DEBUG]: extracted: " + payload);

					cancel_order_test_succeeded = cancelOrder(Integer.valueOf(payload));

					// Response to client
					send_response(client, new String("OrderCancel"), new String("Boolean"),
							new Boolean(cancel_order_test_succeeded));
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
				} catch (IOException e_io) {
					System.out.println("[OrderCreate_ep |ERROR ]: Failed OrderCreate response");
					e_io.printStackTrace();
					error = e_io.getMessage();
				} catch (Exception e) {
					e.printStackTrace();
					error = e.getMessage();
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

		case "OrderEdit":
			// UPDATE orders SET park_name="yossi_park" WHERE orderId="4";
			return;

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
				arr.add(new String("" + rs.getInt("orderId")));
				arr.add(new String(rs.getString("park_name")));
				arr.add(new String(rs.getString("time_of_visit")));
				arr.add(new String("" + rs.getInt("visitor_number")));
				arr.add(new String(rs.getString("visitor_email")));
				arr.add(new String(rs.getString("visitor_phone")));
				// send Order to Client
				send_response(client, new String("OrderGet"), new String("ArrayList<String>"), arr);
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
			return;

		/*
		 * case "GetAvailableTable": //payload = ArrayList<String> = {orderTime,
		 * visitorNumber} if (!payload_type.equals("ArrayList<String>")) { System.out.
		 * println("[GetAvailableTable | ERROR]: Client sent payload for GetAvailableTable ep which is not an ArrayList<String>"
		 * ); try { // send error to client send_response(client, new
		 * String("GetAvailableTable"), new String("ErrorString"), new
		 * String("Client asked GetAvailableTable end point but payload-type was not ArrayList<String>!"
		 * )); } catch (IOException e) { System.out.
		 * println("[GetAvailableTable_ep |ERROR]: Failed GetAvailableTable error response"
		 * ); e.printStackTrace(); } return; } ArrayList<String> payload =
		 * (ArrayList<String>) arr_msg.get(2); DateTimeFormatter f =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); LocalDateTime orderTime =
		 * LocalDateTime.parse(payload.get(0), f); int visitorNumber =
		 * Integer.valueOf(payload.get(1)); //LocalDateTime testTime = TODO: need to
		 * think how to do that
		 */

		case "EnterWaitList":
			try {
				checkType(client, payload_type, "ArrayList<String>", endpoint);
			} catch (IOException e) {
				System.out.println("checkType failed in EnterWaitList");
				e.printStackTrace();
				return;
			}
			// get payload
			arr = (ArrayList<String>) arr_msg.get(2);
			// insert order in payload as waitlist
			try {
				PreparedStatement ps = db_con.prepareStatement(
						"INSERT INTO `orders`(`visitor_id`, `park_name`, `time_of_visit`,`visitor_number`,`visitor_email`, `visitor_phone`, `status`) VALUES (?,?, ?, ?, ?, ?, 'WaitList');");
				ps.setInt(1, Integer.valueOf(arr.get(0)));
				ps.setString(2, arr.get(1));
				ps.setString(3, arr.get(2));
				ps.setInt(4, Integer.valueOf(arr.get(3)));
				ps.setString(5, arr.get(4));
				ps.setString(6, arr.get(5));
				if (ps.executeUpdate() != 1) {
					System.out.println("[EnterWaitList | ERROR]: couldnt insert order to orders table");
					send_response(client, endpoint, new String("ErrorString"),
							new String("couldnt insert order into orders table as waitList"));
				} else {
					send_response(client, endpoint, "Boolean", new Boolean(true));
					System.out.println("[EnterWaitList | INFO]: sent client answear of true");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			System.out.println("[handleMessageFromClient|info]: default enpoint");
		}
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
			int visitorNum = Integer.valueOf(arr.get(4));
			// get parks time of visit, and capacity of park
			PreparedStatement ps = db_con
					.prepareStatement("SELECT visitTimeInMinutes, capacity, diff FROM parks WHERE parkName = ?");
			ps.setString(1, arr.get(2));
			ResultSet rs = ps.executeQuery();
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
			String startTime = arr.get(3);
			ps = db_con.prepareStatement(
					"SELECT SUM(visitor_number) AS sum FROM orders WHERE status = 'Active' AND time_of_visit BETWEEN ? AND ?");
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
			boolean validOrder = (availableSpace - visitorNum) > 0;
			LocalDateTime preStart = LocalDateTime.parse(startTime, f);
			preStart = preStart.minusMinutes(timeToAdd);
			String preStartFormatted = preStart.format(f);
			ps = db_con.prepareStatement(
					"SELECT SUM(visitor_number) AS sum FROM orders WHERE time_of_visit BETWEEN ? AND ?");
			ps.setString(1, preStartFormatted);
			ps.setString(2, startTime);
			rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("couldnt sum?");
				throw new Exception("couldnt sum");
			}
			sum = rs.getInt("sum");
			availableSpace = actual - sum;
			return validOrder && ((availableSpace - visitorNum) > 0);
		} catch (Exception e) {
			System.out.println("error in checkOrderTime(): ");
			e.printStackTrace();
		}
		return false;
	}

	private void checkType(ConnectionToClient client, String payloadType, String expected, String ep)
			throws IOException {
		if (!payloadType.equals(expected)) {
			System.out.println(
					String.format("[rp | ERROR]: Client sent payload for %s ep which is not a %s", ep, payloadType));
			try {
				// send error to client
				send_response(client, ep, new String("ErrorString"), new String(
						String.format("Client asked %s end point but payload-type was not %s!", ep, payloadType)));
			} catch (IOException e) {
				System.out.println(String.format("[%s |ERROR]: Failed %s error response", ep, ep));
				e.printStackTrace();
				throw e;
			}
			return;
		}
	}

	private ArrayList<ArrayList<String>> getWaitListedOrders(String orderTime, String parkName) {

		ArrayList<ArrayList<String>> arr = new ArrayList<>();
		// get capacity of park
		int capacity = getParkCapacity(parkName);
		int visitTime = getParkTime(parkName);
		// get start and end times affected by orderTime cancellation
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime order = LocalDateTime.parse(orderTime, f), start = order.minusMinutes(visitTime),
				end = order.plusMinutes(visitTime);

		try {
			PreparedStatement ps = db_con.prepareStatement(
					"SELECT orderId FROM orders WHERE status = 'WaitList' AND time_of_visit BETWEEN ? AND ?");
			ps.setString(1, start.format(f));
			ps.setString(2, end.format(f));
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {// no orders found
				System.out.println("[getWaitListedOrders | INFO]: no waitlisted orders found to check");
				return null;
			}
			do {// add all orders to array of orders
				int id = rs.getInt("orderId");
				arr.add(getOrderFromId(id));
			} while (rs.next());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arr;// return array of orders

	}

	private ArrayList<String> getOrderFromId(int orderId) {
		ArrayList<String> arr = new ArrayList<>();
		try {
			PreparedStatement ps = db_con.prepareStatement("SELECT * FROM orders WHERE orderId = ?");
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;
			arr.add(String.valueOf(orderId));
			arr.add(rs.getString("visitor_id"));
			arr.add(rs.getString("park_name"));
			arr.add(rs.getString("time_of_visit"));
			arr.add(String.valueOf(rs.getInt("visitor_number")));
			arr.add(rs.getString("visitor_email"));
			arr.add(rs.getString("visitor_phone"));
			arr.add(rs.getString("status"));
		} catch (SQLException e) {
			System.out.println("getOrderFromId: sql exception was thrown");
			e.printStackTrace();
		}
		return arr;
	}

	private int getParkCapacity(String parkName) {

		int capacity, diff;
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = db_con.prepareStatement("SELECT capacity, diff FROM parks WHERE parkName = ?");
			ps.setString(1, parkName);
			rs = ps.executeQuery();
			if (!rs.next())
				return -1;
			capacity = rs.getInt("capacity");
			diff = rs.getInt("diff");
			return capacity - diff;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	private int getParkTime(String parkName) {
		int time;
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = db_con.prepareStatement("SELECT visitTimeInMinutes FROM parks WHERE parkName = ?");
			ps.setString(1, parkName);
			rs = ps.executeQuery();
			if (!rs.next())
				return -1;
			time = rs.getInt("visitTimeInMinutes");
			return time;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public boolean cancelOrder(int orderId) throws SQLException {
		boolean result = false;
		PreparedStatement ps = db_con
				.prepareStatement("UPDATE `orders` SET `status` = 'Cancelled' WHERE `orderId` = ?");
		ps.setInt(1, orderId);
		int rowsAffected = ps.executeUpdate();

		// Cancellation result update check
		if (rowsAffected == 1) {
			result = true;
		}
		advanceWaitList(orderId);
		return result;
	}

	private void advanceWaitList(int orderId) {
		ArrayList<String> order = getOrderFromId(orderId);
		System.out.println(order);
		String parkName = order.get(2);
		String orderTime = order.get(3);
		ArrayList<ArrayList<String>> affectedOrders = getWaitListedOrders(orderTime, parkName);
		if (affectedOrders == null)
			return;// no need to advance waitlist
		for (ArrayList<String> current : affectedOrders) {
			if (checkOrderTime(current)) {
				openPopup(current);
			}
		}
	}

	private void openPopup(ArrayList<String> order) {
		System.out.println(String.format("SIMULATION! SENDING EMAIL TO %s and SMS to %s, spot opened to approve the reservation", order.get(5), order.get(6)));
		System.out.println("order notified id is: " + order.get(0));
		approveOrder(order.get(0));
		
	}

	private void approveOrder(String orderId) {
		try {
			PreparedStatement ps = db_con
					.prepareStatement("UPDATE `orders` SET `status` = 'Active' WHERE (`orderId` = ?);");
			ps.setString(1, orderId);
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected != 1)
			System.out.println("approveOrder didnt work??");
			System.out.println("assuming user has chosen to approve his waitlisted order, his order is now active");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		controller.addClient(client);
		return;
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) { // supposed to be called when client
																				// disconnects...
		System.out.println("[clientConnected|INFO]: removing client from gui list");
		controller.removeClient(client);
		return;
	}
}
