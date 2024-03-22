package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import entity.Order;

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
	public static String visitorID;
	public static Order order;
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
		try {
			System.out.println("--> handleMessageFromServer");
			awaitResponse = false;
			
			if (msg instanceof ArrayList) {
				
				Boolean pay_load_from_srv_bln;
				ArrayList<String> pay_load_from_srv_arr_lst;
				String pay_load_from_srv_str;
				
				ArrayList<Object> arr = (ArrayList<Object>) msg;
				String endpoint_from_server = (String) arr.get(0);
				String paylod_type_from_server = (String) arr.get(1);
				
				switch (endpoint_from_server) {
					case "ConnectToServer": {
						switch (paylod_type_from_server) {
						case "String":
							pay_load_from_srv_str = (String) arr.get(2);
							result = false;
							break;
						case "Boolean":
							pay_load_from_srv_bln = (Boolean) arr.get(2);
							caseDecision(pay_load_from_srv_bln, "Succesfull connection", "Failed connection to server");
						}
						
						break;}
					
					case "UserLogin": {
						pay_load_from_srv_arr_lst = (ArrayList<String>) arr.get(2);
						dataFromServer = pay_load_from_srv_arr_lst;
						break;}
					
					case "OrderUpdate": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "Order updated in DB", "Order not updated in DB");
						break;}
					
					case "OrderCancel": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "Order cancelled", "Order not cancelled");
						break;}

					case "OrderGet": {	
						switch (paylod_type_from_server) {
						case "String":
							System.out.println("DB didn't return the order");
							pay_load_from_srv_str = (String) arr.get(2);
							result = false;
							break;
						case "ArrayList<String>":
							System.out.println("DB returned the order");
							result = true;
							pay_load_from_srv_arr_lst = (ArrayList<String>) arr.get(2);
							System.out.println(pay_load_from_srv_arr_lst.toString());
							order = new Order(pay_load_from_srv_arr_lst);
						}
						break;}
					
					case "OrderCreate": {
						switch (paylod_type_from_server) {
							case "String": {
								pay_load_from_srv_str = (String) arr.get(2);
								result = false;
								break;
							}
							case "Integer": {
								pay_load_from_srv_str = ((Integer) arr.get(2)).toString();
								dataFromServer = new ArrayList<>();
								dataFromServer.add(pay_load_from_srv_str);
								result = true;
								System.out.println("Order created");
								break;
							}
						}
						break;}
					
					case "GroupGuideCheck": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The visitor is group guide", "The visitor is not group guide");
						break;}
					
					case "ParksListGet": {
						pay_load_from_srv_arr_lst = (ArrayList<String>) arr.get(2);
						dataFromServer = pay_load_from_srv_arr_lst;
						break;}
					
					case "ParkCheckCapacity": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "Park capacity allows to order", "Park capacity doesn't allow to order");
						break;}
					
					case "OrderedEnter": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The planned visitors succesfully entered the park", "The planned visitors not entered the park");
						break;}
					
					case "UnplannedEnter": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The unplanned visitors succesfully entered the park", "The unplanned visitors not entered the park");
						break;}
					
					case "GuideRegistration": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The guide has been successfully registered", "The guide is not registered");
						break;}
					
					case "IsLoggedIn": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The user is logged in", "The user is not logged in");
						break;}
					
					case "UserLogOut": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The user logged out", "The user wasn't logged out");
						break;}
					
					case "ExitRegistration": {
						pay_load_from_srv_bln = (Boolean) arr.get(2);
						caseDecision(pay_load_from_srv_bln, "The exit registration performed", "The exit registration is not performed");
						break;}
					

				}
				
			}else
				System.out.println("Msg is not Boolean or Array in ChatClient: handleMessageFromServer");
		} catch (Exception e) {
			System.out.println("Error in ChatClient: handleMessageFromServer.");
			System.out.println(e.getMessage());
		}

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
