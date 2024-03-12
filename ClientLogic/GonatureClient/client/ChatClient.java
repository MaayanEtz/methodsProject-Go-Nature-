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
	public static Order order = new Order(null, null, null, null, null, null);
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

		System.out.println(msg);
		if (msg instanceof Boolean) {
			System.out.println(msg);
			Boolean booleanMsg = (Boolean) msg;
			if (booleanMsg) {
				// case order updated successfully in DB
				System.out.println("Order updated successfully in DB");
				result = true;
				order = null;
			} else {
				// case order was not updated in DB
				System.out.println("Order was not updated in DB");
				order = null;
			}
		} else if (msg instanceof ArrayList) {
//			ArrayList<String> arr = (ArrayList<String>) msg;
//			System.out.println(arr);
//			System.out.println(arr.isEmpty());
//			// if no such order number in DB -> set order null
//			if (arr.isEmpty())
//				order = null;
//			else {
//				order = new Order(arr);
//			}
			ArrayList<Object> arr = (ArrayList<Object>) msg;
			String endpoint_from_server = (String) arr.get(0);
			String paylod_type_from_server = (String) arr.get(1);
			switch (endpoint_from_server) {
			case "ConnectToServer":
				switch (paylod_type_from_server) {
				case "String":
					String pay_load_from_srv_str = (String) arr.get(2);
					break;
				case "Boolean":
					Boolean pay_load_from_srv_bln = (Boolean) arr.get(2);
					if (pay_load_from_srv_bln) {
						System.out.println("Succesfull connection");
					}
					else {
						System.out.println("Failed connection to server");
					}
				}
			}
				
					
			
			String paylod = (String) arr.get(1);
		} else if (msg instanceof String) {
			result = true;
		} else
			System.out.println("Msg is not Boolean or Array in ChatClient: handleMessageFromServer");

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
