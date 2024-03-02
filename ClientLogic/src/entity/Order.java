package entity;

import java.util.ArrayList;
import java.util.Arrays;

public class Order {
	private String parkName;
	private String orderNumber;
	private String timeOfVisit;
	private String numberOfVisitors;
	private String telephoneNumber;
	private String email;
	/**
	 * @param parkName
	 * @param orderNumber
	 * @param timeOfVisit
	 * @param numberOfVisitors
	 * @param telephoneNumber
	 * @param Email
	 */
	//constructor
	public Order(String parkName, String orderNumber, String timeOfVisit, String numberOfVisitors, String telephoneNumber, String email) {
		this.parkName = parkName;
		this.orderNumber = orderNumber;
		this.timeOfVisit = timeOfVisit;
		this.numberOfVisitors = numberOfVisitors;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
	}
	
	//constructor
	public Order(ArrayList<String> arr) {
		this.parkName = arr.get(0);
		this.orderNumber = arr.get(1);
		this.timeOfVisit = arr.get(2);
		this.numberOfVisitors = arr.get(3);
		this.telephoneNumber = arr.get(4);
		this.email = arr.get(5);
		System.out.println(this);
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

	
	public String toString(){
		return String.format("%s %s %s %s %s %s\n",parkName, orderNumber, timeOfVisit, numberOfVisitors, telephoneNumber, email);
	}
	
	public ArrayList<String> toArrayList() {
		return new ArrayList<String>(Arrays.asList(parkName, orderNumber, timeOfVisit, numberOfVisitors, telephoneNumber, email));
	}
	
	//set all fields to order from given array
	public void setAllFields(ArrayList<String> arr) {
		System.out.println("1");
		this.parkName = arr.get(0);
		this.orderNumber = arr.get(1);
		this.timeOfVisit = arr.get(2);
		this.numberOfVisitors = arr.get(3);
		this.telephoneNumber = arr.get(4);
		this.email = arr.get(5);
	}
}