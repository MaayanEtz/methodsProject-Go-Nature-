package gui;

import java.time.LocalDate;
import java.util.ArrayList;

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
import javafx.scene.control.DateCell;


public class CreateOrderFrameController {
	
	public static final int GUIDED_GROUP_MAX_VISITORS_NUMBER = 15;

    @FXML
    private DatePicker SelectDayDp;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnCreate;

    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblResult;

    @FXML
    private ComboBox<String> selectParkCmb;
    @FXML
    private ComboBox<String> selectTimeCmb;
    
    @FXML
    private CheckBox guidedChkb;

    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtVisitorsNum;
    @FXML
    private TextField txtPhoneNumber;
    
    private String visitorId;
    
    //load data
    public void loadData(String visitorId) {
    	
    	this.visitorId = visitorId;
    	
    	//1. Load Parks List  	
    	//////REAL CODE: OPEN//////////////
		//ArrayList<Object> arrmsg = new ArrayList<Object>();
		//arrmsg.add(new String("ParksListGet"));
		//arrmsg.add(new String("Get"));
		//arrmsg.add(new String("Get"));
		//ClientUI.chat.accept(arrmsg);		
		//this.selectParkCmb.getItems().addAll(ChatClient.dataFromServer);
		
    	////////////ONLY FOR CHECK////////////////
		ArrayList<String> parks = new ArrayList<>();
		parks.add("Kew gardens");
		parks.add("St. James park");
		parks.add("Hyde park");
		this.selectParkCmb.getItems().addAll(parks);
		

    	//2. Load available visit days
		
		// Setting the limits for selectable dates
        LocalDate minDate = LocalDate.now().minusDays(0); // 0 days before today
        LocalDate maxDate = LocalDate.now().plusDays(60); // 60 days after today
        
        // Configuring the dayCellFactory to limit selectable dates
        this.SelectDayDp.setDayCellFactory(new javafx.util.Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker picker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(minDate) || date.isAfter(maxDate));
                    }
                };
            }
        });
    	
    	//3. Load available visit time: from 09:00 to 17:00       
		ArrayList<String> times = new ArrayList<>();
		times.add("09:00");
		for(int i=10; i<18; i++) {
			times.add(i + ":00");
		}
		this.selectTimeCmb.getItems().addAll(times);
    	
    	//4. Load if organized guided group
		
    	//////REAL CODE: OPEN//////////////
		//ArrayList<Object> arrmsg = new ArrayList<Object>();
		//arrmsg.add(new String("GroupGuideCheck"));
		//arrmsg.add(new String("String"));
		//arrmsg.add(visitorId);
		//ClientUI.chat.accept(arrmsg);
				
		if(ChatClient.result == true) {
			//the visitor is a guide
			this.guidedChkb.setSelected(true);
			this.guidedChkb.setDisable(true);
		}else {
			//the visitor is not a guide
			this.guidedChkb.setSelected(false);
			this.guidedChkb.setDisable(true);
		}
    	
    	
    }
    
    //Event for "Close" button
    @FXML
    void pressCloseBtn(ActionEvent event) {
		try {
			
	    	NextPage page = new NextPage(event, "/gui/TravellerPage.fxml", "Traveller Page", "TravellerPageController", "pressIdentifyBtn");
	    	page.Next();
		}catch(Exception e) {
			System.out.println("Error in CreateOrderFrameController: pressCloseBtn");
			System.out.println(e.getMessage());}

    }

    //Event for "Create" button
    @FXML
    void pressCreateBtn(ActionEvent event) {
    	
    	//1. If guided group - check the number of visitors. Should be less then 15
    	if (this.guidedChkb.isSelected()) {
    		//the guided group
    		if (Integer.valueOf(this.txtVisitorsNum.getText()) > GUIDED_GROUP_MAX_VISITORS_NUMBER) {
    			this.lblResult.setText("An organized guided group is limited to 15 participants.");
    			return;
    		}
    	}
    		
    	//2. Get data
    	String selectedTimeOption = this.selectTimeCmb.getValue();
    	String selectedDate = SelectDayDp.getValue().toString();
    	
    	ArrayList<String> orderArr = new ArrayList<>();
    	orderArr.add(this.visitorId);
    	orderArr.add(this.selectParkCmb.getValue());
    	orderArr.add(new String(selectedDate + " " + selectedTimeOption + ":00"));
    	orderArr.add(this.txtVisitorsNum.getText());
    	orderArr.add(this.txtEmail.getText());
    	orderArr.add(this.txtPhoneNumber.getText());
    	
    	//3. Check if the day and time of visit is available
    	
    	//////REAL CODE: OPEN//////////////
		//ArrayList<Object> arrmsg = new ArrayList<Object>();
		//arrmsg.add(new String("ParkCheckCapacity"));
		//arrmsg.add(new String("String"));
		//arrmsg.add(this.selectParkCmb.getValue());
		//ClientUI.chat.accept(arrmsg);
    	
    	if(ChatClient.result == false) {
			//park capacity doesn't allow to order
			this.lblResult.setText("The park is full in choosen time. Choose other time or day.");
		}else {	
	    	//4. Create new order    	
	    	//////REAL CODE: OPEN//////////////
			//ArrayList<Object> arrmsg = new ArrayList<Object>();
			//arrmsg.add(new String("OrderCreate"));
			//arrmsg.add(new String("ArrayList<String>"));
			//arrmsg.add(orderArr);
			//ClientUI.chat.accept(arrmsg);
			
			this.lblResult.setText(ChatClient.dataFromServer.get(0));

		}	
    }//End pressCreateBtn

}//END class
