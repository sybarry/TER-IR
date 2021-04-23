package Message;

public class MessageDouble extends AMessage<Double> {

	private double message;
	
	public MessageDouble(double message) {
		super("double");
		this.message = message;
	}
	
	public MessageDouble(int idMessage, String sender, String receiver, String typeMessage, boolean withACK, double message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, sender, receiver, typeMessage, withACK);
		this.message = message;
	}
	
	public MessageDouble(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Double.parseDouble(message.getMessage());
	}

	@Override
	public Double getMessage() {
		return message;
	}

	@Override
	public void setMessage(Double newMessage) {
		this.message = newMessage;
	}
}
