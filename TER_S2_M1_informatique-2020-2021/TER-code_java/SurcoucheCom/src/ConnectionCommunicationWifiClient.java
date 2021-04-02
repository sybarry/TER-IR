import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionCommunicationWifiClient implements IConnectionCommunication {

	private Socket client;
	private DataOutputStream dOut;
	private DataInputStream dIn;
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
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Connection closed");
	}

	@Override
	public void sendMessage(String message) throws IOException {
		dOut.writeUTF(message);
		dOut.flush();		
	}

	@Override
	public String receiveMessage() throws IOException {
		return dIn.readUTF();		
	}
}
