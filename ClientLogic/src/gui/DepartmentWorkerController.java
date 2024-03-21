package gui;

import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import client.ClientUI;
import entity.NextPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class DepartmentWorkerController {

	@FXML
	private Text errorTxt;

	@FXML
	private TextField guide_id;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnGuideReg;

	@FXML
	private Button btnLogout;

	@FXML
	void pressGuideRegBtn(ActionEvent event) {
		try {
			String guide = guide_id.getText();
			if (guide.trim().isEmpty()) {
				msgCase("You must enter valid guide id", "You must enter valid guide id");
			} else {
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				arrmsg = new ArrayList<Object>();
				arrmsg.add(new String("GroupGuideCheck"));
				arrmsg.add(new String("String"));
				arrmsg.add(guide);
				ClientUI.chat.accept(arrmsg);
				
				if(ChatClient.result == true) {
					//the guide is already registered
					msgCase("The guide is already registered in the system", "The guide is already registered in the system, please enter a diffrent guide id");
				} else {
				ArrayList<String> RegistrationDetails = new ArrayList<>(Arrays.asList(guide));

				arrmsg.clear();
				arrmsg.add(new String("GuideRegistration"));
				arrmsg.add(new String("ArrayList<String>"));
				arrmsg.add(RegistrationDetails);
				ClientUI.chat.accept(arrmsg);
				if(ChatClient.result == true) {
					//the guide registered successfully
					msgCase("The guide registered successfully", "The guide registered successfully!");
					errorTxt.setStyle("-fx-text-fill: green;");
				}
				else {
					msgCase("An error occurred, in Guide Registration ", "An error occurred, the guide is not registered in the system");
				}
			}
		}
	} catch (Exception e) {
			System.out.println("Error in DepartmentWorkerController: GuideRegistration");
	}
}

	@FXML
	void pressLogoutBtn(ActionEvent event) { //// NEED TO ADD LOGOUT
		
	}

	// Event for "Back" button
//	public void pressBackBtn(ActionEvent event) throws Exception {
//		try {
//			NextPage page = new NextPage(event, "/gui/TravellerPage.fxml", "Traveller Page", "TravellerPageController",
//					"pressBackBtn"); // need to add path and title
//			page.Next();
//		} catch (Exception e) {
//			System.out.println("Error in FindOrderFrameController: pressBackBtn");
//			System.out.println(e.getMessage());
//		}
//
//	}

	// private method for messages
	private void msgCase(String strPrint, String strSet) {
		System.out.println(strPrint);
		errorTxt.setText(strSet);
		guide_id.setText("");
	}
}
