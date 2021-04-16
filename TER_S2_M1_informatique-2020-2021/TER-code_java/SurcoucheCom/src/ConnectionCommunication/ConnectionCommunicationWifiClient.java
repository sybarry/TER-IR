package ConnectionCommunication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Divers.InfoConnection;

public class ConnectionCommunicationWifiClient extends AConnectionCommunication {

	private Socket client;
	private int port;
	private String ip;
	
	public ConnectionCommunicationWifiClient(int port, String ip) {
		this.port = port;
		this.ip = ip;
		this.client = null;
		this.dOut = null;
		this.dIn = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		client = new Socket(ip, port); 
		System.out.println("Connected");
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		
		dIn = new DataInputStream(in);
		dOut = new DataOutputStream(out);
		
		infoConnection = new InfoConnection(client.getLocalAddress().getHostAddress(), ip);
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Connection closed");
	}
}
