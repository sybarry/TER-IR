package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageBoolean extends AMessage<Boolean> {

	private boolean message; // the body of message
	
	/*
	 * Create an instance for MessageBoolean
	 * 
	 * @param message the body of message 
	 */
	public MessageBoolean(boolean message) {
		super("boolean");
		this.message = message;
	}
	
	/*
	 * Create an instance for MessageBoolean
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */
	public MessageBoolean(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = Boolean.parseBoolean(message.getMessage());
	}

	@Override
	public Boolean getMessage() {
		return message;
	}

	@Override
	public void setMessage(Boolean newMessage) {
		this.message = newMessage;
	}
}
