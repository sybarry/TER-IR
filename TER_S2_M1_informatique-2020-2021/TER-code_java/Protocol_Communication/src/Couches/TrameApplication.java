package Couches;

import Message.IMessage;

public class TrameApplication {
	protected String trame;
	
	public TrameApplication()
	{
		trame = "";
	}
	
	public String sendTrameApplication(IMessage<?> msg) {
		trame = (String) msg.getMessage();
		return trame;
	}
	
	public String receiverTrameApplication(String msg) {
		return msg;
	}

}
