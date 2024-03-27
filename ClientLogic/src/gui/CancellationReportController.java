package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CancellationReportController implements Initializable{
    @FXML
    private CategoryAxis X;

    @FXML
    private NumberAxis Y;

    @FXML
    private Button btnBack;

    @FXML
    private BarChart<String, Integer> chart;

    @FXML
    private Text txtError;

    @FXML
    private Text txtMonth;

    @FXML
    private Text txtYear;
    @FXML
    private Text txtAvFully;

    @FXML
    private Text txtAvNotFully;
    
    private ArrayList<String> dataForReport;
    //data report = {0 - parkName\"All parks", 1 - day_from, 2 - month_from, 3 - year_from,
    //								4 - day_to, 5 - month_to, 6 - year_to}
    
    //load report data
    public void loadData(ArrayList<String> dataForReport) {
    	/////////OPEN///////////////////
    	//this.dataForReport = dataForReport;
    	
    	////////////////CHECK///////////////////
		this.dataForReport = new ArrayList<>();
		this.dataForReport.add("Central Park");
		this.dataForReport.add("01");
		this.dataForReport.add("03");
		this.dataForReport.add("2011");
		this.dataForReport.add("07");
		this.dataForReport.add("03");
		this.dataForReport.add("2011");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	try {
    		
    		////////////////CHECK///////////////////
    		dataForReport = new ArrayList<>();
    		dataForReport.add("Central Park");
    		dataForReport.add("01");
    		dataForReport.add("03");
    		dataForReport.add("2011");
    		dataForReport.add("07");
    		dataForReport.add("03");
    		dataForReport.add("2011");
    		
    		ArrayList<ArrayList<Integer>> array = new ArrayList<>();
    		ArrayList<Integer> avarage = new ArrayList<>();
    		avarage.add(14);
    		avarage.add(20);
    		array.add(avarage);
    		
    		ArrayList<Integer> day1 = new ArrayList<>();
    		day1.add(14);
    		day1.add(6);
    		array.add(day1);
    		
    		ArrayList<Integer> day2 = new ArrayList<>();
    		day2.add(3);
    		day2.add(7);
    		array.add(day2);
    		
			//calculate the difference between the dates
	        String date1String = new String(dataForReport.get(3)+"-"+dataForReport.get(2)+"-"+dataForReport.get(1)); // First date in YYYY-MM-DD format
	        String date2String = new String(dataForReport.get(6)+"-"+dataForReport.get(5)+"-"+dataForReport.get(4)); // Second date in YYYY-MM-DD format
	        LocalDate date1 = LocalDate.parse(date1String);
	        LocalDate date2 = LocalDate.parse(date2String);
	        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
	        
	        //guided
	        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<String, Integer>();
	        for(int i = 1; i <= daysBetween; i++) {
	        	dataSeries1.setName("cancelled");
	        	
	        	 // Iterate over the dates
	        	LocalDate currentDate = date1;
	        	while (!currentDate.isAfter(date2)) {
	        		dataSeries1.getData().add(new XYChart.Data<>(currentDate.toString(), array.get(i).get(0)));
	        		currentDate = currentDate.plusDays(1); // Move to the next day
	        	}
	        }
	        
	        
	        //single
	        XYChart.Series<String, Integer> dataSeries2 = new Series<String, Integer>();
	        for(int i = 1; i <= daysBetween; i++) {
	        	//single
	        	dataSeries2.setName("not fully cancelled");
	        	
	        	 // Iterate over the dates
	        	LocalDate currentDate = date1;
	        	while (!currentDate.isAfter(date2)) {
	        		dataSeries1.getData().add(new XYChart.Data<>(currentDate.toString(), array.get(i).get(1)));
	        		currentDate = currentDate.plusDays(1); // Move to the next day
	        	}
	        }
    		
	        //set the average to the label
	        this.txtAvFully.setText(array.get(0).get(0).toString());
	        this.txtAvNotFully.setText(array.get(0).get(1).toString());
    		

    		
    		///////////////////REAL CODE//////////////////////
    		/*if(dataForReport.get(0).equals("All parks")) {
        		ArrayList<String> dataRep = new ArrayList<>();
        		dataRep.add(dataForReport.get(1)); //day_from
        		dataRep.add(dataForReport.get(2)); //month_from
        		dataRep.add(dataForReport.get(3)); //year_from
        		
        		dataRep.add(dataForReport.get(4)); //day_to
        		dataRep.add(dataForReport.get(5)); //month_to
        		dataRep.add(dataForReport.get(6)); //year_to
        		
    			ArrayList<Object> arrmsg = new ArrayList<Object>();
    			arrmsg.add(new String("ShowCancellationReportAllParks"));
    			arrmsg.add(new String("ArrayList<String>"));
    			arrmsg.add(dataRep);
    			ClientUI.chat.accept(arrmsg);
    		}else {
        		ArrayList<String> dataRep = new ArrayList<>();
        		dataRep.add(dataForReport.get(0)); //park name
        		dataRep.add(dataForReport.get(1)); //day_from
        		dataRep.add(dataForReport.get(2)); //month_from
        		dataRep.add(dataForReport.get(3)); //year_from
        		
        		dataRep.add(dataForReport.get(4)); //day_to
        		dataRep.add(dataForReport.get(5)); //month_to
        		dataRep.add(dataForReport.get(6)); //year_to
        		
    			ArrayList<Object> arrmsg = new ArrayList<Object>();
    			arrmsg.add(new String("ShowCancellationReport"));
    			arrmsg.add(new String("ArrayList<String>"));
    			arrmsg.add(dataRep);
    			ClientUI.chat.accept(arrmsg);
    		}
    		
			if(ChatClient.dataFromServer.get(0).equals("null"))
				throw new NullPointerException("No report data");
			
			//calculate the difference between the dates
	        String date1String = new String(dataForReport.get(3)+"-"+dataForReport.get(2)+"-"+dataForReport.get(1)); // First date in YYYY-MM-DD format
	        String date2String = new String(dataForReport.get(6)+"-"+dataForReport.get(5)+"-"+dataForReport.get(4)); // Second date in YYYY-MM-DD format
	        LocalDate date1 = LocalDate.parse(date1String);
	        LocalDate date2 = LocalDate.parse(date2String);
	        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
	        
	        //guided
	        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<String, Integer>();
	        for(int i = 1; i <= daysBetween; i++) {
	        	dataSeries1.setName("cancelled");
	        	
	        	 // Iterate over the dates
	        	LocalDate currentDate = date1;
	        	while (!currentDate.isAfter(date2)) {
	        		dataSeries1.getData().add(new XYChart.Data<>(currentDate.toString(), ChatClient.intDataFromServer.get(i).get(0)));
	        		currentDate = currentDate.plusDays(1); // Move to the next day
	        	}
	        }
	        
	        
	        //single
	        XYChart.Series<String, Integer> dataSeries2 = new Series<String, Integer>();
	        for(int i = 1; i <= daysBetween; i++) {
	        	//single
	        	dataSeries2.setName("not fully cancelled");
	        	
	        	 // Iterate over the dates
	        	LocalDate currentDate = date1;
	        	while (!currentDate.isAfter(date2)) {
	        		dataSeries1.getData().add(new XYChart.Data<>(currentDate.toString(), ChatClient.intDataFromServer.get(i).get(1)));
	        		currentDate = currentDate.plusDays(1); // Move to the next day
	        	}
	        }
	        
	        //set the average to the label
	        this.txtAvFully.setText(ChatClient.intDataFromServer.get(0).get(0).toString());
	        this.txtAvNotFully.setText(ChatClient.intDataFromServer.get(0).get(1).toString());*/
		    //////////////END REAL CODE////////////////////////
    		
    		
			chart.getData().addAll(dataSeries1, dataSeries2);
    		
    	}catch(NullPointerException e) {
    		this.txtError.setText(e.getMessage());
    	}catch(Exception e) {
    		System.out.println("Error in CancellationReportController: initialize");
    		System.out.println(e.getMessage());
    	}
    	
    }
    
    
    
    
    
    //Event for "Back" button
    @FXML
    void pressBack(ActionEvent event) {
    	try {
    		Button btn = (Button) event.getSource();
    		Stage stage = (Stage) btn.getScene().getWindow();
    		stage.close();
    	}catch (Exception e) {
    		System.out.println("Error in CancellationReportController: pressBack");
    		System.out.println(e.getMessage());
    	}
    }
    
    
}
