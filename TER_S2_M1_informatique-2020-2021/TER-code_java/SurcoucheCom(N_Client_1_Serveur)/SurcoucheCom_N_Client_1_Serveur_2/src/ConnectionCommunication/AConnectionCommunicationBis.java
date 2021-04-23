package ConnectionCommunication;

import java.io.IOException;

import Exception.MessageException;
import Message.IMessage;

public abstract class AConnectionCommunicationBis extends AConnectionCommunication{

	@Override 
	public abstract void openConnection() throws IOException;
	
	@Override
	public abstract void closeConnection() throws IOException;
	
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		AsendMessage(msg);
	}

	public IMessage<?> receiveMessage() throws IOException, MessageException{
		return AreceiveMessage();
	}
	

	public void sendMessageSynchronized(IMessage<?> msg) throws IOException, MessageException{	
		AsendMessageSynchronized(msg);
	}
	
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, MessageException {
		AsendMessageAsynchronized(msg);
	}
}
