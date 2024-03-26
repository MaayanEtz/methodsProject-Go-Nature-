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
    private Text promptTxt;
    
    @FXML
    private Text availableSpaceTxt;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnDeny;

    @FXML
    private Button btnBack;

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
    		//check if there are new information to approve
    		ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("ParkCheckIfApproveRequired"));
			arrmsg.add(new String(parkName));
			ClientUI.chat.accept(arrmsg);
			
			//show the information 
    		if(ChatClient.result == true) {
				arrmsg.clear();
				arrmsg.add(new String("ParkNewParamsGet"));
				arrmsg.add(new String("String"));
				arrmsg.add(new String(parkName));
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
    		}else {
    			
    			//show prompt text
    			promptTxt.setText("There are no new information to approve");
    		}
	        
    	} catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: loadData");
			System.out.println(e.getMessage());
    }
   }
    
    @FXML
    void pressApprove(ActionEvent event) {
    	try {
    		//send information to change the db
    		ArrayList<Object> arrmsg = new ArrayList<Object>();
    		ArrayList<String> updatePark = new ArrayList<String>();
    		arrmsg.add(new String("ParkCorrentParamsUpdate"));
    		arrmsg.add(new String("ArrayList<String>"));
    		updatePark.add(new String(parkName));
    		updatePark.add(new String(maxCapacityTxt.getText()));
    		updatePark.add(new String(configureGapTxt.getText()));
    		updatePark.add(new String(closesParkTxt.getText()));
    		updatePark.add(new String("0"));
    		updatePark.add(new String("0"));
    		updatePark.add(new String("0"));
    		arrmsg.add(updatePark);
    		
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
    		ArrayList<String> updatePark = new ArrayList<String>();
    		arrmsg.add(new String("ParkCurrentParamsGet"));
            arrmsg.add(new String("String"));
            arrmsg.add(new String(parkName));
            ClientUI.chat.accept(arrmsg);

            if (ChatClient.dataFromServer.equals(null))
                throw new NullPointerException("This park doesn't exists.");
            
    		updatePark.add(new String(parkName));
    		updatePark.add(new String((String) arrmsg.get(0))); //Capacity
    		updatePark.add(new String((String) arrmsg.get(1)));	//Gap
    		updatePark.add(new String((String) arrmsg.get(2))); //Stay time
    		updatePark.add(new String("0"));
    		updatePark.add(new String("0"));
    		updatePark.add(new String("0"));
    		
    		arrmsg.clear();
    		arrmsg.add(new String("ParkCurrentParamsUpdate"));
    		arrmsg.add(new String("ArrayList<String>"));
    		arrmsg.add(updatePark);
    		
			ClientUI.chat.accept(arrmsg);
			if (ChatClient.dataFromServer.equals(false))
				throw new NullPointerException("Update Park info woesn't succesful.");
    	}catch (Exception e) {
			System.out.println("Error in DepartmentManagerController: pressApprove");
			System.out.println(e.getMessage());
    }
    }

    @FXML
    void pressBack(ActionEvent event) {
    	try {
        	NextPage page = new NextPage(event, "/gui/DepartmentManagerMenu.fxml", "Department Manager Menu", "DepartmentManagerMenuController", "pressLogoutBtn"); 
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


