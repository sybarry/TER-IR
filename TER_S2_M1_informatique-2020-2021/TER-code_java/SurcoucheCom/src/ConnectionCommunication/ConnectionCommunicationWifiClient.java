package ConnectionCommunication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Divers.InfoConnection;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class ConnectionCommunicationWifiClient extends AConnectionCommunication {

	private Socket client;
	private int port; // the port of wifi connection 
	private String ip; // the identifer od wifi server
	
	/*
	 * Create an instance for a Wifi client
	 * 
	 * @param port The port of wifi connection 
	 * @param ip The identifier of wifi server
	 */
	public ConnectionCommunicationWifiClient(int port, String ip) {
		this.port = port;
		this.ip = ip;
		this.client = null;
		this.dOut = null;
		this.dIn = null;
	}
	
	public Socket getClient() {
		return this.client;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	@Override
	public void openConnection() throws IOException {
		client = new Socket(ip, port); // to connect to the port of client that corresponds to the ip address
		System.out.println("Wifi connection accepted");
		
		InputStream in = client.getInputStream(); // recover the client input stream
		OutputStream out = client.getOutputStream(); // recover the client output stream
		
		dIn = new DataInputStream(in); // initializes the input stream of the connection between the two systems with the input of client
		dOut = new DataOutputStream(out); // initializes the output stream of the connection between the two systems with the output of client
		
		infoConnection = new InfoConnection(client.getLocalAddress().getHostAddress(), ip);
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Wifi connection closed");
	}
}
