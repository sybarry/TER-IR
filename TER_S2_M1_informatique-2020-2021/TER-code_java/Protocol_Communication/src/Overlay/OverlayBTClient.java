package Overlay;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class OverlayBTClient extends AOverlay {

	private BTConnection BTLink;
	private String nomAppareil;
	/* Les differents mode de connexion qu'on peut mettre :
	 * NXTConnection.RAW (pour appareil de type tablette, telephone, ...)
	 * NXTConnection.PACKET (pour appareil de type brique NXT)
	 * NXTConnection.LCP (pour accéder à distance au menus de la brique)
	 */
	private int modeConnexion; 

	public OverlayBTClient(String nomAppareil, int modeConnexion) {
		this.nomAppareil = nomAppareil;
		this.modeConnexion = modeConnexion;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
		
		// TO DO !!!!!!
		addressSender = "";
		addressReceiver = "";
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("Connexion en cours");
		BTConnector bt = (BTConnector) Bluetooth.getNXTCommConnector();
		BTLink = (BTConnection) bt.connect(nomAppareil, modeConnexion);
		
		dOut = BTLink.openDataOutputStream();
		dIn = BTLink.openDataInputStream();
		
		System.out.println("Connexion accepte");
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Connection closed");
	}

}
