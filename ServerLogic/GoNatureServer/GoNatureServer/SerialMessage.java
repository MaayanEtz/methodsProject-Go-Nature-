package GoNatureServer;

import java.io.Serializable;

public class SerialMessage implements Serializable{
	
	public static enum ActionType{
		SAVE, UPDATE, DELETE, INSERT, INFORM, GET
	}
	
	public static enum TypeOfObject{
		Park, Order, StringMessage, ArrayList
	}
	
	public static enum Endpoint{
		ConnectToServer, loginUser, getPark, getOrder, editOrder, cancelOrder, createNewOrder
	}
	
	private ActionType actionType;
	private TypeOfObject objectType;
	private Endpoint endPoint;
	private Object payload;
	private Exception e;
	
	
	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	

	// CONSTRUCTORS
	public SerialMessage() {this.e = null;} ;
	
	public SerialMessage(ActionType actionType, TypeOfObject  objectType) {
		this();
		this.actionType = actionType;
		this.objectType = objectType;
	}
	
	

	public SerialMessage(ActionType actionType, TypeOfObject  objectType, Endpoint endPoint) {
		this(actionType, objectType);
		this.endPoint = endPoint;
	}
	
	public SerialMessage(ActionType actionType, TypeOfObject  objectType, Endpoint endPoint, Object payload) {
		this(actionType,objectType,endPoint);
		this.payload = payload;
	}
	
	
	// GETTERS SETTERS
	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public TypeOfObject getEntity() {
		return objectType;
	}

	public void setEntity(TypeOfObject entity) {
		this.objectType = entity;
	}

	public Endpoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Endpoint endPoint) {
		this.endPoint = endPoint;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public Object getPayload() {
		return payload;
	}
}