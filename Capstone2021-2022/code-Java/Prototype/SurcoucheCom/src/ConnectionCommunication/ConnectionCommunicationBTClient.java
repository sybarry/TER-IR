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

public class ConnectionCommunicationBTClient extends AConnectionCommunication {

	private BTConnection BTLink;
	private String deviceName; // The name of the device to connect to
	
	/* The different connection modes that can be set:
	 * NXTConnection.RAW (for tablet, telephone, ...)
	 * NXTConnection.PACKET (for NXT brick)
	 * NXTConnection.LCP (for remote access to brick menus)
	 */
	private int connectionMode; 

	/*
	 * Create an instance for a Bluetooth client
	 * 
	 * @param deviceName The name of the device to connect to
	 * @param connectionMode The connection mode that reference to device 
	 */
	public ConnectionCommunicationBTClient(String deviceName, int connectionMode) {
		this.deviceName = deviceName;
		this.connectionMode = connectionMode;
		this.dOut = null;
		this.dIn = null;
		this.BTLink = null;
	}

	public BTConnection getBTLink() {
		return this.BTLink;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public int getConnectionMode() {
		return this.connectionMode;
	}
	
	@Override
	public void openConnection() throws IOException {
		System.out.println("Bluetooth connection in progress");
		BTConnector bt = (BTConnector) Bluetooth.getNXTCommConnector(); 
		BTLink = (BTConnection) bt.connect(deviceName, connectionMode); // established the connection with the device "deviceName"
		
		dOut = BTLink.openDataOutputStream(); // initializes the output stream of the connection between the two systems
		dIn = BTLink.openDataInputStream(); // initializes the input stream of the connection between the two systems
		
		System.out.println("Bluetooth connection accepted");
		
		// pas sur que ca marche 
		String nameSender = BrickFinder.getLocal().getName();
		//BrickInfo[] brikeSender = BrickFinder.find(nameSender);
		//String ipSender = brikeSender[0].getIPAddress();
		
		//BrickInfo[] brikeReceiver = BrickFinder.find(nomAppareil);
		//String ipReceiver = brikeReceiver[0].getIPAddress();
		
		infoConnection = new InfoConnection(nameSender, deviceName);
	}

	@Override
	public void closeConnection() throws IOException {
		BTLink.close();
		/*donneeSortie.close();
		donneeEntree.close();*/
		System.out.println("Bluetooth connection closed");
	}
}
