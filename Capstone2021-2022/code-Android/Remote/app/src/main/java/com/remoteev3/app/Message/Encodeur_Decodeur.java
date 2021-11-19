package com.remoteev3.app.Message;

public class Encodeur_Decodeur {
	
	public static byte[] encoderString(String message) {
		return message.getBytes();
	}
	
	public static String decoderString(byte[] convertedMessage) {
		return new String(convertedMessage);
	}
	
	public static byte[] encoderMessage(IMessage<?> msg) {
		return msg.toString().getBytes();
	}
	
	public static IMessage<?> decoderMessage(byte[] convertedMessage) {
		String message =  new String(convertedMessage); // pour convertire le convertedMessage en String
		MessageString messageString = MessageString.toMessageString(message);
		return MessageFactory.createMessage(messageString);
	}
}
