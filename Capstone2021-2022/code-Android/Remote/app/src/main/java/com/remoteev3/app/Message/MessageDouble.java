package com.remoteev3.app.Message;;

public class MessageDouble extends AMessage<Double> {

	private double message;
	
	public MessageDouble(double message) {
		super("double");
		this.message = message;
	}
	
	public MessageDouble(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Double.parseDouble(message.getMessage());
	}

	@Override
	public Double getMessage() {
		return message;
	}

	@Override
	public void setMessage(Double newMessage) {
		this.message = newMessage;
	}
}
