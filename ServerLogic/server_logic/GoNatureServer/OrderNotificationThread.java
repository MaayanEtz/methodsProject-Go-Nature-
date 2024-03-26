package GoNatureServer;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import ocsf.server.ConnectionToClient;

public class OrderNotificationThread extends Thread {
    private GoNatureServer server;
    private Connection db_con;

    public OrderNotificationThread(GoNatureServer server, Connection db_con) {
        this.server = server;
        this.db_con = db_con;
    }

    @Override
    public void run() {
    	int repeat_milisecs = 15000;
    	System.out.println("[OrderNotificationThread | INFO ]:Starting OrderNotificationThread");
        while (true) { // Run indefinitely, periodically checking for new orders
            try {
            	
            	System.out.println("[OrderNotificationThread | INFO ]: SMS simulation mechanism started: will rerun every " + repeat_milisecs/1000 + " seconds");
            	LocalDateTime twoHoursLater = LocalDateTime.now().plusHours(2);
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            	String formattedCurrentTime = LocalDateTime.now().format(formatter);
            	String formattedTwoHoursLater = twoHoursLater.format(formatter);

            	PreparedStatement statement = db_con.prepareStatement("SELECT orderID, time_of_visit FROM orders WHERE time_of_visit >= ? AND time_of_visit <= ? AND reminderMsgSend = ?");
            	statement.setString(1, formattedCurrentTime); // Start time: current time formatted
            	statement.setString(2, formattedTwoHoursLater); // End time: current time plus 2 hours formatted
            	statement.setBoolean(3, false);
            	ResultSet resultSet = statement.executeQuery();



                // Iterate through the result set
                while (resultSet.next()) {
                	
                    String orderId = resultSet.getString("orderID");
                    String timeOfVisit = resultSet.getString("time_of_visit");
                    
                    System.out.println("[OrderNotificationThread | INFO ]pulled data from orders, orderID: "
                    		+ "" + orderId + " and time: " + timeOfVisit +" Please make sure these orders were created by running clients!!"
                    		);
                    
                    // Check if the orderId exists in the logged_in_clients map
                    if (server.clients_with_orders.containsKey(orderId)) {
                        // Get the corresponding client connection
                    	System.out.println("[OrderNotificationThread | INFO ]: client referenced with orderID: " + orderId + " was located");
                        ConnectionToClient client = server.clients_with_orders.get(orderId); // pull client of the relevant order
                        System.out.println("extracted client:" + client);
                        // Send a message to the client
                        String message = "!!!!SMS:Your visit is scheduled for " + timeOfVisit + ".!!!!";

						try {
							server.send_response(client, new String("SMS_SIMULATOR"), new String("String"), message);
							System.out.println("[OrderNotificationThread | INFO ]send message to client");
							
							// Now update the orders table that message was sent to the client:
							PreparedStatement preparedStatement = db_con.prepareStatement("UPDATE orders SET reminderMsgSend = true WHERE orderId = ?");
							preparedStatement.setInt(1, Integer.valueOf(orderId));
							preparedStatement.executeUpdate();

							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                    }
                }
                
                System.out.println("[OrderNotificationThread | INFO ]: Another SMS cycle done, work will never end :( ");
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
}
