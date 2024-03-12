package gui;


import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class IdentifyPageController {

	    @FXML
	    private Button btnBack;

	    @FXML
	    private Button btnExit;

	    @FXML
	    private Button btnIdntefy;

	    @FXML
	    private Text errorTxt;

	    //@FXML
	    //private ImageView goIMG;

	    @FXML
	    private TextField identify_id;

	    //Event for "Identify" button
	    @FXML
	    void pressIdentifyBtn(ActionEvent event) {
	    	try {
	    		
	    		//ADD CHECK FOR IDENTIFICATION NUMBER
	    		
		    	NextPage page = new NextPage(event, "/gui/TravellerPage.fxml", "Traveller Page", "TravellerPageController", "pressIdentifyBtn");
		    	page.Next();
	    	}catch (Exception e) {
	    		System.out.println("Error in IdentifyPageController: pressBackBtn");
	    	}
	    }
	    
	    //Event for "Back" button
	    @FXML
	    void pressBackBtn(ActionEvent event) throws Exception {
	    	try {
		    	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn");
		    	page.Next();
	    	}catch (Exception e) {
	    		System.out.println("Error in IdentifyPageController: pressBackBtn");
	    	}

	    }

	    @FXML
		// Event for "Exit" button
		public void pressExitBtn(ActionEvent event) throws Exception {
			System.out.println("Exit Identify Page");
			System.exit(0);
		}

	}


 