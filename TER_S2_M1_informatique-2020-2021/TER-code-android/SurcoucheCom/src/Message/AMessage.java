package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

import Divers.InfoConnection;
import Divers.InfoMessage;

public abstract class AMessage<T> implements IMessage<T> {

	private InfoMessage infoMessage; // the generic message characteristics
	private InfoConnection infoConnection; // the connection information related to this generic message
	
	/*
	 * Create an instance for abstract Message 
	 * 
	 * @param messageType The type of the generic message to be created
	 */
	public AMessage(String messageType) { // Constructor used by user
		this.infoMessage = new InfoMessage(-1, messageType, false);
		this.infoConnection = null;
	}
	
	/*
	 * Create an instance for abstract Message 
	 * 
	 * @param infoMessage The generic message characteristics
	 * @param infoConnection The connection information related to this generic message
	 */
	public AMessage(InfoMessage infoMessage, InfoConnection infoConnection) { // Constructor used when using decoderMessage()
		this.infoMessage = infoMessage;
		this.infoConnection = infoConnection;
	}
	
	@Override
	public InfoConnection getInfoConnection() {
		return this.infoConnection;
	}
	
	@Override
	public InfoMessage getInfoMessage() {
		return this.infoMessage;
	}
	
	@Override
	public abstract T getMessage();	
	
	@Override
	public void setInfoConnection(InfoConnection newInfoConnection) {
		this.infoConnection = newInfoConnection;
	}
	
	@Override
	public void setInfoMessage(InfoMessage newInfoMessage) {
		this.infoMessage = newInfoMessage;
	}
	
	@Override
	public abstract void setMessage(T newMessage);
	
	@Override
	public String toString() {
		return this.infoMessage+"@@"+this.infoConnection.toString()+"&&"+this.getMessage();
	}

	/*
	 * Creates an MessageString instance from a string
	 * 
	 * @param message A string corresponding to the content of all the information linked to the generic message
	 * @return An instance of MessageString corresponding to the string set to, because this is the only type (with Byte) 
	 * that can be converted into all other types
	 */
	public static MessageString toMessageString(String message){  
		String[] messageSplit1 =  message.split("@@");
		String[] messageSplit2 =  messageSplit1[1].split("&&");
		
		return new MessageString(messageSplit1[0], messageSplit2[0], messageSplit2[1]);
	}
}