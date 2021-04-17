package Message;

public class Encodeur_Decodeur {
	
	public static byte[] encoderString(String message) {
		return message.getBytes();
	}
	
	public static String decoderString(byte[] convertedMessage) {
		return new String(convertedMessage);
	}
	
	public static byte[] encoderMessage(IMessage<?> msg) {
		String message = msg.getIdMessage()+"@@"+msg.getInfoConnection().getSender()+"@@"+msg.getInfoConnection().getReceiver()+"@@"+msg.getTypeMessage()+"@@"+msg.getWithACK()+"@@"+msg.getMessage();
		return message.getBytes();
	}
	
	public static MessageString decoderMessage(byte[] convertedMessage) {
		String message =  new String(convertedMessage);
		String[] messageSplit = message.split("@@");
		return new MessageString(Integer.parseInt(messageSplit[0]), messageSplit[1], messageSplit[2], messageSplit[3], Boolean.parseBoolean(messageSplit[4]), messageSplit[5]);
	}
}
