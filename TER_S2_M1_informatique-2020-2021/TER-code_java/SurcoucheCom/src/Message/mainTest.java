package Message;

public class mainTest {

	public static void main(String[] args) {
		String str = "bonjour@@au revoir";
		System.out.println(str);
		byte[] convertedMessage = Encodeur_Decodeur.encoderString(str);
		System.out.println(convertedMessage);
		String str1 = Encodeur_Decodeur.decoderString(convertedMessage);
		System.out.println(str1);
	}

}
