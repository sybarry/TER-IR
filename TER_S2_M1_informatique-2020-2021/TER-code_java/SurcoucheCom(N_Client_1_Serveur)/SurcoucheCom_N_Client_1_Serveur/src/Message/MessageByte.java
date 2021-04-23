package Message;

public class MessageByte extends AMessage<Byte> {

	private byte message;
	
	public MessageByte(byte message) {
		super("byte");
		this.message = message;
	}
	
	public MessageByte(int idMessage, String sender, String receiver, String typeMessage, boolean withACK, byte message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, sender, receiver, typeMessage, withACK);
		this.message = message;
	}
	
	public MessageByte(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
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
