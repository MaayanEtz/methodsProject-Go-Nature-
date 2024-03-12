package server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import gui.*;
import GoNatureServer.GoNatureServer;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;

	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
				  		
		HomePageController serverFrame = new HomePageController();
		serverFrame.start(primaryStage);
	}
	
	public static void runServer(String p)
	{

		GoNatureServer s = new GoNatureServer(Integer.parseInt(p));
		try {
			s.listen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
