package client;
import javafx.application.Application;

import javafx.stage.Stage;

import java.util.Vector;
import client.ClientController;
import gui.SettingsPageController;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		 
						  		
		 SettingsPageController settingsPageFrame = new SettingsPageController();
		 
		 settingsPageFrame.start(primaryStage);
	}
	
	
}
