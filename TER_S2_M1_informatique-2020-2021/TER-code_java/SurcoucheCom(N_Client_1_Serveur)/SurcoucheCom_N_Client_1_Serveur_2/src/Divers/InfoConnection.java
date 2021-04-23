package Divers;

public class InfoConnection {
	
	private String sender;
	private String receiver;
	
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
	
	public String toString() {
		return this.sender+"--"+this.receiver;
	}
	
	public static InfoConnection toInfoConnection(String infoConnection) {
		String[] infoConnectionSplit = infoConnection.split("--");
		return new InfoConnection(infoConnectionSplit[0], infoConnectionSplit[1]);
	}
}
