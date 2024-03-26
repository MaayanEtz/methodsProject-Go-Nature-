package gui;

import java.util.ArrayList;
import java.util.Random;

import client.ChatClient;
import client.ClientUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UsageReportPageController {
	

    @FXML
    private TableColumn<ReportData, String> colDate;

    @FXML
    private TableColumn<ReportData, String> colReason;

    @FXML
    private TableView<ReportData> tblUsageReport;

    @FXML
    private Button btnClose;

    @FXML
    private Text txtMonth;

    @FXML
    private Text txtYear;
    
    private ArrayList<String> reasons;
    
    public UsageReportPageController() {
        reasons = new ArrayList<>();
        reasons.add("Low attendance due to bad weather");
        reasons.add("Maintenance work being carried out");
        reasons.add("Special event hosted outside the park");
        reasons.add("Local holiday observed in the area");
        reasons.add("Nearby road closure affecting access");
        reasons.add("School trip cancellations");
        reasons.add("Reduced hours due to staffing issues");
        reasons.add("Promotional discounts not attracting visitors");
    }
    
    private ArrayList<String> dataForReport;
    //data report = {0 - parkName, 1 - month, 2 - year
    
    //load data to the page
    public void loadData(ArrayList<String> dataForReport) {
		try {		
			this.dataForReport = dataForReport;
			this.txtMonth.setText(dataForReport.get(1));
			this.txtYear.setText(dataForReport.get(2));
			
			//get dates from DB
			ArrayList<String> dataForRep = new ArrayList<String>();
			dataForRep.add(dataForReport.get(0)); //parkName
			dataForRep.add(dataForReport.get(1)); //month
			dataForRep.add(dataForReport.get(2)); //year
			
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("GetDatesUsageReport"));
			arrmsg.add(new String("ArrayList<String>"));
			arrmsg.add(new String("Get"));
			ClientUI.chat.accept(dataForRep);
			
			////////////////CHECK//////////////////////
			ObservableList<ReportData> reportData = FXCollections.observableArrayList();
			if(!ChatClient.dataFromServer.get(0).equals("null")) {
				for(String data : ChatClient.dataFromServer) {
					reportData.add(new ReportData(data));
				}	
			}
			this.loadTableData(reportData);
			
			//reportData.add(new ReportData("21-12-1998"));
		
		} catch (Exception e) {
			System.out.println("Error in UsageReportPageController: loadData");
			System.out.println(e.getMessage());
		}
    	
    }

    
    //Event for "Close" button
    @FXML
    void pressCloseBtn(ActionEvent event) {
    	try {
    		Button btn = (Button) event.getSource();
    		Stage stage = (Stage) btn.getScene().getWindow();
    		stage.close();
    	}catch (Exception e) {
    		System.out.println("Error in UsageReportPageController: pressCloseBtn");
    		System.out.println(e.getMessage());
    	}
    }
    
    //private class for report data
    private class ReportData {
    	private SimpleStringProperty date;
    	private SimpleStringProperty reason;
    	
    	public ReportData(String date) {
    		this.date = new SimpleStringProperty(date);
    		Random rand = new Random();
    		this.reason = new SimpleStringProperty(reasons.get(rand.nextInt(reasons.size())));
    	}
    	
    	public SimpleStringProperty date() {
    		return date;
    	}
    	
    	public SimpleStringProperty reason() {
    		return reason;
    	}
    }//end private class
    
    //load data to the table
	private void loadTableData(ObservableList<ReportData> repData) {
		try {
			this.colDate.setCellValueFactory(cellData -> cellData.getValue().date());
			this.colReason.setCellValueFactory(cellData -> cellData.getValue().reason());
			this.tblUsageReport.setItems(repData);

		} catch (Exception e) {
			System.out.println("Error in ServerPortFrameController: loadTableData");
			System.out.println(e.getMessage());
		}
	}
    
}
