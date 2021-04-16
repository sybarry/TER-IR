package Couches;

import Message.IMessage;
import Message.MessageString;

public class TrameTransport extends TrameApplication {
	protected String trame;
	protected String enTeteTramReseau;
	
	public TrameTransport()
	{
		trame = "";
	}
	
	public String sendTrameTransport(IMessage<?> msg) {
		enTeteTramReseau = msg.getIdMessage()+"&&"+msg.getTypeMessage()+"&&";
		trame = enTeteTramReseau+sendTrameApplication(msg);
		return trame;
	}
	
	public  IMessage<?> receiverTrameTransport(String msg) {
		String[] message = msg.split("&&");
		
		//Possibilité de comparer les adresses 
		switch(message[1]) {
		case "String":
			return  new MessageString(Integer.parseInt(message[0]),message[1],receiverTrameApplication(message[2]));
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
	}
}
