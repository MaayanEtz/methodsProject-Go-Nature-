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
import javafx.scene.control.DateCell;

public class ParkManagerController 
{


     @FXML
        private Button btnLogout;

        @FXML
        private Button btnRefresh;

        @FXML
        private Button btnSendToApprove;

        @FXML
        private Label lblResult;

        @FXML
        private ComboBox<String> selectTimeCmb;

        @FXML
        private TextField txtAvailableSpace;

        @FXML
        private TextField txtCapacity;

        @FXML
        private TextField txtGapInPark;

        //load data
        public void loadData() 
        {

        }

        @FXML
        void pressLogout(ActionEvent event) 
        {


        }

        @FXML
        void pressRefreshbtn(ActionEvent event) 
        {

        }
        @FXML
        void pressSentTomanager(ActionEvent event) 
        {

        }


    }