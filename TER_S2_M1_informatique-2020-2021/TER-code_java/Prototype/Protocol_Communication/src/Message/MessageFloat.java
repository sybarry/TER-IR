package Message;

public class MessageFloat extends AMessage<Float> {

	private float message;
	
	public MessageFloat(float message) {
		super("float");
		this.message = message;
	}
	public MessageFloat(int idMessage, String typeMessage, float message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, typeMessage);
		this.message = message;
	}
	
	public MessageFloat(MessageString message) {
		super(message.getIdMessage(), message.getTypeMessage());
		this.message = Float.parseFloat(message.getMessage());
	}

	@Override
	public Float getMessage() {
		return message;
	}

	@Override
	public void setMessage(Float newMessage) {
		this.message = newMessage;
	}
}
