package Message;

public class MessageByte extends AMessage<Byte> {

	private byte message;
	
	public MessageByte(byte message) {
		super("byte");
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
