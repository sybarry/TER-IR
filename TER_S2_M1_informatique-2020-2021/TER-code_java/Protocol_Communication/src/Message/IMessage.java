package Message;


public interface IMessage<T> {

	int getIdMessage();
	String getTypeMessage();
	T getMessage();
	void setIdMessage(int newIdMessage);
	void setTypeMessage(String newTypeMessage);
	void setMessage(T newMessage);
	void setAddressSender(String newAddressSender);
	void setAddressReceiver(String newAddressReceiver);
	void setWithACK(boolean ack);
	boolean getWithACK();
}
