package GoNatureServer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ocsf.server.ConnectionToClient;

public class OrderNotificationThread extends Thread {
	private GoNatureServer server;
	private Connection db_con;
	private Map<String, Timer> orderTimers;
	private int orderExpirationMilis;

	public OrderNotificationThread(GoNatureServer server, Connection db_con) {
		this.server = server;
		this.db_con = db_con;
		this.orderTimers = new HashMap<>();
		orderExpirationMilis = 1 * 60 * 1000; // 2 hours in milisecs
	}

	@Override
	public void run() {
		int repeat_milisecs = 15000;
		System.out.println("[OrderNotificationThread | INFO ]: Starting OrderNotificationThread");
		while (true) { // Run indefinitely, periodically checking for new orders
			try {

				System.out.println(
						"[OrderNotificationThread | INFO ]: SMS simulation mechanism started: will rerun every "
								+ repeat_milisecs / 1000 + " seconds");
				
				///////// CODE FOR 2 Hours
//				LocalDateTime twoHoursLater = LocalDateTime.now().plusHours(24);
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				String formattedCurrentTime = LocalDateTime.now().format(formatter);
//				String formattedTwoHoursLater = twoHoursLater.format(formatter);
//
//				PreparedStatement statement = db_con.prepareStatement(
//						"SELECT orderID, time_of_visit FROM orders WHERE time_of_visit >= ? AND time_of_visit <= ? AND reminderMsgSend = ?");
//				statement.setString(1, formattedCurrentTime); // Start time: current time formatted
//				statement.setString(2, formattedTwoHoursLater); // End time: current time plus 2 hours formatted
//				statement.setBoolean(3, false);
//				ResultSet resultSet = statement.executeQuery();

				// Extract orders of tomorrow
	            LocalDateTime oneDayLater = LocalDateTime.now().plusDays(1); // Get time 1 day from now
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCurrentTime = LocalDateTime.now().format(formatter);
                String formattedOneDayLater = oneDayLater.format(formatter);

                PreparedStatement statement = db_con
                        .prepareStatement("SELECT orderID, time_of_visit FROM orders WHERE time_of_visit >= ? AND time_of_visit <= ? AND reminderMsgSend = ?");
                statement.setString(1, formattedCurrentTime); // Start time: current time formatted
                statement.setString(2, formattedOneDayLater); // End time: current time plus 1 day formatted
                statement.setBoolean(3, false);
                ResultSet resultSet = statement.executeQuery();
				
				
				// Iterate through the result set
				while (resultSet.next()) {

					String orderId = resultSet.getString("orderID");
					String timeOfVisit = resultSet.getString("time_of_visit");

					System.out.println("[OrderNotificationThread | INFO ]: pulled data from orders, orderID: " + orderId
							+ " and time: " + timeOfVisit
							+ " Please make sure these orders were created by running clients!!");

					// Check if the orderId exists in the clients_with_orders map
					if (server.clients_with_orders.containsKey(orderId)) {
						// Get the corresponding client connection
						System.out.println("[OrderNotificationThread | INFO ]: client referenced with orderID: "
								+ orderId + " was located");
						ConnectionToClient client = server.clients_with_orders.get(orderId); // pull client of the
																								// relevant order
						System.out.println("extracted client:" + client);
						// Send a message to the client
						String message = "!!!!SMS: Your visit is scheduled for " + timeOfVisit + ".!!!!";

						try {
							server.send_response(client, new String("SMS_OrderReminder"), new String("String"), message);
							System.out.println("[OrderNotificationThread | INFO ]: send message to client");

							// Now update the orders table that message was sent to the client:
							statement = db_con
									.prepareStatement("UPDATE orders SET reminderMsgSend = true WHERE orderId = ?");
							statement.setInt(1, Integer.valueOf(orderId));
							statement.executeUpdate();

							// Start the timer for this order
							startTimer(orderId, client);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

				System.out
						.println("[OrderNotificationThread | INFO ]: Another SMS cycle done, work will never end :( ");
				// Close resources
				resultSet.close();
				statement.close();

				// Sleep for some time before checking again (e.g., every hour)
				// Thread.sleep(7200000); // Sleep for 2 hour - every 2H
				Thread.sleep(repeat_milisecs); // sleep 15 seconds and try sending SMSs again
			} catch (SQLException | InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private boolean orderCancel(String orderID) {
		boolean cancel_order_test_succeeded = false;
		String db_table = "orders";
		try {
			cancel_order_test_succeeded = server.cancelOrder(Integer.valueOf(orderID));
		} catch (SQLException e_sql) {
			System.out.println("[OrderCreate | ERROR]: MySQL query execution error");
			e_sql.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cancel_order_test_succeeded;
	}

	private void startTimer(String orderId, ConnectionToClient client) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Timer task to execute when timer expires
				System.out.println("[OrderNotificationThread | INFO ]: Timer expired for order: " + orderId);
				/// ORENB : Here code for checking if ordered was approve, if no - cancel order
				PreparedStatement ps;
				try {
					// Check if visitor confirmed order
					ps = db_con.prepareStatement("SELECT visitorConfirmedOrder FROM orders WHERE orderId = ?;");
					ps.setInt(1, Integer.valueOf(orderId));
					ResultSet resultSet = ps.executeQuery();
					if (!resultSet.next()) { // ResultSet is empty
						System.out.println(
								"[ startTimer| ERROR ]: ResultSet is empty - didnt not find the orderId: " + orderId);

					} else {
						System.out.println("[startTimer|INFO]:ResultSet is not empty " + orderId + " was found");
						
						// Delete order if user did not confirm
						if (!resultSet.getBoolean("visitorConfirmedOrder")) {
							if (orderCancel(orderId)) {
								System.out.println("[startTimer | INFO]: OrderID " + orderId + " was canceled successfuly");
								// Send message to client
								String message = "!!!!SMS: order: " + orderId
										+ " was canceled - you did not approve it !!!!";
								server.send_response(client, new String("SMS_OrderCanceled"), new String("String"),
										message);
								System.out.println("[OrderNotificationThread | INFO ]: send message to client");

							} else {
								System.out.println("[startTimer | ERROR]: FAILED OrderID " + orderId + " cancel");
							}
						}
						else {
							System.out.println("[startTimer | INFO]: User approved order: " + orderId + " order is not canceled after timer");

						}
						
						
					}

				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Remove the timer from the map
				orderTimers.remove(orderId);
			}
		}, orderExpirationMilis); // 2 hours delay before the timer expires 2 * 60 * 60 * 1000
		orderTimers.put(orderId, timer); // Store the timer in the map
	}
}
