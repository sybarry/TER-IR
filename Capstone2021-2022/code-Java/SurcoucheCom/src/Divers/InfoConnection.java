package Divers;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class InfoConnection {
	
	private String sender; // The ID of the person sending the messages
	private String receiver; // The ID of the person receiving the messages
	
	/*
	 * Create an instance of the connection informations
	 * 
	 * @param sender The ID of the person sending the messages
	 * @param receiver The ID of the person receiving the messages
	 */
	public InfoConnection(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getReceiver() {
		return this.receiver;
	}
	
	public void setSender(String newSender) {
		this.sender = newSender;
	}

	public void setReceiver(String newReceiver) {
		this.receiver = newReceiver;
	}
	
	/*
	 * Method that transforms the contents of all class attributes into string and concatenates them
	 * 
	 * @return String of concatenate attributes
	 */
	public String toString() {
		return this.sender+"--"+this.receiver;
	}
	
	/*
	 * Creates an InfoConnection instance from a string
	 *
	 * @param infoConnection A string corresponding to the content of all the information linked to the connection
	 * @return An instance of InfoConnection corresponding to the string set to
	 */
	public static InfoConnection toInfoConnection(String infoConnection) {
		String[] infoConnectionSplit = infoConnection.split("--"); // divides the string to be able to retrieve each information from the connection
		return new InfoConnection(infoConnectionSplit[0], infoConnectionSplit[1]);
	}
}
