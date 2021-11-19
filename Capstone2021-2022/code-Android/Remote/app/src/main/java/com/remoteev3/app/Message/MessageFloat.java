package com.remoteev3.app.Message;;

public class MessageFloat extends AMessage<Float> {

	private float message;
	
	public MessageFloat(float message) {
		super("float");
		this.message = message;
	}
	
	public MessageFloat(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Float.parseFloat(message.getMessage());
	}

	@Override
	public Float getMessage() {
		return message;
	}

	@Override
	public void setMessage(Float newMessage) {
		this.message = newMessage;
	}
}
