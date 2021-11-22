package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageInt extends AMessage<Integer> {

	private int message; // the body of message
	
	/*
	 * Create an instance for MessageInt
	 * 
	 * @param message The body of message 
	 */
	public MessageInt(int message) {
		super("int");
		this.message = message;
	}
	
	/*
	 * Create an instance for MessageInt
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */
	public MessageInt(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = Integer.parseInt(message.getMessage());
	}

	@Override
	public Integer getMessage() {
		return message;
	}

	@Override
	public void setMessage(Integer newMessage) {
		this.message = newMessage;
	}
}
