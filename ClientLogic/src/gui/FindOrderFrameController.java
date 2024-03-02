package gui;

import client.ChatClient;
import client.ClientUI;
import entity.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FindOrderFrameController {
	
	//labels
	@FXML
	private Label lblEnteredOrderNum;
	@FXML
	private Label lblResult;
		
	//text fields
	@FXML
	private TextField txtEnteredOrderNum;
	
	//buttons
	@FXML
	private Button btnFind=null;
	@FXML
	private Button btnBack=null;
	
	//Image
	@FXML
	private ImageView imgGoNature;
	
	//get entered order number
	private String getEnteredOrderNum() {
		return txtEnteredOrderNum.getText();
	}
	
	//set text to Result label
	public void loadResul (String str) {
		try {
			this.lblResult.setText(str);
		}
		catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: loadLabel");
			System.out.println(e.getMessage());
		}	
	}
	

	
	//Event for "Find" button
	public void pressFindBtn (ActionEvent event) throws Exception {
		try {
			String orderNumber;
			
			orderNumber = getEnteredOrderNum();
			
			//prepare the window to show the result
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			
			if(orderNumber.trim().isEmpty()) {
				System.out.println("You must enter an order number");
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
				FindOrderFrameController findOrderFrameController = loader.getController();			
				findOrderFrameController.loadResul("You must enter an order number");
				Scene scene = new Scene(root);
				
				primaryStage.setTitle("Find Order");
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
			else {
				//send order number for searching
				ClientUI.chat.get(orderNumber);
				
				if(ChatClient.order == null) {
					System.out.println("Order number not found");
					
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window	
					Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
					FindOrderFrameController findOrderFrameController = loader.getController();			
					findOrderFrameController.loadResul("Order number does not exist in the system.");
					Scene scene = new Scene(root);
					primaryStage.setTitle("Find Order");
					primaryStage.setScene(scene);		
					primaryStage.show();
				}
				else {	
					System.out.println("Order number found");
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					
					Pane root = loader.load(getClass().getResource("/gui/OrderForm.fxml").openStream());
					OrderFrameController orderFrameController = loader.getController();
					orderFrameController.loadOrder(ChatClient.order);
					Scene scene = new Scene(root);

					primaryStage.setTitle("Order Form");	
					primaryStage.setScene(scene);		
					primaryStage.show();
				}
			}		
		}catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: pressFindBtn");
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}

	}
	
	//Even for "Back" button
	public void pressBackBtn(ActionEvent event) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
			
			HomePageController homePageController = new HomePageController();
			loader.setController(homePageController);
			homePageController.start(primaryStage);
			
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("Home Page");

			primaryStage.setScene(scene);		
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: pressBackBtn");
			System.out.println(e.getMessage());
		}
		
	}

}
