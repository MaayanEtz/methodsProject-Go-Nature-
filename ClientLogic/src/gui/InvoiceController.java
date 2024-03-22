package gui;

import java.text.DecimalFormat;
import java.time.LocalDate;

import entity.PriceGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class InvoiceController {
    @FXML
    private Label lblResult;
    @FXML
    
    private Text txtAmountRow1, txtAmountRow2, txtAmountRow3, txtAmountRow4, txtAmountRow5, txtAmountRow6;

    @FXML
    private Text txtInvoceDate;

    @FXML
    private Text txtPriceRow1, txtPriceRow2, txtPriceRow3, txtPriceRow4, txtPriceRow5, txtPriceRow6;

    @FXML
    private Text txtQuantityRow1, txtQuantityRow2, txtQuantityRow3, txtQuantityRow4, txtQuantityRow5, txtQuantityRow6;

    @FXML
    private Text txtRow1, txtRow2, txtRow3, txtRow4, txtRow5, txtRow6;

    @FXML
    private Text txtTotalPrice;
 
    //load invoice
    public void genarateInvoice(PriceGenerator priceGenerator) {
    	try {
			
			//1. Invoice date
			LocalDate today = LocalDate.now();
			this.txtInvoceDate.setText(today.toString());
			
			//2. Full price
			this.txtRow1.setText("Full price");
			this.txtPriceRow1.setText(String.valueOf(priceGenerator.getFullPrice()));
			this.txtQuantityRow1.setText(String.valueOf(priceGenerator.getVisitorsNumber()));
			this.txtAmountRow1.setText(String.valueOf(priceGenerator.getFullPrice() * priceGenerator.getVisitorsNumber()));
			
			//3. Guide
			this.txtRow2.setText("Guided visit discount");
			if(priceGenerator.getIsGuidedGroup())
				this.txtQuantityRow2.setText("1");
			else
				this.txtQuantityRow2.setText("0");
			
			//4. Discounts if has
			this.txtRow3.setText("Planned visit discount");
			if(priceGenerator.getIsPlannedVisit())
				this.txtQuantityRow3.setText("1");
			else
				this.txtQuantityRow3.setText("0");
			
			this.txtRow4.setText("Payment in advance discount");
			if(priceGenerator.getIsPaidInAdvance())
				this.txtQuantityRow4.setText("1");
			else
				this.txtQuantityRow4.setText("0");
				
			//5. Total price
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			this.txtTotalPrice.setText(String.valueOf(decimalFormat.format(priceGenerator.getFinalPrice())));
			
		} catch (Exception e) {
			System.out.println("Error in InvoiceController: genarateInvoice");
			System.out.println(e.getMessage());
		}
    }

}
