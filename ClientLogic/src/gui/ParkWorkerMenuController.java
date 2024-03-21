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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class ParkWorkerMenuController {
	
		private String parkName;
		
		private Boolean isGotInvoice = false;
		
		//buttons
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
	    private Button btnExitResitration;

	    @FXML
	    private Label lblEnteredOrderNum;
	    @FXML
	    private Label lblResult;
	    

	    @FXML
	    private TextField txtEnteredOrderNum;
	    @FXML
	    private TextField txtEnteredVisitorsNum;
	    @FXML
	    private TextField txtExitRegNumber;
	    
	    @FXML
	    private Text txtEmail;
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
	    @FXML
	    private Text txtErrorExitReg;
	    @FXML
	    private Text txtTitle;
	    
	    @FXML
	    private CheckBox ckbGuidedGroup;
	    @FXML
	    private CheckBox ckbPrivateFamVisit;
	    
	    //load data
	    public void loadData(String parkName) {
	    	try {
	    		//load title
	    		this.parkName = parkName;
	    		this.txtTitle.setText(new String(parkName + "worker"));
	    		
	            // Add change listeners to each checkbox
	    		ckbGuidedGroup.selectedProperty().addListener((observable, oldValue, newValue) -> {
	                if (newValue) {
	                	ckbPrivateFamVisit.setSelected(false);
	                }
	            });
	    		ckbPrivateFamVisit.selectedProperty().addListener((observable, oldValue, newValue) -> {
	                if (newValue) {
	                	ckbGuidedGroup.setSelected(false);
	                }
	            });
	    		
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: loadData");
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    //Event for "Enter the park" button in planned visit
	    @FXML
	    void pressEnterParkPlannedBtn(ActionEvent event) {
	    	try {
	    		
	    		if (this.isGotInvoice == false)
	    			errorCase("You have tried to enter the park before getting invoce.", "You should get invoce first.");
	    		else {
		    		String orderNum = this.txtEnteredOrderNum.getText();
		    		
		    		//Send the order
					ArrayList<Object> arrmsg = new ArrayList<Object>();
					arrmsg.add(new String("OrderedEnter"));
					arrmsg.add(new String("String"));
					arrmsg.add(orderNum);
					ClientUI.chat.accept(arrmsg);
					
					if(ChatClient.result == true)
						errorCase("The visitors succesfully entered the park","The visitors succesfully entered the park.");
					else
						errorCase("The visitors not entered the park","The visitors not entered the park.");
	    		}	    		
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressEnterParkBtn");
	    		System.out.println(e.getMessage());
	    	}

	    }
	    
	  //Event for "Enter the park" button in unplanned visit
	    @FXML
	    void pressEnterParkUnplannedBtn(ActionEvent event) {
	    	try {
	    		if (this.isGotInvoice == false)
	    			errorCaseUnplanned("You have tried to enter the park before getting invoce.", "You should get invoce first.");
	    		else {
		    		String visitorNum = this.txtEnteredVisitorsNum.getText();
		    		
		    		if(visitorNum.trim().isEmpty()) {
		    			errorCaseUnplanned("String for number of visitors is empty","You must enter a number of visitors.");
		    		}else {
		    			// Check if the string contains any digit
				        Pattern pattern = Pattern.compile("\\d");
				        Matcher matcher = pattern.matcher(visitorNum);
				        if (!matcher.find())
				            throw new IllegalArgumentException(); 
			    		
			    		ArrayList<String> arrEnterPark = new ArrayList<>();
			    		arrEnterPark.add(this.parkName);
			    		arrEnterPark.add(visitorNum);
			    		
						ArrayList<Object> arrmsg = new ArrayList<Object>();
						arrmsg.add(new String("UnplannedEnter"));
						arrmsg.add(new String("ArrayList<String>"));
						arrmsg.add(arrEnterPark);
						ClientUI.chat.accept(arrmsg);
						
						if(ChatClient.result == true)
							errorCaseUnplanned("The unplanned visitors succesfully entered the park","The unplanned visitors succesfully entered the park.");
						else
							errorCaseUnplanned("The unplanned visitors not entered the park","The unplanned visitors not entered the park.");
		    		}
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
					errorCase("String for order number is empty","You must enter an order number");
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
	    		}	
	    	}catch (IllegalArgumentException e) {
	    		errorCase("String does not contain numbers.","Order number should contain only numbers.");
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressFindBtn");
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    
	    //Event for "Get Invoice" planned visit button
	    @FXML
	    void pessGetInvoicePlanned(ActionEvent event) {
	    	try {
	    		//1. Check if the group is Guided
	    		
	    		//2. Check if payed in advance
	    		
	    		//3. Check the number of visitors
	    		
	    		//4. Call PriceGenerator to get the final price
	    		
	    		//5. Generate Invoice
	    		
	    		//6. Open Invoice in the SECOND window
	    		
	    		
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pessGetInvoicePlanned");
	    		System.out.println(e.getMessage());
	    	}
	    	
	    }
	       
	    //Event for "Get Invoice" unplanned visit button
	    @FXML
	    void pessGetInvoiceUnplanned(ActionEvent event) {
	    	try {
	    		Boolean isGuidedGroup = false;
	    		this.isGotInvoice = true;
	    		
	    		//1. Check checkbox if guided group or private visit
	    		if(this.ckbGuidedGroup.isSelected())
	    			isGuidedGroup = true;
	    		
	    		//2. Check the number of visitors
	    		String visitorNum = this.txtEnteredVisitorsNum.getText();
	    		if(visitorNum.trim().isEmpty()) {
	    			errorCaseUnplanned("String for number of visitors is empty","You must enter a number of visitors.");
	    		}else {
	    			// Check if the string contains any digit
			        Pattern pattern = Pattern.compile("\\d");
			        Matcher matcher = pattern.matcher(visitorNum);
			        if (!matcher.find())
			            throw new IllegalArgumentException(); 
		    		
		    		//3. Call PriceGenerator to get the final price
			        
		    		
		    		//4. Generate Invoice
		    		
		    		//5. Open Invoice in the SECOND window
			        
			        
			        
	    		}
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pessGetInvoiceUnplanned");
	    		System.out.println(e.getMessage());
	    	}
	    }
	    
	    //Event for "Perform exit registration" button
	    @FXML
	    void pressPerformExitRegistration(ActionEvent event) {
	    	try {
	    		String exitVisitorsNum = this.txtExitRegNumber.getText();
	    		
	    		if(exitVisitorsNum.trim().isEmpty()) {
	    			errorCaseExit("String number of visitors for exit registration is empty","You must enter a number of visitors for exit registration.");
	    		}else {
	    			// Check if the string contains any digit
			        Pattern pattern = Pattern.compile("\\d");
			        Matcher matcher = pattern.matcher(exitVisitorsNum);
			        if (!matcher.find())
			            throw new IllegalArgumentException(); 
			        
					//send the number of visitors for performing an exit registration
					ArrayList<Object> arrmsg = new ArrayList<Object>();
					arrmsg.add(new String("ExitRegistration"));
					arrmsg.add(new String("ArrayList<String>"));
					arrmsg.add(exitVisitorsNum);
					ClientUI.chat.accept(arrmsg);
					
					if (ChatClient.result == false) {
						//exit registration not succeeded
						errorCaseExit("Exit registration failed","Exit registration wasn't performed.");
					}else {
						errorCaseExit("Exit registration succeeded","Exit registration performed successfully.");
					}
	    		}
	    	}catch (IllegalArgumentException e) {
	    		errorCaseExit("String does not contain numbers.","The number of visitors should contain only numbers.");
	    	}catch (Exception e) {
	    		System.out.println("Error in ParkWorkerMenuController: pressPerformExitRegistration");
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
	    
	    //private method for error cases
	    private void errorCaseExit(String strPrint, String strSet) {
			System.out.println(strPrint);
			this.txtErrorExitReg.setText(strSet);
	    }



}
