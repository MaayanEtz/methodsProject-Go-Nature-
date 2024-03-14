package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import entity.Order;
import entity.Park;

import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	public static Order order;
	public static Park park;
	public static ArrayList<String> dataFromServer;
	public static boolean result = false;
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;
		
		if (msg instanceof ArrayList) {
			
			Boolean pay_load_from_srv_bln;
			ArrayList<String> pay_load_from_srv_arr_lst;
			
			ArrayList<Object> arr = (ArrayList<Object>) msg;
			String endpoint_from_server = (String) arr.get(0);
			String paylod_type_from_server = (String) arr.get(1);
			
			//ERROR CASE
			switch (paylod_type_from_server) {
			case "String":
				String pay_load_from_srv_str = (String) arr.get(2);
				result = false;
				return;}

			switch (endpoint_from_server) {
				case "ConnectToServer": {
					switch (paylod_type_from_server) {
					case "String":
						String pay_load_from_srv_str = (String) arr.get(2);
						result = false;
						break;
					case "Boolean":
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "Succesfull connection", "Failed connection to server");
					}
					
					break;}
				
				case "UserLogin": {
					pay_load_from_srv_bln = (Boolean) arr.get(2);
					caseDecision(pay_load_from_srv_bln, "User exists", "User not exists");
					break;}
				
				case "OrderFind": {
					pay_load_from_srv_bln = (Boolean) arr.get(2);
					caseDecision(pay_load_from_srv_bln, "Order found in DB", "Order not found id DB");
					break;}
				
				case "OrderUpdate": {
					pay_load_from_srv_bln = (Boolean) arr.get(2);
					caseDecision(pay_load_from_srv_bln, "Order updated in DB", "Order not updated in DB");
					break;}
				
				case "OrderGet": {
					pay_load_from_srv_arr_lst = (ArrayList<String>) arr.get(2);
					order = new Order(pay_load_from_srv_arr_lst);
					break;}
				
				case "OrderCreate": {
					pay_load_from_srv_bln = (Boolean) arr.get(2);
					caseDecision(pay_load_from_srv_bln, "Order created", "Order not created");
					break;}
				
				case "GroupGuideCheck": {
					pay_load_from_srv_bln = (Boolean) arr.get(2);
					caseDecision(pay_load_from_srv_bln, "The visitor is group guide", "The visitor is not group guide");
					break;}
				
				case "ParksListGet": {
					pay_load_from_srv_arr_lst = (ArrayList<String>) arr.get(2);
					dataFromServer = new ArrayList<String>(pay_load_from_srv_arr_lst);
					break;}
			}
			
		}else
			System.out.println("Msg is not Boolean or Array in ChatClient: handleMessageFromServer");

	}
	
	//private method for endpoint_from_server Switch-Case
	private void caseDecision(Boolean load, String trueResult, String falseResult) {
		if (load) {
			System.out.println(trueResult);
			result = true;
		}
		else {
			System.out.println(falseResult);
			result = false;
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
