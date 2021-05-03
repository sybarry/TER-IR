package Message;

public class MessageBoolean extends AMessage<Boolean> {

	private boolean message;
	
	public MessageBoolean(boolean message) {
		super("boolean");
		this.message = message;
	}
	
	public MessageBoolean(int idMessage, String sender, String receiver, String typeMessage, boolean withACK, boolean message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, sender, receiver, typeMessage, withACK);
		this.message = message;
	}
	
	public MessageBoolean(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Boolean.parseBoolean(message.getMessage());
	}

	@Override
	public Boolean getMessage() {
		return message;
	}

	@Override
	public void setMessage(Boolean newMessage) {
		this.message = newMessage;
	}
}
