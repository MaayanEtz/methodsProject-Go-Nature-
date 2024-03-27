package gui;

import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class DepartmentManagerMenuController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnRequest;

    @FXML
    private ComboBox<String> parkBox;

	public void loadData(String s) {
	    	
	    	try {
		
				//Load Parks List  	
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("ParksListGet"));
				arrmsg.add(new String("Get"));
				arrmsg.add(new String("Get"));
				ClientUI.chat.accept(arrmsg);
				
				if (ChatClient.dataFromServer.get(0).equals("null"))
					throw new NullPointerException("The parks list doesn't exists.");
				
				this.parkBox.getItems().addAll(ChatClient.dataFromServer);
	    	}catch (NullPointerException e) {
				System.out.println(e.getMessage());
	    	}
	}

    @FXML
    void pressReportsBtn(ActionEvent event) {
    	try {
        	NextPage page = new NextPage(event, "/gui/DepartmentManagerReportsPage.fxml", "Department Manager Reports Page", "DepartmentManagerReportsPageController", "pressReportsBtn", this.parkBox.getValue()); 
        	page.Next();
    	}catch (Exception e) {
    		System.out.println("Error in DepartmentManagerMenuController: pressReportsBtn");
    		System.out.println(e.getMessage());
    	}
    }

    @FXML
    void pressRequestBtn(ActionEvent event) {
    	try {
        	NextPage page = new NextPage(event, "/gui/DepartmentManager.fxml", "Department Manager Request Page", "DepartmentManagerController", "pressRequestBtn", this.parkBox.getValue()); 
        	page.Next();
    	}catch (Exception e) {
    		System.out.println("Error in DepartmentManagerMenuController: pressRequestBtn");
    		System.out.println(e.getMessage());
    	}
    }

    @FXML
    void pressLogout(ActionEvent event) {
    	try {
    		///////////ENTER LOG OUT REQUEST////////////
    		
        	NextPage page = new NextPage(event, "/gui/Login.fxml", "Login Page", "LoginController", "pressLogoutBtn"); 
        	page.Next();
    	}catch (Exception e) {
    		System.out.println("Error in DepartmentManagerController: pressLogOut");
    		System.out.println(e.getMessage());
    	}
    }
}

