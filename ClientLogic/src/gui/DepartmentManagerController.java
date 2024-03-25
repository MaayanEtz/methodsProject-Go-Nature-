package gui;

import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class DepartmentManagerController {

	private String parkName;
    @FXML
    private Text availableSpaceTxt;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnDeny;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnRefresh;

    @FXML
    private Text closesParkTxt;

    @FXML
    private Text configureGapTxt;

    @FXML
    private Text maxCapacityTxt;
    
  //load data
    public void loadData(String parkName) 
    {
    	try {
    		this.parkName = parkName;
    		//load data
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("ParkInfoForAprrovelGet"));
			arrmsg.add(new String(parkName));
			arrmsg.add(new String("Get"));
			ClientUI.chat.accept(arrmsg);
			
			if (ChatClient.dataFromServer.equals(null))
				throw new NullPointerException("This park doesn't exists.");
			arrmsg.clear();
			arrmsg.add(ChatClient.dataFromServer);
			maxCapacityTxt.setText((String) arrmsg.get(0));
			configureGapTxt.setText((String) arrmsg.get(1));
			closesParkTxt.setText((String) arrmsg.get(2));
	        Integer spaceInPark = ((Integer) arrmsg.get(1)) - ((Integer) arrmsg.get(4));
	        availableSpaceTxt.setText(spaceInPark.toString());
	        
    	} catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: loadData");
			System.out.println(e.getMessage());
    }
   }
    
    @FXML
    void pressApprove(ActionEvent event) {
    	try {
    		ArrayList<Object> arrmsg = new ArrayList<Object>();
    		ArrayList<Object> updatePark = new ArrayList<Object>();
    		arrmsg.add(new String("UpdateParkInfo"));
    		arrmsg.add(new String("Update"));
    		updatePark.add(new String(parkName));
    		updatePark.add(new String(maxCapacityTxt.getText()));
    		updatePark.add(new String(configureGapTxt.getText()));
    		updatePark.add(new String(closesParkTxt.getText()));
    		updatePark.add(new String("1"));
    		arrmsg.add(new ArrayList(updatePark));
    		
			ClientUI.chat.accept(arrmsg);
			if (ChatClient.dataFromServer.equals(false))
				throw new NullPointerException("Update manager doesn't succesful.");
    	}catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: pressApprove");
			System.out.println(e.getMessage());
    }
    }

    @FXML
    void pressDeny(ActionEvent event) {
    	try {
    		ArrayList<Object> arrmsg = new ArrayList<Object>();
    		ArrayList<Object> updatePark = new ArrayList<Object>();
    		arrmsg.add(new String("UpdateParkInfo"));
    		arrmsg.add(new String("Update"));
    		updatePark.add(new String(parkName));
    		updatePark.add(new String(maxCapacityTxt.getText()));
    		updatePark.add(new String(configureGapTxt.getText()));
    		updatePark.add(new String(closesParkTxt.getText()));
    		updatePark.add(new String("2"));
    		arrmsg.add(new ArrayList(updatePark));
    		
			ClientUI.chat.accept(arrmsg);
			if (ChatClient.dataFromServer.equals(false))
				throw new NullPointerException("Update Park info woesn't succesful.");
    	}catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: pressApprove");
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

    @FXML
    void pressRefreshbtn(ActionEvent event) {
    	try {
    		ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("AvilableSpaceGet"));
			arrmsg.add(new String(parkName));
			arrmsg.add(new String("Get"));
			ClientUI.chat.accept(arrmsg);
			if (ChatClient.dataFromServer.equals(null))
				throw new NullPointerException("This park doesn't exists.");
	        Integer spaceInPark = ((Integer) arrmsg.get(1)) - ((Integer) arrmsg.get(4));
	        availableSpaceTxt.setText(spaceInPark.toString());
    	}catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: pressRefreshbtn");
			System.out.println(e.getMessage());
    }
   }

}


