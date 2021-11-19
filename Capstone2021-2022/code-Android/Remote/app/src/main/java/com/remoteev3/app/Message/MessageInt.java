package com.remoteev3.app.Message;;

public class MessageInt extends AMessage<Integer> {

	private int message;
	
	public MessageInt(int message) {
		super("int");
		this.message = message;
	}
	
	public MessageInt(MessageString message) {
		super(message.getIdMessage(), message.getInfoConnection(), message.getTypeMessage(), message.getWithACK());
		this.message = Integer.parseInt(message.getMessage());
	}

	@Override
	public Integer getMessage() {
		return message;
	}

	@Override
	public void setMessage(Integer newMessage) {
		this.message = newMessage;
	}
}
