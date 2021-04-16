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
	
	
	public void sendMessage(IMessage<?> msg) throws IOException{
		msg.setIdMessage(idMessage.getAndIncrement());	
		msg.setAddressReceiver(this.addressReceiver);
		msg.setAddressSender(this.addressSender);
		dOut.writeUTF(trameSender.sendTrameLiaison(msg,this));
	}
	
	public IMessage<?> receiveMessage() throws IOException{
		IMessage<?> msg = trameReceiver.receiveTrameLiaison(dIn.readUTF(),this);
		msg.setAddressReceiver(this.addressReceiver);
		msg.setAddressSender(this.addressSender);
		return msg;
	}
	
	//TO DO !!!
	@Override
	public void sendMessageSynchronized(final IMessage<?> msg) throws IOException, InterruptedException{

	}
	
	//TO DO !!!
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg) throws IOException, InterruptedException{	

	}
	
	
	//TO DO !!!
	@Override
	public void sendACK(int idMessage) throws IOException { // à modifier quand sendMessage sera approuvé parce que peut etre envoyé un msg de type Message 
		dOut.writeUTF("received message "+idMessage); 
	}
	
	//TO DO !!!
	@Override
	public boolean receiveACK(int idMessage) throws IOException { // à modifier quand receiveMessage sera approuvé
		if(dIn.available() == 0) return false; // pour savoir si il y a des octets qui ont été envoyé dans le flux d'entrée car le read() attend qu'on lui envoie quelque chose avant de continuer le code
		return dIn.readUTF().contentEquals("received message "+idMessage);
	}	
	
	public String get_addressReceiver() {return addressReceiver;}
	public String get_addressSender() {return addressSender;}
}
