package Message;

public class MessageString extends AMessage<String> {

	private String message;
	
	public MessageString(String message) {
		super("String");
		this.message = message;
	}
	
	public MessageString(int idMessage, String sender, String receiver, String typeMessage, String message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, sender, receiver, typeMessage);
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
