package Couches;

import Message.IMessage;
import Surcouche.AbstSurcouche;

public class TrameReseau extends TrameTransport {
	protected String trame;
	protected String enTeteTramReseau;
	
	public TrameReseau()
	{
		trame = "";
	}
	
	public String sendTrameReseau(IMessage<?> msg, AbstSurcouche aSurcouche ) {
		enTeteTramReseau = aSurcouche.get_addressSender()+"##"+aSurcouche.get_addressReceiver()+"##";
		trame = enTeteTramReseau+sendTrameTransport(msg);
		return trame;
	}
	
	public  IMessage<?> receiverTrameReseau(String msg) {
		String[] message = msg.split("##");
		
		//Possibilité de comparer les adresses 
		
		return receiverTrameTransport(message[2]) ;
	}

}
