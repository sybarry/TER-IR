package ConnectionCommunication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionCommunicationWifiServeur extends AConnectionCommunication {
	
	private ServerSocket server;
	private Socket client;
	private int port;
	
	public ConnectionCommunicationWifiServeur(int port) {
		this.port = port;
		this.server = null;
		this.client = null;
		this.dOut = null;
		this.dIn = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		server = new ServerSocket(port);
		System.out.println("Awaiting client..");
		client = server.accept();
		System.out.println("CONNECTED");
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		
		dIn = new DataInputStream(in);
		dOut = new DataOutputStream(out);
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Connection closed");
	}
}