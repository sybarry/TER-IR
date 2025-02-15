package ConnectionCommunication;
import java.io.IOException;

import Divers.InfoConnection;
import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class ConnectionCommunicationBTClient extends AConnectionCommunication {

	private BTConnection BTLink;
	private String nomAppareil;
	/* Les differents mode de connexion qu'on peut mettre :
	 * NXTConnection.RAW (pour appareil de type tablette, telephone, ...)
	 * NXTConnection.PACKET (pour appareil de type brique NXT)
	 * NXTConnection.LCP (pour acc�der � distance au menus de la brique)
	 */
	private int modeConnexion; 

	public ConnectionCommunicationBTClient(String nomAppareil, int modeConnexion) {
		this.nomAppareil = nomAppareil;
		this.modeConnexion = modeConnexion;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("Connexion en cours");
		BTConnector bt = (BTConnector) Bluetooth.getNXTCommConnector();
		BTLink = (BTConnection) bt.connect(nomAppareil, modeConnexion);
		
		dOut = BTLink.openDataOutputStream();
		dIn = BTLink.openDataInputStream();
		
		System.out.println("Connexion accepte");
		
		// pas sur que ca marche 
		String nameSender = BrickFinder.getLocal().getName();
		//BrickInfo[] brikeSender = BrickFinder.find(nameSender);
		//String ipSender = brikeSender[0].getIPAddress();
		
		//BrickInfo[] brikeReceiver = BrickFinder.find(nomAppareil);
		//String ipReceiver = brikeReceiver[0].getIPAddress();
		
		infoConnection = new InfoConnection(nameSender, nomAppareil);
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Connection closed");
	}
}
