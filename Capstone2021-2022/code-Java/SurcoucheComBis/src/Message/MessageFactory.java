package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageFactory {
	
	/*
	 * Creates an generic message instance from a MessageString
	 * 
	 * @param message A string corresponding to the content of all the information linked to the generic message
	 * @return An instance of generic message corresponding to the type of message indicate into the MessageString
	 */
	public static IMessage<?> createMessage(MessageString message){
		
		switch(message.getInfoMessage().getMessageType()) {
		case "String":
			return  message;
		case "int":
			return  new MessageInt(message);
		case "float":
			return  new MessageFloat(message);
		case "boolean":
			return  new MessageBoolean(message);
		case "byte":
			return  new MessageByte(message);
		case "double":
			return  new MessageDouble(message);
		}
		
		return null;
	}
}
