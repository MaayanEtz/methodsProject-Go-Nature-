package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.ServerUI;

public class HomePageController {

	@FXML
	private TextField port_id;

	@FXML
	private Button btnOpen;

	@FXML
	private Button btnExit;

	@FXML
	private Text errorTxt;

	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setTitle("Server Home Page");
			primaryStage.setScene(scene);

			primaryStage.show();

		} catch (Exception e) {
			System.out.println("Error in HomePageController: start");
			System.out.println(e.getMessage());
		}
	}

	@FXML
	// Event for "Open Sever" button
	public void openServer(ActionEvent event) {
		String portNumber = port_id.getText();

		if (portNumber.trim().isEmpty()
				|| (Integer.valueOf(portNumber.trim()) < 1024 || Integer.valueOf(portNumber.trim()) > 49151)) {
			System.out.println("you have to enter valid port number");
			errorTxt.setText("you have to enter valid port number!");
			port_id.setText("");
		} else {
			ServerUI.runServer(portNumber);
			try {
				FXMLLoader loader = new FXMLLoader();

				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/ServerFrame.fxml").openStream());

				Scene scene = new Scene(root);

				ServerPortFrameController controller = loader.getController();
				controller.setIp();
				primaryStage.setTitle("Connection status");
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (Exception e) {
				System.out.println("Error in HomePageController: pressOpenBtn");
				System.out.println(e.getMessage());
			}
		}
	}

	// Event for "Exit" button
	public void pressExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Home Page");
		System.exit(0);
	}

}
