package gui;

import java.util.ArrayList;
import java.util.Arrays;

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
			// Oren Testing
			//String username =  orderNumber.split(",")[0];
			//String password =  orderNumber.split(",")[1];
			String visitor_id = orderNumber.split(",")[0];
			String park_name = orderNumber.split(",")[1];
			String time_of_visit = orderNumber.split(",")[2];
			String visitor_number = orderNumber.split(",")[3];
			String visitor_email = orderNumber.split(",")[4];
			String visitor_phone = orderNumber.split(",")[5];
			
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("OrderCreate"));
			arrmsg.add(new String("ArrayList<String>"));
			arrmsg.add(new ArrayList<String>(Arrays.asList(visitor_id,park_name,time_of_visit,visitor_number,visitor_email,visitor_phone)));
			ClientUI.chat.accept(arrmsg);
			
//			if(orderNumber.trim().isEmpty()) {
//				System.out.println("You must enter an order number");
//				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//				Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
//				FindOrderFrameController findOrderFrameController = loader.getController();			
//				findOrderFrameController.loadResul("You must enter an order number");
//				Scene scene = new Scene(root);
//				
//				primaryStage.setTitle("Find Order");
//				primaryStage.setScene(scene);		
//				primaryStage.show();
//			}
//			else {
//				//send order number for searching
//				ClientUI.chat.get(orderNumber);
//				
//				if(ChatClient.order == null) {
//					System.out.println("Order number not found");
//					
//					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window	
//					Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
//					FindOrderFrameController findOrderFrameController = loader.getController();			
//					findOrderFrameController.loadResul("Order number does not exist in the system.");
//					Scene scene = new Scene(root);
//					primaryStage.setTitle("Find Order");
//					primaryStage.setScene(scene);		
//					primaryStage.show();
//				}
//				else {	
//					System.out.println("Order number found");
//					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//					
//					Pane root = loader.load(getClass().getResource("/gui/OrderForm.fxml").openStream());
//					OrderFrameController orderFrameController = loader.getController();
//					orderFrameController.loadOrder(ChatClient.order);
//					Scene scene = new Scene(root);
//
//					primaryStage.setTitle("Order Form");	
//					primaryStage.setScene(scene);		
//					primaryStage.show();
//				}
//			}		
		}catch (Exception e) {
			e.printStackTrace();
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
