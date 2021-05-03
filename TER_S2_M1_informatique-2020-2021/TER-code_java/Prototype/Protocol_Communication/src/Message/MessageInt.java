package Message;

public class MessageInt extends AMessage<Integer> {

	private int message;
	
	public MessageInt(int message) {
		super("int");
		this.message = message;
	}
	public MessageInt(int idMessage, String typeMessage, int message) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		super(idMessage, typeMessage);
		this.message = message;
	}
	
	public MessageInt(MessageString message) {
		super(message.getIdMessage(), message.getTypeMessage());
		this.message = Integer.parseInt(message.getMessage());
	}

	@Override
	public Integer getMessage() {
		return message;
	}

	@Override
	public void setMessage(Integer newMessage) {
		this.message = newMessage;
	}
}
