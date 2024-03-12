package gui;

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
    	NextPage page = new NextPage(event, "/gui/FindOrderFrame.fxml", "Find order", "FindOrderFrameController", "pressCheckingBtn", FindOrderFrameController.Action.CANCEL);
    	page.Next();
    }
    
    //Event for "Checking and editing an existing order" button
    @FXML
    void pressCheckingBtn(ActionEvent event) throws Exception {
    	try {
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
    	NextPage page = new NextPage(event, "", "", "", "pressNewOrderBtn"); //need to add path and title
    	page.Next();
    }
    
    //Event for "Back" button
    @FXML
    void pressBackBtn(ActionEvent event) throws Exception {
    	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn"); 
    	page.Next();
    }
    
	// Event for "Exit" button
	public void pressExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Home Page");
		System.exit(0);
	}

}
