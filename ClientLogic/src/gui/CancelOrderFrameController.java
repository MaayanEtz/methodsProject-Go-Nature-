package gui;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import entity.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CancelOrderFrameController {
	
	private Order order;
	
	//buttons
	@FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;

    //labels
    @FXML
    private Label lblEmail;

    @FXML
    private Label lblOrderNum;

    @FXML
    private Label lblParkName;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblResult;

    @FXML
    private Label lblVisitTime;

    @FXML
    private Label lblVisitorsNum;

    //text fields
    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtOrderNum;

    @FXML
    private TextField txtParkName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtVisitTime;

    @FXML
    private TextField txtVisitorsNum;

    
    //Event for "Cancel order" button
    @FXML
    void pressCancelBtn(ActionEvent event) {
    	try {
    		String orderNumber = this.txtOrderNum.getText();
		
			//send order number for cancelling	
			ClientUI.chat.accept(orderNumber);
			
			if(ChatClient.result == true) {
				//updated successfully
				lblResult.setText("Cancelled successfully!");

				ChatClient.order = getOrder();
				loadOrder(ChatClient.order);
			}else {
				//update failed
				lblResult.setText("Cancel failed!");
			}	
    		
    	}catch (Exception e) {
    		System.out.println("Error in CancelOrderFrameController: pressCancelBtn");
    		System.out.println(e.getMessage());
    	}

    }

    //Event for "Close" button
    @FXML
    void pressCloseBtn(ActionEvent event) {
		try {
			
	    	NextPage page = new NextPage(event, "/gui/TravellerPage.fxml", "Traveller Page", "TravellerPageController", "pressIdentifyBtn");
	    	page.Next();
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


}
