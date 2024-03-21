package gui;

import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginController {
		
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

    
    @FXML
    // Event for "Login" button
	void Login(ActionEvent event) throws Exception {
		try {
			String username = username_id.getText(), password = password_id.getText();

			if (username.trim().isEmpty() || password.trim().isEmpty()) {
				// if user name or password are empty
				errorCase("You must enter username and password",
						"You must enter username and password in order to login");
			} else {
				
				ArrayList<String> loginDetails = new ArrayList<>(Arrays.asList(username, password));

				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("UserLogin"));
				arrmsg.add(new String("ArrayList<String>"));
				arrmsg.add(loginDetails);
				ClientUI.chat.accept(arrmsg);

				switch (ChatClient.dataFromServer.get(0)) {
					case "DepartmentWorker": {
						// if user name and password are correct and the type of the employee is
						// DepartmentWorker
						NextPage page = new NextPage(event, "/gui/DepartmentWorker.fxml", "Department worker page",
								"DepartmentWorkerController", "", ""); // need to add path and title
						page.Next();
						break;
					}
					case "ParkWorker": {
						// if user name and password are correct and the type of the employee is
						// ParkWorker
						NextPage page = new NextPage(event, "/gui/ParkWorkerMenu.fxml", "Park worker page",
								"ParkWorkerMenuController", "", ChatClient.dataFromServer.get(1));
						page.Next();
						break;
					}
					case "DepartmentManager": {
						// if user name and password are correct and the type of the employee is
						// DepartmentManager
						NextPage page = new NextPage(event, "/gui/DepartmentManager.fxml", "Department manager page",
								"DepartmentManagerController", "", ""); // need to add path and title
						page.Next();
						break;
					}
					case "ParkManager": {
						// if user name and password are correct and the type of the employee is
						// ParkManager
						NextPage page = new NextPage(event, "/gui/ParkManager.fxml", "Park manager page",
								"ParkManagerController", "", ""); // need to add path and title
						page.Next();
						break;
					}
					case "null": {
						// if user name or password are empty
						errorCase("You must enter username and password",
								"You must enter username and password in order to login");
						break;
					}
				}
			}
		} catch (Exception e) {
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

