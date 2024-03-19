package gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class ParkWorkerMenuController {
	
	 	@FXML
	    private Button btnEnterParkPlanned;
	    @FXML
	    private Button btnEnterParkUnplanned;
	    @FXML
	    private Button btnFind;
	    @FXML
	    private Button btnGetInvoicePlanned;
	    @FXML
	    private Button btnGetInvoiceUnplanned;
	    @FXML
	    private Button btnLogOut;

	    @FXML
	    private ComboBox<String> cmbChoosePark;

	    @FXML
	    private Label lblEnteredOrderNum;
	    @FXML
	    private Label lblResult;
	    
	    @FXML
	    private Text txtEmail;
	    @FXML
	    private TextField txtEnteredOrderNum;
	    @FXML
	    private TextField txtEnteredVisitorsNum;
	    @FXML
	    private Text txtNumberOfVisitors;
	    @FXML
	    private Text txtOrderNum;
	    @FXML
	    private Text txtParkName;
	    @FXML
	    private Text txtPhoneNum;
	    @FXML
	    private Text txtTimeOfVisit;
	    @FXML
	    private Text txtError;
	    @FXML
	    private Text txtErrorUnplanned;
	    
	    //load data
	    public void loadData() {
	    	try {
				//Load Parks List  	
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("ParksListGet"));
				arrmsg.add(new String("Get"));
				arrmsg.add(new String("Get"));
				ClientUI.chat.accept(arrmsg);
				if (ChatClient.dataFromServer.equals(null))
					throw new NullPointerException("The parks list doesn't exists.");
				this.cmbChoosePark.getItems().addAll(ChatClient.dataFromServer);
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: loadData");
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    //Event for "Enter the park" button in planned visit
	    @FXML
	    void pressEnterParkPlannedBtn(ActionEvent event) {
	    	try {
	    		String orderNum = this.txtEnteredOrderNum.getText();
	    		
	    		//Send the order
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("OrderedEnter"));
				arrmsg.add(new String("String"));
				arrmsg.add(orderNum);
				ClientUI.chat.accept(arrmsg);
				
				if(ChatClient.result == true)
					this.txtError.setText("The visitors succesfully entered the park");
				else
					this.txtError.setText("The visitors not entered the park");
	    		
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressEnterParkBtn");
	    		System.out.println(e.getMessage());
	    	}

	    }
	    
	  //Event for "Enter the park" button in unplanned visit
	    @FXML
	    void pressEnterParkUnplannedBtn(ActionEvent event) {
	    	try {
	    		String parkName = this.cmbChoosePark.getValue();
	    		String visitorNum = this.txtEnteredVisitorsNum.getText();
	    		
	    		if(visitorNum.trim().isEmpty()) {
	    			errorCaseUnplanned("You must enter a number of visitors","You must enter a number of visitors");
	    		}else {
	    			// Check if the string contains any digit
			        Pattern pattern = Pattern.compile("\\d");
			        Matcher matcher = pattern.matcher(visitorNum);
			        if (!matcher.find())
			            throw new IllegalArgumentException(); 
		    		
		    		ArrayList<String> arrEnterPark = new ArrayList<>();
		    		arrEnterPark.add(parkName);
		    		arrEnterPark.add(visitorNum);
		    		
		    		/////////OPEN WHEN READY///////////////////
					//ArrayList<Object> arrmsg = new ArrayList<Object>();
					//arrmsg.add(new String("UnplannedEnter"));
					//arrmsg.add(new String("ArrayList<String>"));
					//arrmsg.add(arrEnterPark);
					//ClientUI.chat.accept(arrmsg);	
	    		}    		
	    	}catch (IllegalArgumentException e) {
	    		errorCaseUnplanned("String does not contain numbers.","Number of visitors should contain only numbers.");
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressEnterParkUnplannedBtn");
	    		System.out.println(e.getMessage());
	    	}
	    }

	    //Event for "Find" button
	    @FXML
	    void pressFindBtn(ActionEvent event) {
	    	try {
	    		String orderNumber = this.txtEnteredOrderNum.getText();
	    		
	    		if(orderNumber.trim().isEmpty()) {
					errorCase("You must enter an order number","You must enter an order number");
	    		}else {
	    			// Check if the string contains any digit
			        Pattern pattern = Pattern.compile("\\d");
			        Matcher matcher = pattern.matcher(orderNumber);
			        if (!matcher.find())
			            throw new IllegalArgumentException();   

						
					//send order number for searching and get the order
					ArrayList<Object> arrmsg = new ArrayList<Object>();
					arrmsg.add(new String("OrderGet"));
					arrmsg.add(new String("String"));
					arrmsg.add(new String(orderNumber));
					ClientUI.chat.accept(arrmsg);	
	    		}
	    		
	    		if(ChatClient.result == false) {
					errorCase("Order number not found","Order number does not exist in the system.");
				}else {
					System.out.println("Order number found");
					
					//Set the order to the screen
					this.txtOrderNum.setText(ChatClient.order.getOrderNumber());
					this.txtParkName.setText(ChatClient.order.getParkName());
					this.txtTimeOfVisit.setText(ChatClient.order.getTimeOfVisit());
					this.txtNumberOfVisitors.setText(ChatClient.order.getNumberOfVisitors());
					this.txtPhoneNum.setText(ChatClient.order.getTelephoneNumber());
					this.txtEmail.setText(ChatClient.order.getEmail());
					
				}
	    		
	    	}catch (IllegalArgumentException e) {
	    		errorCase("String does not contain numbers.","Order number should contain only numbers.");
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressFindBtn");
	    		System.out.println(e.getMessage());
	    	}

	    }

	    //Event for "Log out" button
	    @FXML
	    void pressLogOut(ActionEvent event) {
	    	try {
	    		///////////ENTER LOG OUT REQUEST////////////
	    		
	        	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn"); 
	        	page.Next();
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressLogOut");
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    //private method for error cases
	    private void errorCase(String strPrint, String strSet) {
			System.out.println(strPrint);
			this.txtError.setText(strSet);
	    }
	    
	    //private method for error cases
	    private void errorCaseUnplanned(String strPrint, String strSet) {
			System.out.println(strPrint);
			this.txtErrorUnplanned.setText(strSet);
	    }


}
