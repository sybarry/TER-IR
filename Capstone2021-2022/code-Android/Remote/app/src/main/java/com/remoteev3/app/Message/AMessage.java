package com.remoteev3.app.Message;

import com.remoteev3.app.Divers.InfoConnection;

public abstract class AMessage<T> implements IMessage<T> {

	private int idMessage;
	private String typeMessage;
	private boolean withACK;	
	private InfoConnection infoConnection;
	
	public AMessage(String typeMessage) {
		this.idMessage = -1;
		this.infoConnection = null;
		this.typeMessage = typeMessage;
		this.withACK = false;
	}
	
	public AMessage(int idMessage, InfoConnection infoConnection, String typeMessage, boolean withACK) { //Constructeur utilis� lorsqu'on utilise decoderMessage
		this.idMessage = idMessage;
		this.infoConnection = infoConnection;
		this.typeMessage = typeMessage;
		this.withACK = withACK;
	}
	
	public int getIdMessage() {
		return this.idMessage;
	}
	
	public InfoConnection getInfoConnection() {
		return this.infoConnection;
	}
	
	public String getTypeMessage() {
		return this.typeMessage;
	}
	
	public abstract T getMessage();	
	
	@Override
	public boolean getWithACK() {
		return this.withACK;
	}
	
	@Override
	public void setIdMessage(int newIdMessage) {
		this.idMessage = newIdMessage;
	}
	
	@Override
	public void setInfoConnection(InfoConnection newInfoConnection) {
		this.infoConnection = newInfoConnection;
	}

	@Override
	public void setTypeMessage(String newTypeMessage) {
		this.typeMessage = newTypeMessage;
	}
	
	public abstract void setMessage(T newMessage);
	
	@Override
	public void setWithACK(boolean newWithACK) {
		this.withACK = newWithACK;
	}
	
	@Override
	public String toString() {
		return this.idMessage+"@@"+this.infoConnection.toString()+"&&"+this.typeMessage+"##"+this.withACK+"##"+this.getMessage();
	}
	
	
	public static MessageString toMessageString(String message){ // return MessageString car c'est le seul type (avec Byte) qui peut etre convertie dans tout les autres type 
		String[] messageSplit1 =  message.split("@@");
		String[] messageSplit2 =  messageSplit1[1].split("&&");
		String[] messageSplit3 =  messageSplit2[1].split("##");
		
		return new MessageString(Integer.parseInt(messageSplit1[0]), messageSplit2[0], messageSplit3[0], Boolean.parseBoolean(messageSplit3[1]), messageSplit3[2]);
	}
}

//la methode toMessageString permet de ne plus se pr�ocup� de la longueur de infoConnection donc l'ajout de parametre dans infoConnection n'entrainera pas de grande modification dans le code
