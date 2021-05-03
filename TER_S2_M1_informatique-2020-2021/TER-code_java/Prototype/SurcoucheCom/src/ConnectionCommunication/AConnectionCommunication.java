package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Divers.InfoConnection;
import Exception.MessageException;
import Message.Encodeur_Decodeur;
import Message.IMessage;

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
		byte[] messageConverted = Encodeur_Decodeur.encoderMessage(msg);
		dOut.write(messageConverted);
		dOut.flush();
	}
	
	@Override
	public void sendACK(int idMessage) throws IOException {
		dOut.writeUTF("received message "+idMessage); 
		dOut.flush();
	}
	
	@Override
	public boolean receiveACK(int idMessage) throws IOException { 
		if(dIn.available() == 0) return false; // pour savoir si il y a des octets qui ont été envoyé dans le flux d'entrée car le readUTF() attend qu'on lui envoie quelque chose avant de continuer le code
		return dIn.readUTF().contentEquals("received message "+idMessage); // regarder si le read supprime le message ou le laisse 
	}
	
	private void ACK(IMessage<?> msg, int timeOut) throws IOException {
		boolean ack = receiveACK(msg.getIdMessage());
		long chrono = java.lang.System.currentTimeMillis();
		
		while(ack == false) {
			ack = receiveACK(msg.getIdMessage());
			if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= timeOut)) { // si il n'a pas recu un acusé de récéption et que ca fait 30 seconde que le premier message a été envoyé
				writeMessage(msg);
				chrono = java.lang.System.currentTimeMillis();
			}
		}
	}
	
	private void initialisationInfoMessage(IMessage<?> msg, boolean withACK) {
		idMessage = idMessage + 1;
		msg.setIdMessage(idMessage);		
		msg.setInfoConnection(this.infoConnection);
		msg.setWithACK(withACK);
	}
	
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, false);
		
		if(dOut != null) {
			writeMessage(msg);
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		if(dIn != null) {
			int streamSize = dIn.available();
			while(streamSize == 0) { streamSize = dIn.available();} // car contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donnée, donc si on laisse le read passé sans rien dedans , cela renvoie une erreur
			byte[] convertedMessage = new byte[streamSize]; //pour ne pas allouer plus de case qu'il n'en faut car autrement ca peut créer des problème
			
			dIn.read(convertedMessage); //contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donnée
			
			IMessage<?> message = Encodeur_Decodeur.decoderMessage(convertedMessage);

			if(message.getWithACK() == true) sendACK(message.getIdMessage()); 
			
			return message;
		}else {
			throw new MessageException("Le flux d'entrée à été mal initialisé");
		}
	}
	
	@Override
	public void sendMessageSynchronized(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			ACK(msg, timeOut);
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}	
	}
	
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, MessageException {
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
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
}
