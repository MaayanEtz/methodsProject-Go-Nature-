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

public class OrderFrameController{
	
	private Order order;
	
	//labels
	@FXML
	private Label lblOrderNum;
	@FXML
	private Label lblParkName;
	@FXML
	private Label lblVisitTime;
	@FXML
	private Label lblVisitorsNum;
	@FXML
	private Label lblPhone;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblResult;
	
	//text fields
	@FXML
	private TextField txtOrderNum;
	@FXML
	private TextField txtParkName;
	@FXML
	private TextField txtVisitTime;
	@FXML
	private TextField txtVisitorsNum;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtEmail;
	
	//buttons
	@FXML
	private Button btnUpdate=null;
	@FXML
	private Button btnClose=null;
	
	//Image
	@FXML
	private ImageView imgOrderStrip;
	
	//set text to all fields
	public void loadOrder(Order order) {
		try {
			this.order = order;
			this.txtOrderNum.setText(order.getOrderNumber());
			this.txtParkName.setText(order.getParkName());
			this.txtVisitTime.setText(order.getTimeOfVisit());
			this.txtVisitorsNum.setText(order.getNumberOfVisitors());
			this.txtPhone.setText(order.getTelephoneNumber());
			this.txtEmail.setText(order.getEmail());
		}catch (Exception e) {
			System.out.println("Error in OrderFrameController: loadOrder");
			System.out.println(e.getMessage());
		}
	}
	
	//set text to Result label
	public void loadLabel (String str) {
		try {
			this.lblResult.setText(str);
		}
		catch (Exception e) {
			System.out.println("Error in OrderFrameController: loadLabel");
			System.out.println(e.getMessage());
		}
	}
	
	//Event for "Update" button
	public void pressUpdateBtn(ActionEvent event) throws Exception { 
		try {
			String parkName = this.txtParkName.getText();
			String telephoneNumber = this.txtPhone.getText();
			
			//send new data for update	
			ClientUI.chat.update(this.order.getOrderNumber(), parkName, telephoneNumber);
			

			//to show the changes
			FXMLLoader loader = new FXMLLoader();
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/OrderForm.fxml").openStream());
			
			OrderFrameController orderFrameController = loader.getController();
			
			if(ChatClient.result == true) {
				//updated successfully
				orderFrameController.loadLabel("Updated successfully!");
				Order order = getOrder();
				ChatClient.order = order;
			}
			else {
				//update failed
				orderFrameController.loadLabel("Update failed!");
			}
				
			orderFrameController.loadOrder(ChatClient.order);
		
			Scene scene = new Scene(root);	
			
			primaryStage.setTitle("Order Form");

			primaryStage.setScene(scene);		
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Error in OrderFrameController: pressUpdateBtn");
			System.out.println(e.getMessage());
		}

	}
	
	
	//Event for "Close" button
	public void pressCloseBtn(ActionEvent event) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
			
			FindOrderFrameController findOrderFrameController = loader.getController();
			
			Scene scene = new Scene(root);
						
			primaryStage.setTitle("Home Page");

			primaryStage.setScene(scene);		
			primaryStage.show();
		}catch(Exception e) {
			System.out.println("Error in OrderFrameController: pressCloseBtn");
			System.out.println(e.getMessage());
		}
	}
	
	//get data from all fields
	private Order getOrder() {
		Order order = new Order(null,null,null,null,null,null);
		try {	
			order.setOrderNumber(this.txtOrderNum.getText());
			order.setParkName(this.txtParkName.getText());
			order.setTimeOfVisit(this.txtVisitTime.getText());
			order.setNumberOfVisitors(this.txtVisitorsNum.getText());
			order.setTelephoneNumber(this.txtPhone.getText());
			order.setEmail(this.txtEmail.getText());
		}catch (Exception e) {
			System.out.println("Error in OrderFrameController: getOrder");
			System.out.println(e.getMessage());
		}
		
		return order;

	}


}
