package Message;


public abstract class AMessage<T> implements IMessage<T> {

	private int idMessage;
	private String typeMessage;
	protected String addressReceiver;
	protected String addressSender;
	private boolean withACK;	
	
	
	public AMessage(String typeMessage) {
		this.idMessage = -1;
		this.typeMessage = typeMessage;
		addressSender = "";
		addressReceiver = "";
	}
	
	public AMessage(int idMessage, String typeMessage) { // Constructeur utilisé pour creer l'objet Message lors d'un receiveMessage()
		this.idMessage = idMessage;
		this.typeMessage = typeMessage;
	}
	
	public int getIdMessage() {
		return this.idMessage;
	}
	
	
	public String getTypeMessage() {
		return this.typeMessage;
	}
	
	public String getAddressReceiver() {
		return this.addressReceiver;
	}
	
	public String getAddressSender() {
		return this.addressSender;
	}
	
	
	public abstract T getMessage();	
	
	@Override
	public boolean getWithACK() {
		return this.withACK;
	}
	
	
	
	@Override
	public void setWithACK(boolean ack) {
		withACK = ack;
	}  
	
	@Override
	public void setIdMessage(int newIdMessage) {
		this.idMessage = newIdMessage;
	}

	@Override
	public void setAddressSender(String newAddressSender) {
		this.addressSender = newAddressSender;
	}
	
	@Override
	public void setAddressReceiver(String newAddressReceiver) {
		this.addressReceiver = newAddressReceiver;
	}
	
	@Override
	public void setTypeMessage(String newTypeMessage) {
		this.typeMessage = newTypeMessage;
	}
	
	public abstract void setMessage(T newMessage);
}
