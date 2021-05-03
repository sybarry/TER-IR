package Message;

public class MessageFactory {
	
	public static IMessage<?> createMessage(String typeMessage, String[] message){

		switch(typeMessage) {
		case "String":
			return  new MessageString(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), message[5]);
		case "int":
			return  new MessageInt(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), Integer.parseInt(message[5]));
		case "float":
			return  new MessageFloat(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), Float.parseFloat(message[5]));
		case "boolean":
			return  new MessageBoolean(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), Boolean.parseBoolean(message[5]));
		case "byte":
			return  new MessageByte(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), Byte.parseByte(message[5]));
		case "double":
			return  new MessageDouble(Integer.parseInt(message[0]), message[1], message[2], message[3], Boolean.parseBoolean(message[4]), Double.parseDouble(message[5]));
		}
		
		return null;
	}	
	
	//utiliser lorsqu'on utilise la fonction decoderMessage dans Encodeur_Decodeur
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
