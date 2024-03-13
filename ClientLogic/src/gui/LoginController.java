package gui;

import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {

	// Image
    //@FXML
    //private ImageView goIMG;
    
    // buttons
    @FXML
    private Button btnExit = null;
    @FXML
    private Button btnLogin = null;
    
    @FXML
    private Button btnBack;
     
    // text fields
    @FXML
    private TextField password_id;
    @FXML
    private TextField username_id;
    
    @FXML
    private Text errorTxt;
    
	private Boolean result;
    
    @FXML
    // Event for "Login" button
    void Login(ActionEvent event) throws Exception {
    	try {
    		String username = username_id.getText(), password = password_id.getText();
    		
    		if (username.trim().isEmpty() || password.trim().isEmpty()) {
    			//if user name or password are empty
    			/*System.out.println("You must enter username and password");
    			errorTxt.setText("You must enter username and password in order to login");
    			username_id.setText("");
    			password_id.setText("");*/
    			errorCase("You must enter username and password","You must enter username and password in order to login");
    		} else {
    			try {
    				ArrayList<String> loginDetails = new ArrayList<>(Arrays.asList(username, password));
    				
    				ArrayList<Object> arrmsg = new ArrayList<Object>();
    				arrmsg.add(new String("UserLogin"));
    				arrmsg.add(new String("ArrayList<String>"));
    				arrmsg.add(loginDetails);
    				ClientUI.chat.accept(arrmsg);
    				
    				/*DELETE THIS BECAUSE IT'S ONLY FOR CHECK NEEDS*/
    				result = true;
    				
    				if(ChatClient.result == true) {
    					//user logged in
    				}else {
    					//user not logged in
    				}
    				
    				} catch (Exception e) {
    					System.out.println("The username or the password are wrong");
    					errorTxt.setText("The username or the password are wrong, Please try again");
    					username_id.setText("");
    					password_id.setText("");
    					return;
    				} if (result) {
    					//if user name and password are correct
    			    	NextPage page = new NextPage(event, "", "", "", "Login"); //need to add path and title
    			    	page.Next();
    				} else { //catch and else same code
    					/*System.out.println("The username or the password are wrong");
    					errorTxt.setText("The username or the password are wrong, Please try again");
    					username_id.setText("");
    					password_id.setText("");*/
    					
    					errorCase("The username or the password are wrong", "The username or the password are wrong, Please try again");
    					return;
    				}
    		}	
    	}catch (Exception e) {
    		System.out.println("Error in LoginController: Login");
    	}	
    }
    
    
    //Event for "Back" button
    @FXML
    void pressBackBtn(ActionEvent event) throws Exception {
    	try {
        	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn");
        	page.Next();
    	}catch (Exception e) {
    		System.out.println("Error in LoginController: pressBackBtn");
    		System.out.println(e.getMessage());
    	}

    }

    @FXML
	// Event for "Exit" button
	public void pressExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Home Page");
		System.exit(0);
	}
    
    //private method for error cases
    private void errorCase(String strPrint, String strSet) {
		System.out.println(strPrint);
		errorTxt.setText(strSet);
		username_id.setText("");
		password_id.setText("");
    }

}

