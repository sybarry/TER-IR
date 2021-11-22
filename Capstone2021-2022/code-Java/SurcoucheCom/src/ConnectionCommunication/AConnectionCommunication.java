package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Divers.InfoConnection;
import Exception.MessageException;
import Message.Encodeur_Decodeur;
import Message.IMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public abstract class AConnectionCommunication implements IConnectionCommunication{
	
	protected static int timeOut = 30000; // Wait time before returning an acknowledge of receipt
	protected DataOutputStream dOut; // The output stream of the connection
	protected DataInputStream dIn; // The input stream of the connection
	
	protected InfoConnection infoConnection; // Information related to the connection
	protected int idMessage = 0; // The identifier of the message that will be sent
	
	@Override 
	public abstract void openConnection() throws IOException;
	
	@Override
	public abstract void closeConnection() throws IOException;
	
	/*
	 *  Method to send the encoder message to the stream
	 *  
	 *  @param msg The message to be sent
	 */
	private void writeMessage(IMessage<?> msg) throws IOException {
		byte[] messageConverted = Encodeur_Decodeur.encoderMessage(msg); // Encodes the message
		dOut.write(messageConverted); 
		dOut.flush(); // Empties the output stream and forces the writing of all the output bytes in buffer memory
	}
	
	@Override
	public void sendACK(int idMessage) throws IOException {
		dOut.writeUTF("received message "+idMessage); 
		dOut.flush();
	}
	
	@Override
	public boolean receiveACK(int idMessage) throws IOException { 
		if(dIn.available() == 0) return false; 	// to find out if there are bytes that have been sent in the input stream 
												// because readUTF() waits for something to be sent to it before continuing the code
		
		return dIn.readUTF().contentEquals("received message "+idMessage); 
	}
	
	/*
	 *  Method to return a message if an acknowledgement has not been received
	 *  
	 *  @param msg The message to be sent
	 *  @param timeOut Wait time before returning an acknowledge of receipt
	 */
	private void ACK(IMessage<?> msg, int timeOut) throws IOException {
		boolean ack = receiveACK(msg.getInfoMessage().getIdMessage());
		long chrono = java.lang.System.currentTimeMillis();
		
		while(ack == false) {
			ack = receiveACK(msg.getInfoMessage().getIdMessage());
			if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= timeOut)) { 	// if he has not received an acknowledge of receipt, and 
																								// that the timeOut has been reached
				writeMessage(msg);
				chrono = java.lang.System.currentTimeMillis();
			}
		}
	}
	
	/*
	 *  Method used to initialize information related to the message to be sent
	 *  
	 *  @param msg The message to be initialized
	 *  @param withACK If the message must have an acknowledgement of receipt
	 */
	private void initialisationInfoMessage(IMessage<?> msg, boolean withACK) {
		
		idMessage = idMessage + 1;    	// increment the message identifier, so that the message that will be sent is a different identifier 
										//	of the other messages
		msg.getInfoMessage().setIdMessage(idMessage);	
		msg.getInfoMessage().setWithACK(withACK);	
		msg.setInfoConnection(this.infoConnection);
	}
	
	@Override
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, false);
		
		if(dOut != null) {
			writeMessage(msg);
		}else {
			throw new MessageException("Output stream was incorrectly initialized");
		}
	}
	
	@Override
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		if(dIn != null) {
			int streamSize = dIn.available();
			while(streamSize == 0) { streamSize = dIn.available();} // unlike readUTF, read() ends even if there is nothing in the data stream
			byte[] convertedMessage = new byte[streamSize]; // not to allocate more than is required
			
			dIn.read(convertedMessage); 
			
			IMessage<?> message = Encodeur_Decodeur.decoderMessage(convertedMessage); // decode the message received

			if(message.getInfoMessage().getWithACK() == true) sendACK(message.getInfoMessage().getIdMessage()); // sends an acknowledgement of receipt if desired by the message
			
			return message;
		}else {
			throw new MessageException("Input stream was incorrectly initialized");
		}
	}
	
	@Override
	public void sendMessageSynchronized(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			ACK(msg, timeOut);
		}else {
			throw new MessageException("Output stream was incorrectly initialized");
		}	
	}
	
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, MessageException {
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			new Thread() { // creation of a thread to allow parallelization of the return of the message if an acknowledgement of receipt has not been received
	    		public void run() {
					try {
						ACK(msg, timeOut);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}   
			}.start();
			
		}else {
			throw new MessageException("Output stream was incorrectly initialized");
		}
	}
}
