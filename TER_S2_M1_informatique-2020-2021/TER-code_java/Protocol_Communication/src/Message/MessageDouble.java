package Message;

public class MessageDouble extends AMessage<Double> {

	private double message;
	
	public MessageDouble(double message) {
		super("double");
		this.message = message;
	}
	
	public MessageDouble(int idMessage, String typeMessage, double message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, typeMessage);
		this.message = message;
	}
	public MessageDouble(MessageString message) {
		super(message.getIdMessage(),message.getTypeMessage());
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
