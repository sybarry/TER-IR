package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import Divers.InfoConnection;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;

public abstract class AConnectionCommunication implements IConnectionCommunication{
	
	protected DataOutputStream dOut;
	protected DataInputStream dIn;
	
	protected InfoConnection infoConnection;
	protected static AtomicInteger idMessage = new AtomicInteger(0);
	
	@Override 
	public abstract void openConnection() throws IOException;
	
	@Override
	public abstract void closeConnection() throws IOException;
	
	@Override
	public void sendACK(int idMessage) throws IOException { // � modifier quand sendMessage sera approuv� parce que peut etre envoy� un msg de type Message 
		dOut.writeUTF("received message "+idMessage); 
	}
	
	@Override
	public boolean receiveACK(int idMessage) throws IOException { // � modifier quand receiveMessage sera approuv�
		if(dIn.available() == 0) return false; // pour savoir si il y a des octets qui ont �t� envoy� dans le flux d'entr�e car le read() attend qu'on lui envoie quelque chose avant de continuer le code
		return dIn.readUTF().contentEquals("received message "+idMessage);
	}	
	
	public void sendMessage(IMessage<?> msg) throws IOException, MessageException{
		msg.setIdMessage(idMessage.getAndIncrement());		
		msg.setInfoConnection(this.infoConnection);
		
		if(dOut != null) {
			dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}
	}
	
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		if(dIn != null) {
			String[] message = dIn.readUTF().split("@@");
			
			//if(Boolean.parseBoolean(message[5])) sendACK(Integer.parseInt(message[0])); // si on rajoute un 6 eme param�tre qui indique si le sender veut un accus� de reception
			
			switch(message[3]) {
			case "String":
				return  new MessageString(Integer.parseInt(message[0]), message[1], message[2], message[3], message[4]);
			/*case "int":
				return  new MessageInt2(Integer.parseInt(message[0]), message[1], message[2], message[3], Integer.parseInt(message[4]));
			case "float":
				return  new MessageFloat2(Integer.parseInt(message[0]), message[1], message[2], message[3], Float.parseFloat(message[4]));
			case "boolean":
				return  new MessageBoolean2(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]));
			case "byte":
				return  new MessageByte2(Integer.parseInt(message[0]), message[1], message[2], message[3], Byte.parseByte(message[4]));*/
			}
			return null;
		}else {
			throw new MessageException("Le flux d'entr�e � �t� mal initialis�");
		}
	}
	
	@Override
	public void sendMessageSynchronized(final IMessage<?> msg) throws IOException, InterruptedException, MessageException{
		msg.setIdMessage(idMessage.getAndIncrement());		
		msg.setInfoConnection(this.infoConnection);
		
		if(dOut != null) {
			dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
			dOut.flush();
			
			boolean ack = receiveACK(msg.getIdMessage());
			long chrono = java.lang.System.currentTimeMillis();
			
			while(ack == false) {
				ack = receiveACK(msg.getIdMessage());
				if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= 30000)) { // si il n'a pas recu un acus� de r�c�ption et que ca fait 30 seconde que le premier message a �t� envoy�
					dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
					dOut.flush();
					chrono = java.lang.System.currentTimeMillis();
				}
			}
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}	
	}
	
	
	/*
	 * peut etre que le message asynchrone correspond a send message donc peut etre faire un message asynchrone avec accus� de r�ception 
	 * Donc corresponderait au sendMessageSynchronized o� le code qui correspond � ACK serait dans un Thread 
	 */
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, InterruptedException, MessageException {
		msg.setIdMessage(idMessage.getAndIncrement());		
		msg.setInfoConnection(this.infoConnection);
		
		if(dOut != null) {
			dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
			dOut.flush();
			
			new Thread() {
	    		public void run() {
	    			boolean ack;
					try {
						ack = receiveACK(msg.getIdMessage());
						long chrono = java.lang.System.currentTimeMillis();
		    			
		    			while(ack == false) {
		    				ack = receiveACK(msg.getIdMessage());
		    				if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= 30000)) { // si il n'a pas recu un acus� de r�c�ption et que ca fait 30 seconde que le premier message a �t� envoy�
		    					dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
		    					dOut.flush();
		    					chrono = java.lang.System.currentTimeMillis();
		    				}
		    			}
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
	
	
	
	

	@Override
	public Object receiveMessageWithACK(int mode) throws IOException{
		Object result = null;
		switch(mode) {
		case 1:
			result = dIn.readUTF();
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 2:
			result = dIn.readInt();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 3:
			result = dIn.readBoolean();
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 4:
			result = dIn.readFloat();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 5:
			result = dIn.readDouble();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 6:
			result = dIn.readShort();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 7:
			result = dIn.readByte();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 8:
			result = dIn.readChar();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 9:
			result = dIn.readLong();
			dOut.writeByte(1);
			dOut.flush();
			break;
		default:
			break;
			
		}
		return result;
	}
}
