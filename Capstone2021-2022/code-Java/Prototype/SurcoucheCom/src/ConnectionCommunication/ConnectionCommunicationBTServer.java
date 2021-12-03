package ConnectionCommunication;
import java.io.IOException;

import Divers.InfoConnection;
import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class ConnectionCommunicationBTServer extends AConnectionCommunication {

	private BTConnection BTLink;
	
	/* The different connection modes that can be set:
	 * NXTConnection.RAW (for tablet, telephone, ...)
	 * NXTConnection.PACKET (for NXT brick)
	 * NXTConnection.LCP (for remote access to brick menus)
	 */
	private int connectionMode;
	private int timeOut; // Time in ms to wait for the connection to be made

	/*
	 * Create an instance for a Bluetooth server
	 * 
	 * @param connectionMode The connection mode that reference to device 
	 * @param timeOut Time in ms to wait for the connection to be made
	 */
	public ConnectionCommunicationBTServer(int connectionMode, int timeOut) {
		this.connectionMode = connectionMode;
		this.timeOut = timeOut;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
	}
	
	public BTConnection getBTLink() {
		return this.BTLink;
	}
	
	public int getConnectionMode() {
		return this.connectionMode;
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("Bluetooth connection in progress");
	    BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    BTLink = (BTConnection) nxtCommConnector.waitForConnection(timeOut, connectionMode); // to wait for a client connection with a device type connectionMode during timeOut milliseconds
		
	    dOut = BTLink.openDataOutputStream(); // initializes the output stream of the connection between the two systems
	    dIn = BTLink.openDataInputStream();	// initializes the input stream of the connection between the two systems
		
	    System.out.println("Bluetooth connection accepted");
		
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
		System.out.println("Bluetooth connection closed");
	}
}
