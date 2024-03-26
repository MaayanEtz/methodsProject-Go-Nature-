package gui;

import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import client.ClientUI;
import entity.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FindOrderFrameController {
	
	//labels
	@FXML
	private Label lblEnteredOrderNum;
	@FXML
	private Label lblResult;
		
	//text fields
	@FXML
	private TextField txtEnteredOrderNum;
	
	//buttons
	@FXML
	private Button btnFind=null;
	@FXML
	private Button btnBack=null;
	
	//Image
	@FXML
	private ImageView imgGoNature;
	
	//get entered order number
	private String getEnteredOrderNum() {
		return txtEnteredOrderNum.getText();
	}
	
	//set text to Result label
	public void loadResul (String str) {
		try {
			this.lblResult.setText(str);
		}
		catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: loadLabel");
			System.out.println(e.getMessage());
		}	
	}
	

	int globalOren = 0;
	//Event for "Find" button
	public void pressFindBtn (ActionEvent event) throws Exception {
		try {
			String orderNumber;
			
			orderNumber = getEnteredOrderNum();
			
			//prepare the window to show the result
			FXMLLoader loader = new FXMLLoader();
			Stage primaryStage = new Stage();
			
			// Oren Testing Connect
//			String username =  orderNumber.split(",")[0];
//			String password =  orderNumber.split(",")[1];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("UserLogin"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(username,password)));
			
			
			// OREN TEST is logged in
			// Oren Testing Connect
//			ArrayList<Object> arrmsg;
//			if (globalOren == 0) {
//				String username =  orderNumber.split(",")[0];
//				String password =  orderNumber.split(",")[1];
//				arrmsg = new ArrayList<>();
//				arrmsg.add(new String("UserLogin"));
//				arrmsg.add(new String("ArrayList<String>"));
//				arrmsg.add(new ArrayList<String>(Arrays.asList(username,password)));
//				globalOren +=1;
//			}else {
//				if (globalOren == 1) {
//					String username =  orderNumber.split(",")[0];
//					arrmsg = new ArrayList<>();
//					arrmsg.add(new String("IsLoggedIn"));
//					arrmsg.add(new String("String"));
//					arrmsg.add(username);
//					globalOren+=1;
//				}else {
//					String username =  orderNumber.split(",")[0];
//					arrmsg = new ArrayList<>();
//					arrmsg.add(new String("UserLogOut"));
//					arrmsg.add(new String("String"));
//					arrmsg.add(username);
//				}
//			
//			}
			
			
			// OREN TEST EXIT REGISTRATION
//			ArrayList<Object> arrmsg;
//			String parkName =  orderNumber.split(",")[0];
//			String visitor_number =  orderNumber.split(",")[1];
//			arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ExitRegistration"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(parkName,visitor_number)));
			
			// OREN TESTING GET
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("OrderGet"));
//			arrmsg.add(new String("String"));
//			arrmsg.add("21");
			
//			// OREN TESTING OrderedEnter
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("OrderedEnter"));
//			arrmsg.add(new String("String"));
//			arrmsg.add("21");
			
//			// OREN TESTING UnplannedEnter
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("UnplannedEnter"));
//			arrmsg.add(new String("ArrayList<String>"));
//			ArrayList<String> arr_str = new ArrayList<>(Arrays.asList("Hyde Park","16"));
//			arrmsg.add(arr_str);
			
			// Testing Delete
			//String order_id_to_del = orderNumber.split(",")[0];
			
			// Testing Delete
			//String update_test = orderNumber.split(",")[0];
			// Testing Create
			
			//String order_id = orderNumber.split(",")[0];
			
			// CREAT TEST
			String visitor_id = orderNumber.split(",")[0];
			String park_name = orderNumber.split(",")[1];
			String time_of_visit = orderNumber.split(",")[2];
			String visitor_number = orderNumber.split(",")[3];
			String visitor_email = orderNumber.split(",")[4];
			String visitor_phone = orderNumber.split(",")[5];
			ArrayList<Object> arrmsg = new ArrayList<>();
			arrmsg.add(new String("OrderCreate"));
			arrmsg.add(new String("ArrayList<String>"));
			arrmsg.add(new ArrayList<String>(Arrays.asList(visitor_id,park_name,time_of_visit,visitor_number,visitor_email,visitor_phone)));
			//
			
			// UPDATE TEST
//			String order_id = orderNumber.split(",")[0];
//			String visitor_id = orderNumber.split(",")[1];
//			String park_name = orderNumber.split(",")[2];
//			String time_of_visit = orderNumber.split(",")[3];
//			String visitor_number = orderNumber.split(",")[4];
//			String visitor_email = orderNumber.split(",")[5];
//			String visitor_phone = orderNumber.split(",")[6];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("OrderUpdate"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(order_id,visitor_id,park_name,time_of_visit,visitor_number,visitor_email,visitor_phone)));
			//
			
			// Test Guid
