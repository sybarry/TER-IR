package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageByte extends AMessage<Byte> {

	private byte message; // the body of message
	
	/*
	 * Create an instance for MessageByte
	 * 
	 * @param message the body of message 
	 */
	public MessageByte(byte message) {
		super("byte");
		this.message = message;
	}

	/*
	 * Create an instance for MessageByte 
	 * 
	 * @param message The body of message 
	 * @param topic The message sending channel (for mqtt)
	 */
	public MessageByte(byte message, String topic) {
		super("byte");
		this.message = message;
		this.getInfoMessage().setTopic(topic);
	}
	
	/*
	 * Create an instance for MessageByte
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */ 
	public MessageByte(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = Byte.parseByte(message.getMessage());
	}

	@Override
	public Byte getMessage() {
		return message;
	}

	@Override
	public void setMessage(Byte newMessage) {
		this.message = newMessage;
	}
}
