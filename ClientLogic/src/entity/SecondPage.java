package entity;

import gui.InvoiceController;
import gui.TotalVisitorsNumberReportPageController;
import gui.UsageReportPageController;
import gui.ChoiceWindowController;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SecondPage {
	
	private ActionEvent event;
	private String path;
	private String title;
	private String controller;
	private Object data;
	private String method;
	
	//Constructors
	public SecondPage(ActionEvent event, String path, String title, String controller, String method) {
		this.event = event; 
		this.path = path;
		this.title = title;
		this.controller = controller;
		this.method = method;
	}
	
	public SecondPage(ActionEvent event, String path, String title, String controller, String method, Object data) {
		this.event = event; 
		this.path = path;
		this.title = title;
		this.controller = controller;
		this.method = method;
		this.data = data;
	}
	
	//function for changing pages
	public void openSecondPage() throws Exception {
		try {
			
			FXMLLoader secondLoader = new FXMLLoader(getClass().getResource(path));
			Pane secondRoot = secondLoader.load();
			
			switch (controller) {
				case "InvoiceController": {
					InvoiceController invoiceController = secondLoader.getController();
					invoiceController.genarateInvoice((PriceGenerator)data);
					break;}
				
				case "ChoiceWindowController": {
					ChoiceWindowController choiceWindowController = secondLoader.getController();
					choiceWindowController.loadData((ArrayList<String>)data);
					break;}
				
				case "TotalVisitorsNumberReportPageController": {
					TotalVisitorsNumberReportPageController totalVisitorsNumberReportPageController = secondLoader.getController();
					totalVisitorsNumberReportPageController.loadData((ArrayList<String>)data);
					break;}
				
				case "UsageReportPageController": {
					UsageReportPageController usageReportPageController = secondLoader.getController();
					usageReportPageController.loadData((ArrayList<String>)data);
					break;}
			}
			

	        Stage secondStage = new Stage();
	        secondStage.setTitle(title);
	        Scene secondScene = new Scene(secondRoot);
	        secondStage.setScene(secondScene);
	        secondStage.show();
		}catch (Exception e) {
			System.out.println("Error in SecondPage: openSecondPage");
			System.out.println(e.getMessage());
		}
	}

}
