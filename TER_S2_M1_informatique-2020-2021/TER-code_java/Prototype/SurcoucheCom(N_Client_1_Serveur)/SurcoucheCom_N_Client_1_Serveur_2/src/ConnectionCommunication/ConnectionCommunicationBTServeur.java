package ConnectionCommunication;
import java.io.IOException;

import Divers.InfoConnection;
import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class ConnectionCommunicationBTServeur extends AConnectionCommunicationBis {

	private BTConnection BTLink;
	/* Les differents mode de connexion qu'on peut mettre :
	 * NXTConnection.RAW (pour appareil de type tablette, telephone, ...)
	 * NXTConnection.PACKET (pour appareil de type brique NXT)
	 * NXTConnection.LCP (pour acc�der � distance au menus de la brique)
	 */
	private int modeConnexion; 
	private int timeOut; // Time in ms to wait for the connection to be made

	public ConnectionCommunicationBTServeur(int modeConnexion, int timeOut) {
		this.modeConnexion = modeConnexion;
		this.timeOut = timeOut;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("En ecoute");
	    BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    BTLink = (BTConnection) nxtCommConnector.waitForConnection(timeOut, modeConnexion);
		
	    dOut = BTLink.openDataOutputStream();
	    dIn = BTLink.openDataInputStream();	
		
		System.out.println("Connexion effectue");	
		
		// pas sur que ca marche 
		String nameSender = BrickFinder.getLocal().getName();
		//BrickInfo[] brikeSender = BrickFinder.find(nameSender);
		//String ipSender = brikeSender[0].getIPAddress();
		
		String nameReceiver = BrickFinder.getLocal().getBluetoothDevice().getFriendlyName();
		
		infoConnection = new InfoConnection(nameSender, nameReceiver);
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Connection closed");
	}
}
