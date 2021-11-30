package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageDouble extends AMessage<Double> {

	private double message; // the body of message
	
	/*
	 * Create an instance for MessageDouble
	 * 
	 * @param message the body of message 
	 */
	public MessageDouble(double message) {
		super("double");
		this.message = message;
	}
	
	public MessageDouble(double message, String topic) {
		super("double");
		this.message = message;
		this.getInfoMessage().setTopic(topic);
	}
	
	/*
	 * Create an instance for MessageDouble
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */
	public MessageDouble(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = Double.parseDouble(message.getMessage());
	}

	@Override
	public Double getMessage() {
		return message;
	}

	@Override
	public void setMessage(Double newMessage) {
		this.message = newMessage;
	}
}
