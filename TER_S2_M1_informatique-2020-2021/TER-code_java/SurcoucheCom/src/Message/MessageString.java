package Message;

public class MessageString extends AMessage<String> {

	private String message;
	
	public MessageString(String message) {
		super("String");
		this.message = message;
	}
	
	public MessageString(int idMessage, String sender, String receiver, String typeMessage, boolean withACK, String message) { // Constructeur utilisé lorsqu'on utilise aucun decodeur
		super(idMessage, sender, receiver, typeMessage, withACK);
		this.message = message;
	}
	
	public MessageString(MessageString message) { //Constructeur utilisé lorsqu'on utilise decoderMessage
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = message.getMessage();
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
