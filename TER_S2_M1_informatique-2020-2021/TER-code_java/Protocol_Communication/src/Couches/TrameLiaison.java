package Couches;

import Message.IMessage;
import Surcouche.AbstSurcouche;

public class TrameLiaison extends TrameReseau{
	protected String trame;
	protected String enTeteTramLiaison;
	protected String piedTramLiaison;
	
	public TrameLiaison()
	{
		trame = "";
	}
	
	public String sendTrameLiaison(IMessage<?> msg, AbstSurcouche aSurcouche ) {
		enTeteTramLiaison = "1@@";
		piedTramLiaison = "@@1";
		trame = enTeteTramLiaison+sendTrameReseau(msg,aSurcouche)+piedTramLiaison;
		return trame;
	}
	
	public IMessage<?> receiveTrameLiaison(String trameReceiver, AbstSurcouche aSurcouche) {
		String[] message = trameReceiver.split("@@");
		
		return receiverTrameReseau(message[1]) ;
	}

}
