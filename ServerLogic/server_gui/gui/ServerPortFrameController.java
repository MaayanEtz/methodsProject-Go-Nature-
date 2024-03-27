package gui;

import server.ServerUI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import GoNatureServer.GoNatureServer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ocsf.server.ConnectionToClient;

public class ServerPortFrameController {

	public enum Columns {
		IP_ADDRESS, HOST_NAME, STATUS
	}

	// button
	@FXML
	private Button btnClose;
	@FXML
	private Button btnGetConUsers;

	// table
	@FXML
	private TableView<ConnectionData> tblConStatus;
	@FXML
	private TableColumn<ConnectionData, String> colIPAddress;
	@FXML
	private TableColumn<ConnectionData, String> colHostName;
	@FXML
	private TableColumn<ConnectionData, String> colStatus;
	@FXML
	private Text Ip_text;
	
	private ServerPortFrameController me;
	

	private static ArrayList<ConnectionToClient> client_conn_data = new ArrayList<>();

	public void loadTableData(ObservableList<ConnectionData> connectionData) {
		try {
			colIPAddress.setCellValueFactory(cellData -> cellData.getValue().ipAddressProperty());
			colHostName.setCellValueFactory(cellData -> cellData.getValue().hostNameProperty());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			tblConStatus.setItems(connectionData);

		} catch (Exception e) {
			System.out.println("Error in ServerPortFrameController: loadTableData");
			System.out.println(e.getMessage());
		}
	}



	// Event for "Close Server" button
	public void pressCloseBtn(ActionEvent event) {
		System.out.println("Exit server frame");
		System.exit(0);
	}

	// Event for "Get connected users" button
	public void pressGetConUsersBtn(ActionEvent event) {
		try {
			updateTable();
		} catch (Exception e) {
			System.out.println("Error in ServerPortFrameController: pressGetConUsersBtn");
			System.out.println(e.getMessage());
		}

	}
	// End function
	
	public void addClient(ConnectionToClient client) {
		client_conn_data.add(client);
		updateTable();
	}
	
	public void removeClient (ConnectionToClient client) {
		client_conn_data.remove(client);
		updateTable();
	}
	
	public void updateTable() {
		String ipAddress;
		String hostName;
		String connectionStatus;
		
		ObservableList<ConnectionData> connectionData = FXCollections.observableArrayList();
		if (!client_conn_data.isEmpty()) {
			for (ConnectionToClient c : client_conn_data) {
				if (c.isAlive()) {
					ipAddress = new String(c.getInetAddress().getHostAddress().toString());
					hostName = new String(c.getInetAddress().getHostName().toString());
					connectionStatus = new String(String.valueOf(c.isAlive()));

					System.out.println("Client ip: " + c.getInetAddress().getHostAddress().toString());
					System.out.println("Client host name: " + c.getInetAddress().getHostName().toString());
					System.out.println("Client stats: " + String.valueOf(c.isAlive()));

					connectionData.add(new ConnectionData(ipAddress, hostName, connectionStatus));
				} else {
					System.out.println("c is dead");
				}
			}
		}
		this.loadTableData(connectionData);
	}

	// private class for connected clients data
	private class ConnectionData {
		private SimpleStringProperty ipAddress;
		private SimpleStringProperty hostName;
		private SimpleStringProperty status;

		public ConnectionData(String ipAddress, String hostName, String status) {
			this.ipAddress = new SimpleStringProperty(ipAddress);
			this.hostName = new SimpleStringProperty(hostName);
			this.status = new SimpleStringProperty(status);
		}

		public SimpleStringProperty ipAddressProperty() {
			return ipAddress;
		}

		public SimpleStringProperty hostNameProperty() {
			return hostName;
		}

		public SimpleStringProperty statusProperty() {
			return status;
		}
	}
	public void setIp() {
		try {
			Ip_text.setText(String.valueOf(InetAddress.getLocalHost()).split("/")[1]);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}
	


	
}// END CLASS
