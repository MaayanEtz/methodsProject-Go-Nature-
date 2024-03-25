package gui;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.DateCell;

public class ParkManagerController 
{

		private String parkName;
     	@FXML
        private Button btnLogout;

        @FXML
        private Button btnRefresh;

        @FXML
        private Button btnSendToApprove;

        @FXML
        private Label lblResult;

        @FXML
        private ComboBox<String> selectTimeCmb;

        @FXML
        private Text availableSpaceTxt;

        @FXML
        private TextField txtCapacity;

        @FXML
        private TextField txtGapInPark;

        //load data
        public void loadData(String parkName) 
        {
        	try {
	    		this.parkName = parkName;
	    		//load data
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("ParksInfoGet"));
				arrmsg.add(new String(parkName));
				arrmsg.add(new String("Get"));
				ClientUI.chat.accept(arrmsg);
				
				if (ChatClient.dataFromServer.equals(null))
					throw new NullPointerException("This park doesn't exists.");
				arrmsg.clear();
				arrmsg.add(ChatClient.dataFromServer);
				txtCapacity.setText((String) arrmsg.get(0));
				txtGapInPark.setText((String) arrmsg.get(1));
				//add availably closing time hours 
				ArrayList<String> times = new ArrayList<>();
		            times.add("09:00");
		            for(int i=10; i<18; i++) {
		                times.add(i + ":00");
		            }
		        this.selectTimeCmb.getItems().addAll(times);
		        selectTimeCmb.setValue((String) arrmsg.get(2));
		        Integer spaceInPark = ((Integer) arrmsg.get(1)) - ((Integer) arrmsg.get(4));
		        availableSpaceTxt.setText(spaceInPark.toString());
		        
        	} catch (Exception e) {
    			System.out.println("Error in ParkManagerController: loadData");
    			System.out.println(e.getMessage());
        }
        }

        @FXML
	    void pressLogOut(ActionEvent event) {
	    	try {
	    		///////////ENTER LOG OUT REQUEST////////////
	    		
	        	NextPage page = new NextPage(event, "/gui/Login.fxml", "Login Page", "LoginController", "pressLogoutBtn"); 
	        	page.Next();
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressLogOut");
	    		System.out.println(e.getMessage());
	    	}
        }

        @FXML
        void pressRefreshbtn(ActionEvent event) 
        {
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
    			System.out.println("Error in ParkManagerController: pressRefreshbtn");
    			System.out.println(e.getMessage());
        }
        }
        @FXML
        void pressSentTomanager(ActionEvent event) 
        {
        	try {
        		ArrayList<Object> arrmsg = new ArrayList<Object>();
        		ArrayList<Object> updatePark = new ArrayList<Object>();
        		arrmsg.add(new String("UpdateParkInfoForAprrovel "));
        		arrmsg.add(new String("Update"));
        		updatePark.add(new String(parkName));
        		updatePark.add(new String(txtCapacity.getText()));
        		updatePark.add(new String(txtGapInPark.getText()));
        		updatePark.add(new String(selectTimeCmb.getValue()));
        		updatePark.add(new String("0"));
        		arrmsg.add(new ArrayList(updatePark));
        		
				ClientUI.chat.accept(arrmsg);
				if (ChatClient.dataFromServer.equals(false))
					throw new NullPointerException("Update manager doesn't succesful.");
        	}catch (Exception e) {
    			System.out.println("Error in ParkManagerController: pressSentTomanager");
    			System.out.println(e.getMessage());
        }
        }


    }