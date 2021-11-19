package com.remoteev3.app.Message;;

public class MessageBoolean extends AMessage<Boolean> {

	private boolean message;
	
	public MessageBoolean(boolean message) {
		super("boolean");
		this.message = message;
	}
	
	public MessageBoolean(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Boolean.parseBoolean(message.getMessage());
	}

	@Override
	public Boolean getMessage() {
		return message;
	}

	@Override
	public void setMessage(Boolean newMessage) {
		this.message = newMessage;
	}
}
