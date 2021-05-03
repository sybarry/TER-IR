package Message;

public class MessageBoolean extends AMessage<Boolean> {

	private boolean message;
	
	public MessageBoolean(boolean message) {
		super("boolean");
		this.message = message;
	}
	public MessageBoolean(int idMessage, String typeMessage, boolean message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, typeMessage);
		this.message = message;
	}
	
	public MessageBoolean(MessageString message) {
		super(message.getIdMessage(), message.getTypeMessage());
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
