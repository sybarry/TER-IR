package Message;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class Encodeur_Decodeur {
	
	/*
	 * Method to encode a string in an array of bytes
	 * 
	 * @param message The string to be encoder
	 * @return the encoding of the string in byte array
	 */
	public static byte[] encoderString(String message) {
		return message.getBytes();
	}
	
	/*
	 * Method for decoding a string
	 * 
	 * @param convertedMessage The string to be decoder
	 * @return the string after decoding perform
	 */
	public static String decoderString(byte[] convertedMessage) {
		return new String(convertedMessage);
	}
	
	/*
	 * Method to encode a generic message in an array of bytes
	 * 
	 * @param msg The generic message to be encoder
	 * @return the encoding of the generic message in byte array
	 */
	public static byte[] encoderMessage(IMessage<?> msg) {
		return msg.toString().getBytes();
	}
	
	/*
	 * Method for decoding a generic message
	 * 
	 * @param connvertedMessage The generic message to be decoder
	 * @return the generic message after decoding perform
	 */
	public static IMessage<?> decoderMessage(byte[] convertedMessage) {
		String message =  new String(convertedMessage); 
		MessageString messageString = MessageString.toMessageString(message);
		return MessageFactory.createMessage(messageString);
	}
}
