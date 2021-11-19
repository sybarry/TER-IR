package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.remoteev3.app.Divers.InfoConnection;
import com.remoteev3.app.Exception.MessageException;
import com.remoteev3.app.Message.Encodeur_Decodeur;
import com.remoteev3.app.Message.IMessage;

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
		msg.setWithACK(withACK);
	}
	
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, false);
		
		if(dOut != null) {
			writeMessage(msg);
		}else {
			throw new MessageException("Le flux de sortie a été mal initialisé");
		}
	}
	
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		if(dIn != null) {
			int streamSize = dIn.available();
			while(streamSize == 0) { streamSize = dIn.available();} // car contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donn�e, donc si on laisse le read pass� sans rien dedans , cela renvoie une erreur
			byte[] convertedMessage = new byte[streamSize]; //pour ne pas allouer plus de case qu'il n'en faut car autrement ca peut cr�er des probl�me
			
			dIn.read(convertedMessage); //contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donn�e
			
			IMessage<?> message = Encodeur_Decodeur.decoderMessage(convertedMessage);

			if(message.getWithACK() == true) sendACK(message.getIdMessage()); 
			
			return message;
		}else {
			throw new MessageException("Le flux d'entr�e � �t� mal initialis�");
		}
	}
	
	@Override
	public void sendMessageSynchronized(IMessage<?> msg) throws IOException, MessageException{
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			ACK(msg, timeOut);
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
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
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}
	}
}
