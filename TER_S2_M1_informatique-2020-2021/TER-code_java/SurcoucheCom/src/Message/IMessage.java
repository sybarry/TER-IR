package Message;

import Divers.InfoConnection;

public interface IMessage<T> {

	int getIdMessage();
	InfoConnection getInfoConnection();
	String getTypeMessage();
	T getMessage();
	void setIdMessage(int newIdMessage);
	void setInfoConnection(InfoConnection newInfoConnection);
	void setTypeMessage(String newTypeMessage);
	void setMessage(T newMessage);
}
