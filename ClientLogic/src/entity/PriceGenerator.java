package entity;

import java.util.ArrayList;

import client.ChatClient;
import client.ClientUI;

public class PriceGenerator {
	
	private Integer finalPrice;
	private Integer fullPrice;
	private Integer discountPrivateFamilyPlanned;
	private Integer discountPrivateFamilyUnplanned;
	private Integer discountGroupPlanned;
	private Integer discountGroupUnplanned;
	private Integer discountPaymentInAdvance;
	
	private Boolean isGuidedGroup;
	private Integer visitorsNumber;
	private Boolean isPayedInAdvance;
	private Boolean isPlannedVisit;
	
	//getters
	public Integer getFinalPrice() {
		return finalPrice;
	}
	public Integer getFullPrice() {
		return fullPrice;
	}
	public Integer getDiscountPrivateFamilyPlanned() {
		return discountPrivateFamilyPlanned;
	}
	public Integer getDiscountPrivateFamilyUnplanned() {
		return discountPrivateFamilyUnplanned;
	}
	public Integer getDiscountGroupPlanned() {
		return discountGroupPlanned;
	}
	public Integer getDiscountGroupUnplanned() {
		return discountGroupUnplanned;
	}
	public Integer getDiscountPaymentInAdvance() {
		return discountPaymentInAdvance;
	}
	
	
	//setters
	public void setIsGuidedGroup(Boolean isGuidedGroup) {
		this.isGuidedGroup = isGuidedGroup;
	}
	public void setVisitorsNumber(Integer visitorsNumber) {
		this.visitorsNumber = visitorsNumber;
	}
	public void setIsPayedInAdvance(Boolean isPayedInAdvance) {
		this.isPayedInAdvance = isPayedInAdvance;
	}
	public void setIsPlannedVisit(Boolean isPlannedVisit) {
		this.isPlannedVisit = isPlannedVisit;
	}
	
	//public method for generating the final price
	public Integer generateFinalPrice() {
		try {
			//to set the prices
			setPrices();
			
			if(isPlannedVisit) {
				//planned visit
				if(isGuidedGroup) {
					//planned guided group
				}else {
					//planned private or family visit
					finalPrice = fullPrice*(1-(discountPrivateFamilyPlanned/100));
				}
			}else {
				//unplanned visit
				if(isGuidedGroup) {
					//unplanned guided group
					
				}else {
					//unplanned private or family visit
					finalPrice = visitorsNumber * fullPrice;
				}
			}
		} catch (Exception e) {
			System.out.println("Error in PriceGenerator: generateFinalPrice");
			System.out.println(e.getMessage());
		}
		return finalPrice;
	}
		
	//private method that get and the prices from DB and set them
	private void setPrices() {
		try {
			//1. get the prices from DB
			ArrayList<Object> arrmsg = new ArrayList<Object>();
			arrmsg.add(new String("GetPrices"));
			arrmsg.add(new String("GetPrices"));
			arrmsg.add(new String("GetPrices"));
			ClientUI.chat.accept(arrmsg);	
			
			//2. set the prices
			if(ChatClient.result == true) {
				//"ArrayList<Integer>", {"0-full_price", "1-discount_private_family_planned", "2-discount_private_family_unplanned", "3-discount_group_planned", "4-discount_group_unplanned", "5-discount_payment_in_advance" }
				fullPrice = Integer.parseInt(ChatClient.dataFromServer.get(0));
				discountPrivateFamilyPlanned = Integer.parseInt(ChatClient.dataFromServer.get(1));
				discountPrivateFamilyUnplanned = Integer.parseInt(ChatClient.dataFromServer.get(2));
				discountGroupPlanned = Integer.parseInt(ChatClient.dataFromServer.get(3));
				discountPrivateFamilyUnplanned = Integer.parseInt(ChatClient.dataFromServer.get(4));
				discountPaymentInAdvance = Integer.parseInt(ChatClient.dataFromServer.get(5));
			}else {
				System.out.println("No prices returned from DB");
			}
		} catch (Exception e) {
			System.out.println("Error in PriceGenerator: setPrices");
			System.out.println(e.getMessage());
		}
	}//END OF setPrices
	
	

}
