package Overlay;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class OverlayBTServer extends AOverlay {

	private BTConnection BTLink;
	/* Les differents mode de connexion qu'on peut mettre :
	 * NXTConnection.RAW (pour appareil de type tablette, telephone, ...)
	 * NXTConnection.PACKET (pour appareil de type brique NXT)
	 * NXTConnection.LCP (pour accéder à distance au menus de la brique)
	 */
	private int modeConnexion; 
	private int timeOut; // Time in ms to wait for the connection to be made

	public OverlayBTServer(int modeConnexion, int timeOut) {
		this.modeConnexion = modeConnexion;
		this.timeOut = timeOut;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
		
		
		// TO DO !!!!!!
		addressSender = "";
		addressReceiver = "";
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("En ecoute");
	    BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    BTLink = (BTConnection) nxtCommConnector.waitForConnection(timeOut, modeConnexion);
		
	    dOut = BTLink.openDataOutputStream();
	    dIn = BTLink.openDataInputStream();	
		
		System.out.println("Connexion effectue");	 
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Connection closed");
	}
}
