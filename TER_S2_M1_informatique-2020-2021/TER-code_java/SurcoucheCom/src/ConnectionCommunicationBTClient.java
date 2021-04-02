import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class ConnectionCommunicationBTClient implements IConnectionCommunication {

	private DataOutputStream donneeSortie; 
	private DataInputStream donneeEntree;
	private BTConnection BTLink;
	private String nomAppareil;
	/* Les differents mode de connexion qu'on peut mettre :
	 * NXTConnection.RAW (pour appareil de type tablette, telephone, ...)
	 * NXTConnection.PACKET (pour appareil de type brique NXT)
	 * NXTConnection.LCP (pour accéder à distance au menus de la brique)
	 */
	private int modeConnexion; 

	public ConnectionCommunicationBTClient(String nomAppareil, int modeConnexion) {
		this.nomAppareil = nomAppareil;
		this.modeConnexion = modeConnexion;
		this.donneeSortie = null;
		this.donneeEntree = null;
		this.BTLink = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("Connexion en cours");
		BTConnector bt = (BTConnector) Bluetooth.getNXTCommConnector();
		BTLink = (BTConnection) bt.connect(nomAppareil, modeConnexion);
		
		donneeSortie = BTLink.openDataOutputStream();
		donneeEntree = BTLink.openDataInputStream();
		
		System.out.println("Connexion accepte");
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Connection closed");
	}

	@Override
	public void sendMessage(Object message, int mode) throws IOException{
		switch(mode) {
		case 1:
			donneeSortie.writeUTF((String) message); 
			donneeSortie.flush();
			break;
		case 2:
			donneeSortie.writeInt((int) message); 
			donneeSortie.flush();
			break;
		case 3:
			donneeSortie.writeBoolean((boolean) message); 
			donneeSortie.flush();
			break;
		case 4:
			donneeSortie.writeFloat((float) message); 
			donneeSortie.flush();
			break;
		case 5:
			donneeSortie.writeDouble((double) message); 
			donneeSortie.flush();
			break;
		case 6:
			donneeSortie.writeShort((short) message); 
			donneeSortie.flush();
			break;
		case 7:
			donneeSortie.writeByte((Byte) message); 
			donneeSortie.flush();
			break;
		case 8:
			donneeSortie.writeChar((char) message); 
			donneeSortie.flush();
			break;
		case 9:
			donneeSortie.writeLong((long) message); 
			donneeSortie.flush();
			break;
		default:
			break;
			
		}
				
	}
	@Override
	public boolean sendMessageWithACK(Object message, int mode) throws IOException, InterruptedException{
		boolean result = false;
		switch(mode) {
		case 1:
			donneeSortie.writeUTF((String) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 2:
			donneeSortie.writeInt((int) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 3:
			donneeSortie.writeBoolean((boolean) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 4:
			donneeSortie.writeFloat((float) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 5:
			donneeSortie.writeDouble((double) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 6:
			donneeSortie.writeShort((short) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 7:
			donneeSortie.writeByte((Byte) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 8:
			donneeSortie.writeChar((char) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		case 9:
			donneeSortie.writeLong((long) message); 
			donneeSortie.flush();
			TimeUnit.SECONDS.sleep(1);
			if(donneeEntree.readByte() == 1) {
				result = true;
			}
			return result;
		default:
			return result;
			
		}
				
	}

	@Override
	public Object receiveMessage(int mode) throws IOException{
		switch(mode) {
		case 1:
			return donneeEntree.readUTF();	
		case 2:
			return donneeEntree.readInt();	
		case 3:
			return donneeEntree.readBoolean();	
		case 4:
			return donneeEntree.readFloat();	
		case 5:
			return donneeEntree.readDouble();	
		case 6:
			return donneeEntree.readShort();	
		case 7:
			return donneeEntree.readByte();	
		case 8:
			return donneeEntree.readChar();	
		case 9:
			return donneeEntree.readLong();	
		default:
			System.out.println("Error mode");
			return new IOException();
			
		}
			
	}
	@Override
	public Object receiveMessageWithACK(int mode) throws IOException{
		Object result = null;
		switch(mode) {
		case 1:
			result = donneeEntree.readUTF();
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 2:
			result = donneeEntree.readInt();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 3:
			result = donneeEntree.readBoolean();
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 4:
			result = donneeEntree.readFloat();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 5:
			result = donneeEntree.readDouble();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 6:
			result = donneeEntree.readShort();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 7:
			result = donneeEntree.readByte();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 8:
			result = donneeEntree.readChar();	
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		case 9:
			result = donneeEntree.readLong();
			donneeSortie.writeByte(1);
			donneeSortie.flush();
			break;
		default:
			break;
			
		}
		return result;
	}
}
