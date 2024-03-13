package entity;

import java.util.ArrayList;

public class Park {
	private int parkId;
	private String name;
	private int capacity; //the maximum quota
	private int diff; // holds diff from capacity to actual park visitor limit - gap
	private int visitTimeInMinutes;
	private int currentVisitors;

	// constructors
	public Park(int parkId, String name, int capacity, int diff, int visitTimeInMinutes, int currentVisitors) {
		this.parkId = parkId;
		this.name = name;
		this.capacity = capacity;
		this.diff = diff;
		this.visitTimeInMinutes = visitTimeInMinutes;
		this.currentVisitors = currentVisitors;
	}

	public Park(ArrayList<String> arr) {
		this(Integer.valueOf(arr.get(0)), arr.get(1), Integer.valueOf(arr.get(2)), Integer.valueOf(arr.get(3)),
				Integer.valueOf(arr.get(4)), Integer.valueOf(arr.get(5)));
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public int getVisitTimeInMinutes() {
		return visitTimeInMinutes;
	}

	public void setVisitTimeInMinutes(int visitTimeInMinutes) {
		this.visitTimeInMinutes = visitTimeInMinutes;
	}

	public int getCurrentVisitors() {
		return currentVisitors;
	}

	public void setCurrentVisitors(int currentVisitors) {
		this.currentVisitors = currentVisitors;
	}
	
	public ArrayList<String> toArr() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("" + parkId);
		arr.add(name);
		arr.add("" + capacity);
		arr.add("" + diff);
		arr.add("" + visitTimeInMinutes);
		arr.add("" + currentVisitors);
		return arr;
	}
	
	

}