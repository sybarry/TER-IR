package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Divers.InfoConnection;
import Exception.MessageException;
import Message.Encodeur_Decodeur;
import Message.IMessage;
import Message.MessageFactory;
import Message.MessageString;

public abstract class AConnectionCommunication implements IConnectionCommunication{
	
	protected static int timeOut = 30000;
	protected DataOutputStream dOut;
	protected DataInputStream dIn;
	
	protected InfoConnection infoConnection;
	protected int idMessage = 0;
	
	@Override 
	public abstract void openConnection() throws IOException;
	
	@Override
	public abstract void closeConnection() throws IOException;
	
	private void writeMessage(IMessage<?> msg) throws IOException {
		
		/////////////////////////////// Pour le encoderMessage /////////////////////////////////
		
		byte[] messageConverted = Encodeur_Decodeur.encoderMessage(msg);
		dOut.write(messageConverted);
		
		/////////////////////////////// Pour le encoderString /////////////////////////////////
		
		/*String message = msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getWithACK()+"@@"+msg.getMessage();
		dOut.write(Encoder_Decoder.encoder(message));*/
		
		/////////////////////////////// Sans encodeur /////////////////////////////////
		
		//dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getWithACK()+"@@"+msg.getMessage());
		dOut.flush();
	}
	
	@Override
	public void sendACK(int idMessage) throws IOException {
		dOut.writeUTF("received message "+idMessage); 
	}
	
	@Override
	public boolean receiveACK(int idMessage) throws IOException { 
		if(dIn.available() == 0) return false; // pour savoir si il y a des octets qui ont �t� envoy� dans le flux d'entr�e car le readUTF() attend qu'on lui envoie quelque chose avant de continuer le code
		return dIn.readUTF().contentEquals("received message "+idMessage); // regarder si le read supprime le message ou le laisse 
	}
	
	private void ACK(IMessage<?> msg, int timeOut) throws IOException {
		boolean ack = receiveACK(msg.getIdMessage());
		long chrono = java.lang.System.currentTimeMillis();
		
		while(ack == false) {
			ack = receiveACK(msg.getIdMessage());
			if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= timeOut)) { // si il n'a pas recu un acus� de r�c�ption et que ca fait 30 seconde que le premier message a �t� envoy�
				writeMessage(msg);
				chrono = java.lang.System.currentTimeMillis();
			}
		}
	}
	
	private void initialisationInfoMessage(IMessage<?> msg, boolean withACK) {
		idMessage = idMessage + 1;
		msg.setIdMessage(idMessage);		
		msg.setInfoConnection(this.infoConnection);
		msg.setWithACK(true);
	}
	
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, false);
		
		if(dOut != null) {
			writeMessage(msg);
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}
	}
	
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		if(dIn != null) {
			
			/////////////////////////////// Pour le DecoderMessage /////////////////////////////////
			
			while(dIn.available() == 0) {}
			byte[] convertedMessage = new byte[dIn.available()]; //pour ne pas allouer plus de case qu'il n'en faut car autrement ca peut cr�er des probl�me
			dIn.read(convertedMessage); //contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donn�e
			MessageString message = Encodeur_Decodeur.decoderMessage(convertedMessage);
			if(message.getWithACK() == true) sendACK(message.getIdMessage()); 
			return MessageFactory.createMessage(message);
			
			/////////////////////////////// Pour le DecoderString /////////////////////////////////
			
			/*while(dIn.available() == 0) {} // car contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donn�e
			byte[] convertedMessage = new byte[dIn.available()]; //pour ne pas allouer plus de case qu'il n'en faut car autrement ca peut cr�er des probl�me
			dIn.read(convertedMessage); 
			String[] message = Encoder_Decoder.decoderString(convertedMessage).split("@@");*/
			
			/////////////////////////////// Sans decodeur /////////////////////////////////
			
			/*String[] message = dIn.readUTF().split("@@");
			
			if(Boolean.parseBoolean(message[4]) == true) sendACK(Integer.parseInt(message[0])); 
			
			return MessageFactory.createMessage(message[3], message);*/
		}else {
			throw new MessageException("Le flux d'entr�e � �t� mal initialis�");
		}
	}
	
	@Override
	public void sendMessageSynchronized(IMessage<?> msg) throws IOException, InterruptedException, MessageException{
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			ACK(msg, timeOut);
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}	
	}
	
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, InterruptedException, MessageException {
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			new Thread() {
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
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}
	}
}
