package Divers;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class InfoMessage {

	private int idMessage; // the identifier of message 
	private String messageType; // the type of message
	private boolean withACK; // if the message must receive an acknowledge of receipt
	private String topic;
	
	/*
	 * Create an instance of the messages informations
	 * 
	 * @param idMessage The identifier of message 
	 * @param messageType The type of message
	 * @param withACK If the message must receive an acknowledge of receipt 
	 */
	public InfoMessage(int idMessage, String messageType, boolean withACK, String topic) {
		this.idMessage = idMessage;
		this.messageType = messageType;
		this.withACK = withACK;
		this.topic = topic;
	}
	
	/*
	 * Create an instance of the messages informations
	 * 
	 * @param idMessage The identifier of message 
	 * @param withACK If the message must receive an acknowledge of receipt 
	 */
	public InfoMessage(int idMessage, boolean withACK) {
		this.idMessage = idMessage;
		this.withACK = withACK;
		this.messageType = "";
		this.topic = " ";
	}
	
	public int getIdMessage() {
		return this.idMessage;
	}
	
	public String getMessageType() {
		return this.messageType;
	}
	
	public boolean getWithACK() {
		return this.withACK;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public void setIdMessage(int newIdMessage) {
		this.idMessage = newIdMessage;
	}
	
	public void setMessageType(String newMessageType) {
		this.messageType = newMessageType;
	}

	public void setWithACK(boolean newWithACK) {
		this.withACK = newWithACK;
	}
	
	public void setTopic(String newtopic) {
		this.topic = newtopic;
	}
	
	/*
	 * Method that transforms the contents of all class attributes into string and concatenates them
	 * 
	 * @return String of concatenate attributes
	 */
	public String toString() {
		return this.idMessage+"%%"+this.messageType+"%%"+this.withACK+"%%"+this.topic;
	}
	
	/*
	 * Creates an InfoMessage instance from a string
	 *
	 * @param infoMessage A string corresponding to the content of all the information linked to the messages
	 * @return An instance of InfoMessage corresponding to the string set to
	 */
	public static InfoMessage toInfoMessage(String infoMessage) {
		String[] infoMessageSplit = infoMessage.split("%%");
		return new InfoMessage(Integer.parseInt(infoMessageSplit[0]), infoMessageSplit[1], Boolean.parseBoolean(infoMessageSplit[2]), infoMessageSplit[3]);
	}
}