//			arrmsg.add(new String("GroupGuideCheck"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(update_test);
			
			// Test GetPrices:
//			ArrayList<Object> arrmsg;
//			arrmsg = new ArrayList<>();
//			arrmsg.add(new String("GetPrices"));
//			arrmsg.add(new String("Doesnt Matter"));
//			arrmsg.add(new String("Doesnt Matter"));
			
			
			// TEST EnterWaitList
//			String order_id =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("EnterWaitList"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(order_id);
			
			// Test getPaidInAdvance
//			String order_id =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("getPaidInAdvance"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(order_id);
			
			// Test setPaidInAdvance
//			String order_id =  orderNumber.split(",")[0];
//			String trueorfalse = orderNumber.split(",")[1];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("setPaidInAdvance"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(order_id, trueorfalse)));
			
			
			// Test park_current_params_get
//			String order_id =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ParkCurrentParamsGet"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(order_id);
			
			// Test park_current_params_update
//			String park_name =  orderNumber.split(",")[0];
//			String capacity =  orderNumber.split(",")[1];
//			String diff =  orderNumber.split(",")[2];
//			String visit_time =  orderNumber.split(",")[3];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ParkCurrentParamsUpdate"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(park_name,capacity, diff, visit_time)));
//
//
			
			// Test new par kl params get
//			String park_name =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ParkNewParamsGet"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(park_name);
//			
			// Test ParkCheckIfApproveRequired
//			String park_name =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ParkCheckIfApproveRequired"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(new String(""));
			
			// AvilableSpaceGet test
//			String park_name =  orderNumber.split(",")[0];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("AvilableSpaceGet"));
//			arrmsg.add(new String("String"));
//			arrmsg.add(park_name);
			
//			
			
			// Test park_current_params_update
//			String park_name =  orderNumber.split(",")[0];
//			String capacity =  orderNumber.split(",")[1];
//			String diff =  orderNumber.split(",")[2];
//			String visit_time =  orderNumber.split(",")[3];
//			ArrayList<Object> arrmsg = new ArrayList<>();
//			arrmsg.add(new String("ParkNewParamsUpdate"));
//			arrmsg.add(new String("ArrayList<String>"));
//			arrmsg.add(new ArrayList<String>(Arrays.asList(park_name,capacity, diff, visit_time)));
			
			// TEST HERE
			ClientUI.chat.accept(arrmsg);
			
//			if(orderNumber.trim().isEmpty()) {
//				System.out.println("You must enter an order number");
//				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//				Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
//				FindOrderFrameController findOrderFrameController = loader.getController();			
//				findOrderFrameController.loadResul("You must enter an order number");
//				Scene scene = new Scene(root);
//				
//				primaryStage.setTitle("Find Order");
//				primaryStage.setScene(scene);		
//				primaryStage.show();
//			}
//			else {
//				//send order number for searching
//				ClientUI.chat.get(orderNumber);
//				
//				if(ChatClient.order == null) {
//					System.out.println("Order number not found");
//					
//					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window	
//					Pane root = loader.load(getClass().getResource("/gui/FindOrderFrame.fxml").openStream());
//					FindOrderFrameController findOrderFrameController = loader.getController();			
//					findOrderFrameController.loadResul("Order number does not exist in the system.");
//					Scene scene = new Scene(root);
//					primaryStage.setTitle("Find Order");
//					primaryStage.setScene(scene);		
//					primaryStage.show();
//				}
//				else {	
//					System.out.println("Order number found");
//					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//					
//					Pane root = loader.load(getClass().getResource("/gui/OrderForm.fxml").openStream());
//					OrderFrameController orderFrameController = loader.getController();
//					orderFrameController.loadOrder(ChatClient.order);
//					Scene scene = new Scene(root);
//
//					primaryStage.setTitle("Order Form");	
//					primaryStage.setScene(scene);		
//					primaryStage.show();
//				}
//			}		
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//Even for "Back" button
	public void pressBackBtn(ActionEvent event) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
			
			HomePageController homePageController = new HomePageController();
			loader.setController(homePageController);
			homePageController.start(primaryStage);
			
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("Home Page");

			primaryStage.setScene(scene);		
			primaryStage.show();
		}catch (Exception e) {
			System.out.println("Error in FindOrderFrameController: pressBackBtn");
			System.out.println(e.getMessage());
		}
		
	}

}
