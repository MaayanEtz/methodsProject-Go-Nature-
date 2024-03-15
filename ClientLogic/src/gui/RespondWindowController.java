package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RespondWindowController {
	
	@FXML
    private Button btnClose;

    @FXML
    private Label lblResult;
    
    @FXML
    private ImageView resultImg;
    
    //Setting image to the image view
    public void setImage(String result) {
    	try {
    		Image image;
    		
			if (result.equals("Cancelled successfully!")) {
			    image = new Image(getClass().getResourceAsStream("Pictures/V.png"));
			}else {
			    image = new Image(getClass().getResourceAsStream("Pictures/X.png"));
			}
			resultImg.setImage(image);
		} catch (Exception e) {
			System.out.println("Error in RespondWindowController: setImage");
			System.out.println(e.getMessage());
		}
    }
    
    //Setting label with the text
    public void setLabel(String text) {
    	try {
    		lblResult.setText(text);
    	}catch (Exception e) {
    		System.out.println("Error in RespondWindowController: setLabel");
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
			System.out.println("Error in RespondWindowController: pressCloseBtn");
			System.out.println(e.getMessage());
		}
    }

}
