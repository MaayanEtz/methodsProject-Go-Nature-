package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Order implements Serializable {
	private String parkName;
	private String orderNumber;
	private String timeOfVisit;
	private String numberOfVisitors;
	private String telephoneNumber;
	private String email;
	private String visitorID;
	private String status;

	/**
	 * @param parkName
	 * @param orderNumber
	 * @param timeOfVisit
	 * @param numberOfVisitors
	 * @param telephoneNumber
	 * @param Email
	 */
	//constructor
	public Order(String status, String parkName, String orderNumber, String timeOfVisit, String numberOfVisitors, String telephoneNumber, String email, String visitorID) {
		this.parkName = parkName;
		this.orderNumber = orderNumber;
		this.timeOfVisit = timeOfVisit;
		this.numberOfVisitors = numberOfVisitors;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.visitorID = visitorID;
		this.status = status;
	}
	
	//constructor
	public Order(ArrayList<String> arr) {
		this.orderNumber = arr.get(0);
		this.visitorID = arr.get(1);
		this.parkName = arr.get(2);
		this.timeOfVisit = arr.get(3);
		this.numberOfVisitors = arr.get(4);
		this.email = arr.get(5);
		this.telephoneNumber = arr.get(6);
		this.status = arr.get(7);
	}
	//get status
	public String getStatus() {
		return status;
	}
	//set status
	public void setStatus(String status) {
		this.status = status;
	}

	//get park name
	public String getParkName() {
		return parkName;
	}
	
	//set park name
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
	//get order number
	public String getOrderNumber() {
		return orderNumber;
	}
	
	//set order number
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	//get time of visit
	public String getTimeOfVisit() {
		return timeOfVisit;
	}

	//set time of visit
	public void setTimeOfVisit(String timeOfVisit) {
		this.timeOfVisit = timeOfVisit;
	}

	//get number of visitors
	public String getNumberOfVisitors() {
		return numberOfVisitors;
	}

	//set number of visitors
	public void setNumberOfVisitors(String numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	//get phone number
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	//set phone number
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	//get email
	public String getEmail() {
		return email;
	}

	//set email
	public void setEmail(String email) {
		this.email = email;
	}
	
	//get visitor ID
	public String getVisitorID() {
		return visitorID;
	}
	
	//set visitor ID
	public void setVisitorID(String visitorID) {
		this.visitorID = visitorID;
	}


	
	public String toString(){
		return String.format("%s %s %s %s %s %s %s\n",parkName, orderNumber, timeOfVisit, numberOfVisitors, telephoneNumber, email, visitorID);
	}
	
	public ArrayList<String> toArrayList() {
		return new ArrayList<String>(Arrays.asList(parkName, orderNumber, timeOfVisit, numberOfVisitors, telephoneNumber, email, visitorID));
	}
	
	//set all fields to order from given array
	public void setAllFields(ArrayList<String> arr) {
		this.orderNumber = arr.get(0);
		this.parkName = arr.get(1);
		this.timeOfVisit = arr.get(2);
		this.numberOfVisitors = arr.get(3);
		this.email = arr.get(4);
		this.telephoneNumber = arr.get(5);
		this.visitorID = arr.get(6);
	}
}