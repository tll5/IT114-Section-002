import java.io.Serializable;
public class Payload implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6625037986217386003L;
	private String message;
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
	@Override
	public String toString() {
		return String.format("Type[%s], Message[%s], Turns[%s]",
					getPayloadType().toString(), getMessage(), getTurns());
	}
}