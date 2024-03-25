package gui;

import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import entity.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    private Label txtEmail;
    @FXML
    private Label txtOrderNum;
    @FXML
    private Label txtParkName;
    @FXML
    private Label txtPhone;
    @FXML
    private Label txtVisitTime;
    @FXML
    private Label txtVisitorsNum;

    
    //Event for "Cancel order" button
    @FXML
    void pressCancelBtn(ActionEvent event) {
    	try {
    		String orderNumber = this.txtOrderNum.getText();
		
			//send order number for cancelling
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("OrderCancel"));
			arrmsg.add(new String("String"));
			arrmsg.add(new String(orderNumber));
			ClientUI.chat.accept(arrmsg);

			if(ChatClient.result == true) {
				//updated successfully			
		    	NextPage page = new NextPage(event, "/gui/RespondWindow.fxml", "Respond", "RespondWindowController", "pressCancelBtn", new String("Cancelled successfully!"));
		    	page.Next();

			}else {
				//update failed			
		    	NextPage page = new NextPage(event, "/gui/RespondWindow.fxml", "Respond", "RespondWindowController", "pressCancelBtn", new String("Cancel failed!"));
		    	page.Next();
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
			System.out.println("Error in CancelOrderFrameController: pressCloseBtn");
			System.out.println(e.getMessage());
		}
    }
    
	//get data from all fields
	private Order getOrder() {
		Order order = new Order(null,null,null,null,null,null,null);
		try {	
			order.setOrderNumber(this.txtOrderNum.getText());
			order.setParkName(this.txtParkName.getText());
			order.setTimeOfVisit(this.txtVisitTime.getText());
			order.setNumberOfVisitors(this.txtVisitorsNum.getText());
			order.setTelephoneNumber(this.txtPhone.getText());
			order.setEmail(this.txtEmail.getText());
		}catch (Exception e) {
			System.out.println("Error in CancelOrderFrameController: getOrder");
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
				System.out.println("Error in CancelOrderFrameController: loadOrder");
				System.out.println(e.getMessage());
			}
		}


}
