package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class InvoiceController {
    @FXML
    private Button btnClose;

    @FXML
    private Label lblResult;
    @FXML
    
    private Text txtAmountRow1;
    @FXML
    private Text txtAmountRow2;
    @FXML
    private Text txtAmountRow3;
    @FXML
    private Text txtAmountRow4;
    @FXML
    private Text txtAmountRow5;
    @FXML
    private Text txtAmountRow6;
    
    @FXML
    private Text txtBillingTo;

    @FXML
    private Text txtInvoceDate;

    @FXML
    private Text txtPriceRow1;
    @FXML
    private Text txtPriceRow2;
    @FXML
    private Text txtPriceRow3;
    @FXML
    private Text txtPriceRow4;
    @FXML
    private Text txtPriceRow5;
    @FXML
    private Text txtPriceRow6;

    @FXML
    private Text txtQuantityRow1;
    @FXML
    private Text txtQuantityRow2;
    @FXML
    private Text txtQuantityRow3;
    @FXML
    private Text txtQuantityRow4;
    @FXML
    private Text txtQuantityRow5;
    @FXML
    private Text txtQuantityRow6;

    @FXML
    private Text txtRow1;
    @FXML
    private Text txtRow2;
    @FXML
    private Text txtRow3;
    @FXML
    private Text txtRow4;
    @FXML
    private Text txtRow5;
    @FXML
    private Text txtRow6;

    @FXML
    private Text txtTotalPrice;
  
    //Event for "Close" button
    @FXML
    void pressCloseBtn(ActionEvent event) {
    	try {

    	}catch (Exception e) {
    		System.out.println("Error in InvoiceController: pressCloseBtn");
    		System.out.println(e.getMessage());
    	}

    }

}
