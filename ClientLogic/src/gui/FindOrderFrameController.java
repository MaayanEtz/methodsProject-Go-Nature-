package gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import entity.Order;
//import entity.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FindOrderFrameController {
	
	public enum Action {
		EDIT, CANCEL
	}
	
	private Action action;
	
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
	//@FXML
	//private ImageView imgGoNature;
	
	//get entered order number
	private String getEnteredOrderNum() {
        return txtEnteredOrderNum.getText();
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	//Event for "Find" button
	public void pressFindBtn (ActionEvent event) throws Exception {
		
		try {
			
			String orderNumber = getEnteredOrderNum();
			
			if(orderNumber.trim().isEmpty()) {
				errorCase("You must enter an order number","You must enter an order number");
			}else {		
				// Check if the string contains any digit
		        Pattern pattern = Pattern.compile("\\d");
		        Matcher matcher = pattern.matcher(orderNumber);
		        
		        if (matcher.find()) {
		            System.out.println("String contains numbers.");
		        } else {
		            errorCase("String does not contain numbers.","Order number should contain only numbers.");
		            throw new Exception();
		        }
					
				//send order number for searching and get the order
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("OrderGet"));
				arrmsg.add(new String("String"));
				arrmsg.add(new String(orderNumber));
				
			    /////ANNA: OPEN////////////////
				//ClientUI.chat.accept(arrmsg);
				
			    /////ANNA: Check////////////////
				ChatClient.result = true; //order found
				ChatClient.order = new Order("St James Park","111", "2023-02-11 12:00:00", "3", "0587126782", "GA@gmail.com");
				//ChatClient.result = false; //order not found
				
				if(ChatClient.result == false) {
					errorCase("Order number not found","Order number does not exist in the system.");
				}else {
					
					System.out.println("Order number found");
					
					//Find order page is the same page for edit and cancel an order
					switch(this.action) {
						case EDIT: {
					    	NextPage page = new NextPage(event, "/gui/OrderForm.fxml", "Order Form", "OrderFrameController", "pressFindBtn", ChatClient.order);
					    	page.Next();
							break;}
						
						case CANCEL: {
					    	NextPage page = new NextPage(event, "/gui/CancelOrderForm.fxml", "Cancel Order Form", "CancelOrderFrameController", "pressFindBtn", ChatClient.order);
					    	page.Next();
							break;}
					}	
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
	    	NextPage page = new NextPage(event, "/gui/TravellerPage.fxml", "Traveller Page", "TravellerPageController", "pressBackBtn"); //need to add path and title
	    	page.Next();
		}catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: pressBackBtn");
			System.out.println(e.getMessage());
		}
		
	}
	
    //private method for error cases
    private void errorCase(String strPrint, String strSet) {
		System.out.println(strPrint);
		lblResult.setText(strSet);
    }

}
