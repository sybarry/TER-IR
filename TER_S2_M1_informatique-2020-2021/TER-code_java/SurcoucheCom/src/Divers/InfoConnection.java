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
}
