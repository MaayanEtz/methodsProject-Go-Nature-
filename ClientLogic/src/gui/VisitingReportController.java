package gui;

import java.net.URL;
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

public class VisitingReportController implements Initializable{

    @FXML
    private Button btnBack;

    @FXML
    private Button btnSubmit;

    @FXML
    private Text txtError;

    @FXML
    private BarChart<String, Integer> chart;

    @FXML
    private CategoryAxis X;

    @FXML
    private NumberAxis Y;
    
    @FXML
    private Text txtMonth;

    @FXML
    private Text txtYear;
    
    
    private ArrayList<String> dataForReport;
    //data report = {0 - parkName, 1 - day, 2 - month, 3 - year}
    
    //load report data
    public void loadData(ArrayList<String> dataForReport) {
    	/////////CREATE///////////////////
    	this.dataForReport = dataForReport;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	try {
    		
    		////////////////CHECK///////////////////
    		ArrayList<ArrayList<Integer>> array = new ArrayList<>();
    		ArrayList<Integer> a9 = new ArrayList<>();
    		a9.add(14); //guided 9:00
    		a9.add(40); //single 9:00
    		array.add(a9);
    		
    		ArrayList<Integer> a10 = new ArrayList<>();
    		a10.add(23); //guided 10:00
    		a10.add(2); //single 10:00
    		array.add(a10);
    		
    		
    		//guided
			XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<String, Integer>();
			dataSeries1.setName("group visit");
		    dataSeries1.getData().add(new XYChart.Data<>("9", array.get(0).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("10", array.get(1).get(0)));


		    //single
			XYChart.Series<String, Integer> dataSeries2 = new Series<String, Integer>();
			dataSeries2.setName("Single visitor");
		    dataSeries2.getData().add(new XYChart.Data<>("9", array.get(0).get(1)));
		    dataSeries2.getData().add(new XYChart.Data<>("10", array.get(1).get(1)));
		    
		    ////////////REAL CODE //////////////////
    		/*ArrayList<String> dataRep = new ArrayList<>();
    		dataRep.add(dataForReport.get(0)); //park name
    		dataRep.add(dataForReport.get(1)); //day
    		dataRep.add(dataForReport.get(2)); //month
    		dataRep.add(dataForReport.get(3)); //year
    		
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("ShowVisitsReport"));
			arrmsg.add(new String("ArrayList<String>"));
			arrmsg.add(dataRep);
			ClientUI.chat.accept(arrmsg);
			
			if(ChatClient.dataFromServer.get(0).equals("null"))
				throw new NullPointerException("No report data");
					
    		//guided
			XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<String, Integer>();
			dataSeries1.setName("group visit");
		    dataSeries1.getData().add(new XYChart.Data<>("9", ChatClient.intDataFromServer.get(0).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("10", ChatClient.intDataFromServer.get(1).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("11", ChatClient.intDataFromServer.get(2).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("12", ChatClient.intDataFromServer.get(3).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("13", ChatClient.intDataFromServer.get(4).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("14", ChatClient.intDataFromServer.get(5).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("15", ChatClient.intDataFromServer.get(6).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("16", ChatClient.intDataFromServer.get(7).get(0)));
		    dataSeries1.getData().add(new XYChart.Data<>("17", ChatClient.intDataFromServer.get(8).get(0)));


		    //single
			XYChart.Series<String, Integer> dataSeries2 = new Series<String, Integer>();
			dataSeries2.setName("Single visitor");
		    dataSeries1.getData().add(new XYChart.Data<>("9", ChatClient.intDataFromServer.get(0).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("10", ChatClient.intDataFromServer.get(1).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("11", ChatClient.intDataFromServer.get(2).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("12", ChatClient.intDataFromServer.get(3).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("13", ChatClient.intDataFromServer.get(4).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("14", ChatClient.intDataFromServer.get(5).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("15", ChatClient.intDataFromServer.get(6).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("16", ChatClient.intDataFromServer.get(7).get(1)));
		    dataSeries1.getData().add(new XYChart.Data<>("17", ChatClient.intDataFromServer.get(8).get(1)));*/
		    //////////////END REAL CODE////////////////////////

		    chart.getData().addAll(dataSeries1, dataSeries2);
    		


    	}catch (NullPointerException e) {
    		this.txtError.setText(e.getMessage());
    	}catch (Exception e) {
			System.out.println("Error in CancellationReportController: initialize");
			System.out.println(e.getMessage());
		}
    }
    
    @FXML
    void pressBack(ActionEvent event) {
    	try {
    		Button btn = (Button) event.getSource();
    		Stage stage = (Stage) btn.getScene().getWindow();
    		stage.close();
    	}catch (Exception e) {
    		System.out.println("Error in VisitingReportController: pressBack");
    		System.out.println(e.getMessage());
    	}
    }


}

