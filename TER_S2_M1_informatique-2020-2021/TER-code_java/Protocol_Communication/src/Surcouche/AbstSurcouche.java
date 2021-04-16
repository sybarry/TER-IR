package Surcouche;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import Message.IMessage;
import Couches.TrameLiaison;


public abstract class AbstSurcouche implements InterSurcouche{
	
	protected DataOutputStream dOut;
	protected DataInputStream dIn;
	
	protected String addressReceiver;
	protected String addressSender;
	
	protected TrameLiaison trameSender = new TrameLiaison();
	protected TrameLiaison trameReceiver =new TrameLiaison();;
	
	protected static AtomicInteger idMessage = new AtomicInteger(0);
	
	@Override 
	public abstract void openConnection() throws IOException;
	
	@Override
	public abstract void closeConnection() throws IOException;
	
	public String get_addressReceiver() {return addressReceiver;}
	public String get_addressSender() {return addressSender;}
	
	@Override
	public void sendACK(int idMessage) throws IOException { // à modifier quand sendMessage sera approuvé parce que peut etre envoyé un msg de type Message 
		dOut.writeUTF("received message "+idMessage); 
	}
	
	@Override
	public boolean receiveACK(int idMessage) throws IOException { // à modifier quand receiveMessage sera approuvé
		if(dIn.available() == 0) return false; // pour savoir si il y a des octets qui ont été envoyé dans le flux d'entrée car le read() attend qu'on lui envoie quelque chose avant de continuer le code
		return dIn.readUTF().contentEquals("received message "+idMessage);
	}	
	
	public void sendMessage(IMessage<?> msg) throws IOException{
		msg.setIdMessage(idMessage.getAndIncrement());	
		msg.setAddressReceiver(this.addressReceiver);
		msg.setAddressSender(this.addressSender);
		dOut.writeUTF(trameSender.sendTrameLiaison(msg,this));
		
		
		
		//dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
		//dOut.flush();
	}
	
	public IMessage<?> receiveMessage() throws IOException{
		IMessage<?> msg = trameReceiver.receiveTrameLiaison(dIn.readUTF(),this);
		msg.setAddressReceiver(this.addressReceiver);
		msg.setAddressSender(this.addressSender);
		return msg;
	}
	
	@Override
	public void sendMessageSynchronized(final IMessage<?> msg) throws IOException, InterruptedException{
		msg.setIdMessage(idMessage.getAndIncrement());		

		/*
		if(dOut != null) {
			dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
			dOut.flush();
			
			boolean ack = receiveACK(msg.getIdMessage());
			long chrono = java.lang.System.currentTimeMillis();
			
			while(ack == false) {
				ack = receiveACK(msg.getIdMessage());
				if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= 30000)) { // si il n'a pas recu un acusé de récéption et que ca fait 30 seconde que le premier message a été envoyé
					dOut.writeUTF(msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getMessage());
					dOut.flush();
					chrono = java.lang.System.currentTimeMillis();
				}
			}
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}	
		*/
	}
	
	
	/*
	 * peut etre que le message asynchrone correspond a send message donc peut etre faire un message asynchrone avec accusé de réception 
	 * Donc corresponderait au sendMessageSynchronized où le code qui correspond à ACK serait dans un Thread 
	 */
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, InterruptedException{
		msg.setIdMessage(idMessage.getAndIncrement());		
		/*
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
		    				if(ack == false && (java.lang.System.currentTimeMillis() - chrono >= 30000)) { // si il n'a pas recu un acusé de récéption et que ca fait 30 seconde que le premier message a été envoyé
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
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
		*/
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
