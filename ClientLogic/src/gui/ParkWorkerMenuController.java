package gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import entity.SecondPage;
import entity.PriceGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;


public class ParkWorkerMenuController {
	
		private String parkName;
		
		private Boolean isGotInvoice = false;
		
	    @FXML
	    private TabPane tabPane;
		
		//buttons
	 	@FXML
	    private Button btnEnterParkPlanned, btnEnterParkUnplanned;
	    @FXML
	    private Button btnFind, btnExitResitration;
	    @FXML
	    private Button btnGetInvoicePlanned, btnGetInvoiceUnplanned;
	    @FXML
	    private Button btnLogOut;

	    @FXML
	    private Label lblEnteredOrderNum, lblResult;    

	    @FXML
	    private TextField txtEnteredOrderNum, txtEnteredVisitorsNum, txtExitRegNumber;
	    
	    @FXML
	    private Text txtEmail, txtNumberOfVisitors, txtOrderNum, txtParkName, txtPhoneNum, txtTimeOfVisit;
    
	    @FXML
	    private Text txtError, txtErrorUnplanned, txtErrorExitReg;
	    @FXML
	    private Text txtTitle;
	    
	    @FXML
	    private CheckBox ckbGuidedGroup, ckbPrivateFamVisit;

	    
	    //load data
	    public void loadData(String parkName) {
	    	try {
	    		//load title
	    		this.parkName = parkName;
	    		this.txtTitle.setText(new String(parkName + " worker"));
	    		
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
	    		
	            // Reset data in other tabs when a new tab is selected
	            tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
	                if (oldTab != null) {
	                    // Reset data in oldTab here
	                	isGotInvoice = false;

	                    // Set up a loop to clear the content of each text field
	                    for (TextField textField : new TextField[]{txtEnteredOrderNum, txtEnteredVisitorsNum, txtExitRegNumber}) {
	                        textField.clear(); // Clear the content of the text field
	                    }
	                    
	                    // Set up a loop to clear the content of each Text object
	                    for (Text text : new Text[]{txtEmail, txtNumberOfVisitors, txtOrderNum, txtParkName, txtPhoneNum, txtTimeOfVisit, txtError, txtErrorUnplanned, txtErrorExitReg}) {
	                        text.setText(""); // Clear the content of the Text object
	                    }
	                    
	                    // Set up a loop to clear the selection of each CheckBox
	                    for (CheckBox checkBox : new CheckBox[]{ckbGuidedGroup, ckbPrivateFamVisit}) {
	                        checkBox.setSelected(false); // Clear the selection of the CheckBox
	                    }
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
		    		
		    		if(!parkName.equals(this.txtParkName.getText()))
		    			throw new IllegalArgumentException("You can't deal with not your park orders.");
		    		
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
	    	}catch (IllegalArgumentException e) {
	    		errorCase(e.getMessage(),e.getMessage());
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
			    		if(!parkName.equals(this.txtParkName.getText()))
			    			throw new IllegalArgumentException("You can't deal with not your park orders.");
		    			
		    			// Check if the string contains any digit
				        Pattern pattern = Pattern.compile("\\d");
				        Matcher matcher = pattern.matcher(visitorNum);
				        if (!matcher.find())
				            throw new IllegalArgumentException("Number of visitors should contain only numbers");
				        
				        if (Integer.parseInt(visitorNum) < 1)
				        	throw new IllegalArgumentException("The number of visitors should be greater then 0");
				        
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
	    		errorCaseUnplanned(e.getMessage(),e.getMessage());
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
						
						this.txtError.setText("");
						
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
	    		if(this.txtOrderNum.getText().equals(""))
	    			throw new NullPointerException("You should find order first");
	    		
	    		Boolean isGuidedGroup = false, isPaidInAdvance = false;
	    		Integer visitorNumInt = 0;
	    		Double finalPrice = 0.0;
	    		
	    		//1. Check if the group is Guided
	    		ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("GroupGuideCheck"));
				arrmsg.add(new String("String"));
				arrmsg.add(ChatClient.order.getVisitorID());
				ClientUI.chat.accept(arrmsg);
				
				if(ChatClient.result == true)
					isGuidedGroup = true;
				
				//2. Check if payed in advance
	    		arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("getPaidInAdvance"));
				arrmsg.add(new String("String"));
				arrmsg.add(ChatClient.order.getOrderNumber());
				ClientUI.chat.accept(arrmsg);
				
				if(ChatClient.result == true)
					isPaidInAdvance = true;
	    		
	    		//3. Check the number of visitors
				visitorNumInt = Integer.parseInt(ChatClient.order.getNumberOfVisitors());
	    		
	    		//4. Call PriceGenerator to get the final price
				PriceGenerator priceGenerator = new PriceGenerator();
				priceGenerator.setIsGuidedGroup(isGuidedGroup);
				priceGenerator.setIsPaidInAdvance(isPaidInAdvance);
				priceGenerator.setIsPlannedVisit(true);
				priceGenerator.setVisitorsNumber(visitorNumInt);	
				finalPrice = priceGenerator.generateFinalPrice();
	    		
	    		//5. Generate Invoice and open Invoice in the SECOND window	
				SecondPage page = new SecondPage(event, "/gui/Invoice.fxml", "Invoice", "InvoiceController", "pessGetInvoicePlanned", priceGenerator); 
	        	page.openSecondPage();
	    		
	    		this.isGotInvoice = true;
	    	}catch (NullPointerException e) {
	    		errorCase("You have tried to get invoice before searching the order","You should find order first");
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
	    		Integer visitorNumInt = 0;
	    		Double finalPrice = 0.0;
	    		this.txtErrorUnplanned.setText("");

	    		//1. Check the number of visitors
	    		String visitorNum = this.txtEnteredVisitorsNum.getText();
	    		if(visitorNum.trim().isEmpty()) {
	    			errorCaseUnplanned("String for number of visitors is empty","You must enter a number of visitors.");
	    		}else {
	    			// Check if the string contains any digit
			        Pattern pattern = Pattern.compile("\\d");
			        Matcher matcher = pattern.matcher(visitorNum);
			        if (!matcher.find())
			            throw new IllegalArgumentException("Number of visitors should contain only numbers");
			        
			        if (Integer.parseInt(visitorNum) < 1)
			        	throw new IllegalArgumentException("Number of visitors should be greater then 0");

		    		//2. Check checkbox if guided group or private visit
		    		if(this.ckbGuidedGroup.isSelected()) {
		    			isGuidedGroup = true;				
		    			//if guided group - the number of visitors should be no more than 15
		    			if(Integer.parseInt(visitorNum) > 15) {
		    				errorCaseUnplanned("Guided group - more than 15 visitors","Guided group should be up to 15 visitors.");
		    				return;
		    			}
		    			visitorNumInt = Integer.parseInt(visitorNum) + 1;
		    			}
		    		
		    		if(this.ckbPrivateFamVisit.isSelected()) {
		    			isGuidedGroup = false;
		    			visitorNumInt = Integer.parseInt(visitorNum);
		    		}
		    		
		    		if((!this.ckbGuidedGroup.isSelected()) && (!this.ckbPrivateFamVisit.isSelected()) )
		    			throw new IllegalArgumentException("You should choose the type of visit: guided or private/family");

		    		//3. Call PriceGenerator to get the final price
					PriceGenerator priceGenerator = new PriceGenerator();
					priceGenerator.setIsGuidedGroup(isGuidedGroup);
					priceGenerator.setIsPaidInAdvance(false);
					priceGenerator.setIsPlannedVisit(false);
					priceGenerator.setVisitorsNumber(visitorNumInt);
					finalPrice = priceGenerator.generateFinalPrice();
		    		
		    		//4. Generate Invoice and open Invoice in the SECOND window
					
					SecondPage page = new SecondPage(event, "/gui/Invoice.fxml", "Invoice", "InvoiceController", "pessGetInvoiceUnplanned", priceGenerator); 
		        	page.openSecondPage();
			        
		    		this.isGotInvoice = true;
	    		}
	    	}catch (IllegalArgumentException e) {
	    		errorCaseUnplanned(e.getMessage(), e.getMessage());
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
			        
			        if (Integer.parseInt(exitVisitorsNum) < 1)
			        	errorCaseExit("The number of visitors should be greater then 0","The number of visitors should be greater then 0");
			        else {
			        	
			        	ArrayList<String> exitRegMsg = new ArrayList<>();
			        	exitRegMsg.add(this.parkName);
			        	exitRegMsg.add(exitVisitorsNum);
			        	
						//send the number of visitors for performing an exit registration
						ArrayList<Object> arrmsg = new ArrayList<Object>();
						arrmsg.add(new String("ExitRegistration"));
						arrmsg.add(new String("ArrayList<String>"));
						arrmsg.add(exitRegMsg);
						ClientUI.chat.accept(arrmsg);
						
						if (ChatClient.result == false) {
							//exit registration not succeeded
							errorCaseExit("Exit registration failed","Exit registration wasn't performed.");
						}else {
							errorCaseExit("Exit registration succeeded","Exit registration performed successfully.");
						}		        	
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
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("UserLogOut"));
				arrmsg.add(new String("String"));
				arrmsg.add(ChatClient.userName);
				ClientUI.chat.accept(arrmsg);
				
				if(ChatClient.result == true) {
					ChatClient.userName = "";
		        	NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController", "pressBackBtn"); 
		        	page.Next();
				}
				else {
					this.txtError.setText("The user wasn't logged out");
					this.txtErrorUnplanned.setText("The user wasn't logged out");
					this.txtErrorExitReg.setText("The user wasn't logged out");
				}
					

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
