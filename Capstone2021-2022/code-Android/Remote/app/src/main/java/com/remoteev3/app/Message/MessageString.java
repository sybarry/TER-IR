package com.remoteev3.app.Message;;

import com.remoteev3.app.Divers.InfoConnection;

public class MessageString extends AMessage<String> {

	private String message;
	
	public MessageString(String message) {
		super("String");
		this.message = message;
	}
	
	public MessageString(MessageString message) { //Constructeur utilis� lorsqu'on utilise la factory dans receiveMessage
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = message.getMessage();
	}
	
	public MessageString(int idMessage, String infoConnection, String typeMessage, boolean withACK, String message) { // Constructeur utilis� lorsqu'on utilise toMessageString dans decoderMessagee
		super(idMessage, InfoConnection.toInfoConnection(infoConnection), typeMessage, withACK);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String newMessage) {
		this.message = newMessage;
	}
}
