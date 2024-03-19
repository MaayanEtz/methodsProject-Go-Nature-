package gui;

import client.ChatClient;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class TravellerPageController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnCancelling;

    @FXML
    private Button btnChecking;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnNewOrder;

    @FXML
    private ImageView goIMG;

    //Event for "Cancelling an order" button
    @FXML
    void pressCancellingBtn(ActionEvent event) throws Exception {
    	try {
			//visitorID instead of method
			NextPage page = new NextPage(event, "/gui/FindOrderFrame.fxml", "Find order", "FindOrderFrameController", "pressCancellingBtn", FindOrderFrameController.Action.CANCEL);
			page.Next();
		} catch (Exception e) {
    		System.out.println("Error in TravellerPageController: pressCancellingBtn");
    		System.out.println(e.getMessage());
		}
    }
    
    //Event for "Checking and editing an existing order" button
    @FXML
    void pressCheckingBtn(ActionEvent event) throws Exception {
    	try {
    		//visitorID instead of method
    		NextPage page = new NextPage(event, "/gui/FindOrderFrame.fxml", "Find order", "FindOrderFrameController", "pressCheckingBtn", FindOrderFrameController.Action.EDIT);
        	page.Next();
    	}catch (Exception e) {
    		System.out.println("Error in TravellerPageController: pressCheckingBtn");
    		System.out.println(e.getMessage());
    	}
    }

    //Event for "Placing a new order" button
    @FXML
    void pressNewOrderBtn(ActionEvent event) throws Exception {
    	NextPage page = new NextPage(event, "/gui/CreateOrderForm.fxml", "Create new order", "CreateOrderFrameController", "pressNewOrderBtn");
    	page.Next();
    }
    
    //Event for "Back" button
    @FXML
    void pressBackBtn(ActionEvent event) throws Exception {
    	//set visitor ID as ""
    	ChatClient.visitorID = "";
    	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn"); 
    	page.Next();
    }
    
	// Event for "Exit" button
	public void pressExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Home Page");
		System.exit(0);
	}

}
