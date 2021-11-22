package ConnectionCommunication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Divers.InfoConnection;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class ConnectionCommunicationWifiServer extends AConnectionCommunication {
	
	private ServerSocket server;
	private Socket client;
	private int port; // the port of wifi connection
	
	/*
	 * Create an instance for a Wifi server
	 * 
	 * @param port The port of wifi connection
	 */
	public ConnectionCommunicationWifiServer(int port) {
		this.port = port;
		this.server = null;
		this.client = null;
		this.dOut = null;
		this.dIn = null;
	}
	
	public ServerSocket getServer() {
		return this.server;
	}
	
	public Socket getClient() {
		return this.client;
	}
	
	public int getPort() {
		return this.port;
	}
	
	@Override
	public void openConnection() throws IOException {
		server = new ServerSocket(port);
		System.out.println("Awaiting wifi client..");
		client = server.accept(); // To accept the client that wants to connect to the server
		System.out.println("Wifi connected");

		InputStream in = client.getInputStream(); // recover the client input stream
		OutputStream out = client.getOutputStream(); // recover the client output stream
		
		dIn = new DataInputStream(in); // initializes the input stream of the connection between the two systems with the input of client
		dOut = new DataOutputStream(out); // initializes the output stream of the connection between the two systems with the output of client
		
		//System.out.println("sender : "+server.getInetAddress().getHostAddress()); // renvoie 0.0.0.0 au lieu de 192.168.1.22 (je ne vois pas de solution)
		// comprendre pq ca renoie 0.0.0.0, parce que c'est pas une bonne pratique de passer par le client pour avoir l'adresse sur server
		// alors que this est le serveur
		
		infoConnection = new InfoConnection(client.getLocalAddress().getHostAddress(), client.getInetAddress().getHostAddress());
	}

	@Override
	public void closeConnection() throws IOException {
		server.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Wifi connection closed");
	}
}