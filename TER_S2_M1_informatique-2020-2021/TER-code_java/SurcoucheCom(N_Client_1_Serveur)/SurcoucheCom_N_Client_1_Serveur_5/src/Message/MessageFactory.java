package Message;

public class MessageFactory {
	
	public static IMessage<?> createMessage(MessageString message){
		
		switch(message.getTypeMessage()) {
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
