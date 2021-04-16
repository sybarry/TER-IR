package Message;

import Divers.InfoConnection;

public abstract class AMessage<T> implements IMessage<T> {

	private int idMessage;
	private String typeMessage;
	
	private InfoConnection infoConnection;
	
	public AMessage(String typeMessage) {
		this.idMessage = -1;
		this.infoConnection = null;
		this.typeMessage = typeMessage;
	}
	
	public AMessage(int idMessage, String sender, String receiver, String typeMessage) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		this.idMessage = idMessage;
		this.infoConnection.setReceiver(receiver);
		this.infoConnection.setSender(sender);
		this.typeMessage = typeMessage;
	}
	
	public int getIdMessage() {
		return this.idMessage;
	}
	
	public InfoConnection getInfoConnection() {
		return this.infoConnection;
	}
	
	public String getTypeMessage() {
		return this.typeMessage;
	}
	
	public abstract T getMessage();	
	
	@Override
	public void setIdMessage(int newIdMessage) {
		this.idMessage = newIdMessage;
	}
	
	@Override
	public void setInfoConnection(InfoConnection newInfoConnection) {
		this.infoConnection = newInfoConnection;
	}

	@Override
	public void setTypeMessage(String newTypeMessage) {
		this.typeMessage = newTypeMessage;
	}
	
	public abstract void setMessage(T newMessage);
}
