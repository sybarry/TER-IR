package ConnectionCommunication;

import java.io.IOException;

import Exception.MessageException;
import Message.IMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public interface IConnectionCommunication {
	
	/*
	 *  Method for establishing a connection between two devices
	 *  
	 *  @throws IOException If an I/O error occurs 
	 */
	void openConnection() throws IOException;
	
	/*
	 *  Method to close the connection between two devices
	 *  
	 *  @throws IOException If an I/O error occurs 
	 */
	void closeConnection() throws IOException;
	
	/*
	 *  Method to send an acknowledgement of receipt a message to the source device
	 *  
	 *  @param msg The message for which you want to send an acknowledgement of receipt
	 *  @throws IOException If an I/O error occurs 
	 */
	void sendACK(IMessage<?> msg) throws IOException;
	
	/*
	 *  Method used to determine if an acknowledgement of receipt has been retrieved for the identifier message idMessage 
	 *  
	 *  @param msg The message for which you want to receive an acknowledgement of receipt
	 *  @return True if a receive an acknowledge of receipt for the message
	 *  @throws IOException If an I/O error occurs 
	 */
	boolean receiveACK(IMessage<?> msg) throws IOException;
	
	/*
	 *  Method for sending a message between two devices without waiting for an acknowledgement of receipt 
	 *  
	 *  @param msg The message to be sent
	 *  @throws IOException If an I/O error occurs 
	 *  @throws MessageException If the stream has been incorrectly initialized
	 */
	void sendMessage(IMessage<?> msg) throws IOException, MessageException;
	
	/*
	 *  Method for receiving a message and sending an acknowledgement if required
	 *  
	 *  @return The message receive
	 *  @throws IOException If an I/O error occurs 
	 *  @throws MessageException If the stream has been incorrectly initialized
	 */
	IMessage<?> receiveMessage() throws IOException, MessageException;
	
	/*
	 *  Method to send a message between two devices synchronously (wait for an acknowledgement before proceeding)
	 *  
	 *  @param msg The message to be sent
	 *  @throws IOException If an I/O error occurs 
	 *  @throws MessageException If the stream has been incorrectly initialized
	 */
	void sendMessageSynchronized(IMessage<?> msg) throws IOException, MessageException;
	
	/*
	 *  Method that allows to send a message between two devices in an asynchronous manner 
	 *  (we do not wait for the acknowledgement to continue, the sending of the acknowledgement is done in parallel)
	 *  
	 *  @param msg The message to be sent
	 *  @throws IOException If an I/O error occurs 
	 *  @throws MessageException If the stream has been incorrectly initialized
	 */
	void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, MessageException;
}
