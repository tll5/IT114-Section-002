import java.io.Serializable;
public class Payload implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6625037986217386003L;
	private boolean isOn = false;
	public String message;
	
	public void IsOn(boolean isOn) {
		this.isOn = isOn;
	}
	public boolean IsOn() {
		return this.isOn;
	}
	
	public void setMessage(String s) {
		this.message = s;
	}
	public String getMessage() {
		return this.message;
	}
	/*
	private String clientName;
	public void setClientName(String clientN)
	{
		this.clientName = clientN;
	}
	public String getClientName()
	{
		return this.getClientName();
	}
	*/
	
	private PayloadType payloadType;
	public void setPayloadType(PayloadType pt) {
		this.payloadType = pt;
	}
	public PayloadType getPayloadType() {
		return this.payloadType;
	}
	/*
	private int number;
	public void setNumber(int n) {
		this.number = n;
	}
	public int getNumber() {
		return this.number;
	}
	*/
	private int turns;
	public void setTurns(int t) {
		this.turns = t;
	}
	public int getTurns() {
		return this.turns;
	}
	//WORKED ON AS OF 4/5/20
	public int clientId;
	public void setClientId(int ci) {
		this.clientId = ci; //assume it's a reference to who send this to server
	}
	public int getClientId() {
		return this.clientId;
	}
	
	public int type;
	public void setType(int typ) {
		this.type = typ;
	}
	public int getType() {
		return this.type;
	}
	
	public int x;
	public void setX(int valueX) {
		this.x = valueX;
	}
	public int getX() {
		return this.x;
	}
	
	public int y;
	public void setY(int valueY) {
		this.x = valueY;
	}
	public int getY() {
		return this.y;
	}
	public int player;
	//public String message;
	@Override
	public String toString() {
		return String.format("Type[%s], Message[%s], Turns[%s]",
					getPayloadType().toString(), IsOn()+"", getX(), getY(), getMessage(), getTurns());
	}
}