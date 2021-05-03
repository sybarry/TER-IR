package Message;

public class MessageByte extends AMessage<Byte> {

	private byte message;
	
	public MessageByte(byte message) {
		super("byte");
		this.message = message;
	}
	public MessageByte(int idMessage, String typeMessage, Byte message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, typeMessage);
		this.message = message;
	}
	
	public MessageByte(MessageString message) {
		super(message.getIdMessage(), message.getTypeMessage());
		this.message = Byte.parseByte(message.getMessage());
	}

	@Override
	public Byte getMessage() {
		return message;
	}

	@Override
	public void setMessage(Byte newMessage) {
		this.message = newMessage;
	}
}
