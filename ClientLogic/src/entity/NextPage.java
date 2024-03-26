package entity;

import java.util.ArrayList;

import gui.*;
import gui.FindOrderFrameController.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NextPage {
	private ActionEvent event;
	private String path;
	private String title;
	private String controller;
	private Object data;
	private String method;
	
	//Constructors
	public NextPage(ActionEvent event, String path, String title, String controller, String method) {
		this.event = event; 
		this.path = path;
		this.title = title;
		this.controller = controller;
		this.method = method;
	}
	
	public NextPage(ActionEvent event, String path, String title, String controller, String method, Object data) {
		this.event = event; 
		this.path = path;
		this.title = title;
		this.controller = controller;
		this.method = method;
		this.data = data;
	}
	
	
	//function for changing pages
	public void Next() throws Exception {
		try{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		Pane root = loader.load();
		
		switch (controller) {
			case "FindOrderFrameController": {
				FindOrderFrameController findOrderFrameController = loader.getController();
				
				if(data == FindOrderFrameController.Action.EDIT)
					findOrderFrameController.setAction(FindOrderFrameController.Action.EDIT);

				if(data == FindOrderFrameController.Action.CANCEL)
					findOrderFrameController.setAction(FindOrderFrameController.Action.CANCEL);
				break;}
			
			case "IdentifyPageController": {
				IdentifyPageController identifyPageController = loader.getController();
				break;}
			
			case "LoginController": {
				LoginController loginController = loader.getController();
				break;}
			
			case "NewHomePageController": {
				NewHomePageController newHomePageController = loader.getController();
				break;}
			
			case "OrderFrameController": {
				OrderFrameController orderFrameController = loader.getController();
				if(data instanceof Order)
					orderFrameController.loadData((Order)data);
				break;}
			
			case "TravellerPageController": {
				TravellerPageController travellerPageController = loader.getController();
				break;}
			
			case "SettingsPageController": {
				SettingsPageController settingsPageController = loader.getController();
				break;}
			
			case "CancelOrderFrameController": {
				CancelOrderFrameController cancelOrderFrameController = loader.getController();
				if(data instanceof Order)
					cancelOrderFrameController.loadOrder((Order)data);
				break;}
			
			case "RespondWindowController": {
				RespondWindowController respondWindowController = loader.getController();
				if (data instanceof String) {
					respondWindowController.setImage((String) data);
					respondWindowController.setLabel((String) data);
				}
				break;}
			
			case "CreateOrderFrameController": {
				CreateOrderFrameController createOrderFrameController = loader.getController();
				createOrderFrameController.loadData();
				break;}
			
			
			case "ParkWorkerMenuController":{
				ParkWorkerMenuController parkWorkerMenuController = loader.getController();
				//send the name of the park!
				if (data instanceof String)
					parkWorkerMenuController.loadData((String)data);
				break;}
			
			case "DepartmentWorkerController": {
				DepartmentWorkerController departmentWorkerController = loader.getController();
				break;}
						
			case "DepartmentManagerController":{
				DepartmentManagerController departmentManagerController = loader.getController();
				//send the name of the park!
				if (data instanceof String)
					departmentManagerController.loadData((String)data);
				break;}
			
			case "ParkManagerController":{
				ParkManagerController parkManagerController = loader.getController();
				//send the name of the park!
				if (data instanceof String)
					parkManagerController.loadData((String)data);
				break;}
			
			case "ParkManagerReportsPageController": {
				ParkManagerReportsPageController parkManagerReportsPageController = loader.getController();
				if (data instanceof String)
					parkManagerReportsPageController.loadData((String)data);
				break;}
			

			
			default: {System.out.println("No such controller in NextPage: Next");}
		}
		
		Scene scene = new Scene(root);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
		}catch (Exception e) {
			//System.out.println("Error in " + controller + ": "+ method);
			System.out.println("Error in NextPage: Next");
			System.out.println(e.getMessage());
		}
	}

}
