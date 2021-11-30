package Message;

import Divers.InfoConnection;
import Divers.InfoMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageString extends AMessage<String> {

	private String message; // the body of message 
	
	/*
	 * Create an instance for MessageString 
	 * 
	 * @param message The body of message 
	 */
	public MessageString(String message) {
		super("String");
		this.message = message;
	}
	
	public MessageString(String message, String topic) {
		super("String");
		this.message = message;
		this.getInfoMessage().setTopic(topic);
	}
	
	/*
	 * Create an instance for MessageString
	 * 
	 * @param message The messaString that will be converted into the type of this class
	 */
	public MessageString(MessageString message) { // Constructor used when using the factory in receiveMessage()
		super(message.getInfoMessage(), message.getInfoConnection());
		this.message = message.getMessage();
	}
	
	/*
	 * Create an instance for MessageString 
	 * 
	 * @param infoMessage The generic message characteristics
	 * @param infoConnection The connection information related to this generic message
	 * @param message The body of message
	 */
	public MessageString(String infoMessage, String infoConnection, String message) { // Constructor used when using toMessageString() in decoderMessage()
		super(InfoMessage.toInfoMessage(infoMessage), InfoConnection.toInfoConnection(infoConnection));
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String newMessage) {
		this.message = newMessage;
	}
}
