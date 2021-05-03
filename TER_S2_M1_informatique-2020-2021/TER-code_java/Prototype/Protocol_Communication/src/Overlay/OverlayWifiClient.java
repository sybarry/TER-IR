package Overlay;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class OverlayWifiClient extends AOverlay {

	private Socket client;
	private int port;
	private String ip;
	
	public OverlayWifiClient(int port, String ip) {
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
		
		addressSender = client.getLocalAddress().getHostAddress();
		addressReceiver = ip;
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		System.out.println("Connection closed");
	}
}
