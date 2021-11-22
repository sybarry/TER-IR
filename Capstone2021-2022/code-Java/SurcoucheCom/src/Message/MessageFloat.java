package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageFloat extends AMessage<Float> {

	private float message; // the body of message
	
	/*
	 * Create an instance for MessageFloat
	 * 
	 * @param message the body of message 
	 */
	public MessageFloat(float message) {
		super("float");
		this.message = message;
	}
	
	/*
	 * Create an instance for MessageFloat
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */ 
	public MessageFloat(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = Float.parseFloat(message.getMessage());
	}

	@Override
	public Float getMessage() {
		return message;
	}

	@Override
	public void setMessage(Float newMessage) {
		this.message = newMessage;
	}
}
