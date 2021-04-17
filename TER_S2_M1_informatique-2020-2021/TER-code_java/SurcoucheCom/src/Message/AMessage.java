package Message;

import Divers.InfoConnection;

public abstract class AMessage<T> implements IMessage<T> {

	private int idMessage;
	private String typeMessage;
	private boolean withACK;	
	private InfoConnection infoConnection;
	
	public AMessage(String typeMessage) {
		this.idMessage = -1;
		this.infoConnection = null;
		this.typeMessage = typeMessage;
		this.withACK = false;
	}
	
	public AMessage(int idMessage, String sender, String receiver, String typeMessage, boolean withACK) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		this.idMessage = idMessage;
		this.infoConnection = new InfoConnection(sender, receiver);
		this.typeMessage = typeMessage;
		this.withACK = withACK;
	}
	
	public AMessage(int idMessage, InfoConnection infoConnection, String typeMessage, boolean withACK) { //Constructeur utilisé lorsqu'on utilise decoderMessage
		this.idMessage = idMessage;
		this.infoConnection = infoConnection;
		this.typeMessage = typeMessage;
		this.withACK = withACK;
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
	
	@Override
	public boolean getWithACK() {
		return this.withACK;
	}
	
	@Override
	public void setWithACK(boolean newWithACK) {
		this.withACK = newWithACK;
	}
}
