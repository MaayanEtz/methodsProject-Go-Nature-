package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.text.Text;
import javafx.scene.control.DateCell;

public class ParkManagerController {

	private String parkName;
	@FXML
	private Button btnLogout;

	@FXML
	private Label lblErrorMsg;

	@FXML
	private Button btnCreateReports;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnSendToApprove;

	@FXML
	private Label lblResult;

	@FXML
	private TextField txtTimeOfstay;

	@FXML
	private Text availableSpaceTxt;

	@FXML
	private TextField txtCapacity;

	@FXML
	private TextField txtGapInPark;

	// load data
	public void loadData(String parkName) {
		try {
			this.parkName = parkName;
			// load data
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("ParkCurrentParamsGet"));
			arrmsg.add(new String("String"));
			arrmsg.add(new String(parkName));
			ClientUI.chat.accept(arrmsg);

			if (ChatClient.dataFromServer.equals(null))
				throw new NullPointerException("This park doesn't exists.");
			arrmsg.clear();
			arrmsg.add(ChatClient.dataFromServer);
			txtCapacity.setText((String) arrmsg.get(0));
			txtGapInPark.setText((String) arrmsg.get(1));
			// time of stay change no need for close time
			txtTimeOfstay.setText((String) arrmsg.get(2));
			Integer spaceInPark = ((Integer) arrmsg.get(1)) - ((Integer) arrmsg.get(4));
			availableSpaceTxt.setText(spaceInPark.toString());

		} catch (Exception e) {
			System.out.println("Error in ParkManagerController: loadData");
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void pressLogOut(ActionEvent event) {
		try {
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("UserLogOut"));
			arrmsg.add(new String("String"));
			arrmsg.add(ChatClient.userName);
			ClientUI.chat.accept(arrmsg);

			if (ChatClient.result == true) {
				ChatClient.userName = "";
				NextPage page = new NextPage(event, "/gui/NewHomePage.fxml", "Home Page", "NewHomePageController",
						"pressBackBtn");
				page.Next();
			} else {
				this.lblErrorMsg.setText("The user wasn't logged out");

			}
		} catch (Exception e) {
			System.out.println("Error in ParkManagerController: pressLogOut");
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void pressRefreshbtn(ActionEvent event) {
		try {
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("AvilableSpaceGet"));
			arrmsg.add(new String("String"));
			arrmsg.add(new String(parkName));
			ClientUI.chat.accept(arrmsg);
			if (ChatClient.dataFromServer.equals(null))
				throw new NullPointerException("This park doesn't exists.");
			Integer spaceInPark = ((Integer) arrmsg.get(1)) - ((Integer) arrmsg.get(4));
			availableSpaceTxt.setText(spaceInPark.toString());
		} catch (Exception e) {
			System.out.println("Error in ParkManagerController: pressRefreshbtn");
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void pressSendToApprove(ActionEvent event) {
		// 1. Check capacity
		String checkCapacity = this.txtCapacity.getText();
		if (checkCapacity.trim().isEmpty()) {
			this.lblErrorMsg.setText("String for capacity cant be empty");
		} else {
			// Check if the string contains any digit
			Pattern pattern_cap = Pattern.compile("\\d");
			Matcher matcher_cap = pattern_cap.matcher(checkCapacity);
			if (!matcher_cap.find())
				throw new IllegalArgumentException("capacity should contain only numbers");

			if (Integer.parseInt(checkCapacity) < 1)
				throw new IllegalArgumentException("capacity should be greater then 0");
			// 2. Check the gap
			String checkGap = this.txtGapInPark.getText();
			if (checkGap.trim().isEmpty()) {
				this.lblErrorMsg.setText("String for gap cant be empty");
			} else {
				// Check if the string contains any digit
				Pattern pattern_gap = Pattern.compile("\\d");
				Matcher matcher_gap = pattern_gap.matcher(checkGap);
				if (!matcher_gap.find())
					throw new IllegalArgumentException("gap should contain only numbers");

				if (Integer.parseInt(checkGap) < 1)
					throw new IllegalArgumentException("gap should be greater then 0");
			}

			// 3. Check time of stay
			String checkTimeOfStay = this.txtTimeOfstay.getText();
			if (checkTimeOfStay.trim().isEmpty()) {
				this.lblErrorMsg.setText("String for time of stay cant be empty");
			} else {
				// Check if the string contains any digit
				Pattern pattern_tos = Pattern.compile("\\d");
				Matcher matcher_tos = pattern_tos.matcher(checkTimeOfStay);
				if (!matcher_tos.find())
					throw new IllegalArgumentException("time of stay should contain only numbers");

				if (Integer.parseInt(checkTimeOfStay) < 1)
					throw new IllegalArgumentException("time of stay should be greater then 0");
			}

			try {
				ArrayList<Object> arrmsg = new ArrayList<Object>();
				ArrayList<String> updatePark = new ArrayList<String>();
				arrmsg.add(new String("ParkNewParamsUpdate"));
				arrmsg.add(new String("ArrayList<String>"));
				updatePark.add(new String(parkName));
				updatePark.add(new String(txtCapacity.getText()));
				updatePark.add(new String(txtGapInPark.getText()));
				updatePark.add(new String(txtTimeOfstay.getText()));
				arrmsg.add(updatePark);

				ClientUI.chat.accept(arrmsg);
				if (ChatClient.dataFromServer.equals(false))
					throw new NullPointerException("Update manager doesn't succesful.");
			} catch (Exception e) {
				System.out.println("Error in ParkManagerController: pressSentTomanager");
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	void pressCreateReports(ActionEvent event) {
    	try {
        	NextPage page = new NextPage(event, "/gui/ParkManagerReportsPage.fxml", "", "ParkManagerReportsPageController", "pressCreateReports", parkName); 
			page.Next();
		} catch (Exception e) {
			System.out.println("Error in ParkManagerController: pressCreateReports");
			System.out.println(e.getMessage());
		}
	}

}