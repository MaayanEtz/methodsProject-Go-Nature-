package gui;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePageController {

	// labels
	@FXML
	private Label lblWellcome;

	// Image
	@FXML
	private ImageView imgGoNature;
	@FXML
	private ImageView imgFindOrder;

	// buttons
	@FXML
	private Button btnConnectToServer = null;
	@FXML
	private Button btnExit = null;

	// text fields
	@FXML
	private TextField txtIpAddress;
	@FXML
	private TextField txtPortNumber;

	@FXML
	private Text errorTxt;

	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
			Scene scene = new Scene(root);


			primaryStage.setTitle("Home Page");
			primaryStage.setScene(scene);

			primaryStage.show();

		} catch (Exception e) {
			System.out.println("Error in HomePageController: start");
			System.out.println(e.getMessage());
		}
	}


	// Event for "connect" button
	public void connectToServer(ActionEvent event) throws Exception {
		String address = txtIpAddress.getText(), portNum = txtPortNumber.getText();

		if (address.trim().isEmpty() || portNum.trim().isEmpty()) {
			System.out.println("You must enter ip address and port number");
			errorTxt.setText("you must enter ip address and port number in order to connect to server");
			txtIpAddress.setText("");
			txtPortNumber.setText("");
		} else {
			try {
			ClientUI.chat = new ClientController(address, Integer.valueOf(portNum));
			//ClientUI.chat.accept("connect");
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("ConnectToServer"));
			arrmsg.add(new String("String"));
			arrmsg.add(new String("Conect"));
			ClientUI.chat.accept(arrmsg);
			} catch (Exception e) {
				System.out.println("you must enter valid ip and port numbers");
				errorTxt.setText("you must enter valid ip address and valid port number in order to connect to server");
				txtIpAddress.setText("");
				txtPortNumber.setText("");
				return;
			}
			if (ChatClient.result) {
				ChatClient.result = false;
				FXMLLoader loader = new FXMLLoader();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
				Scene scene = new Scene(root);
				primaryStage.setTitle("Find Order");
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				System.out.println("couldnt connect to server");
			}
		}

	}

	// Event for "Exit" button
	public void pressExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Home Page");
		System.exit(0);
	}

}
