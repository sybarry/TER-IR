package portailEV3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EcouteWifi extends Thread {
	private String str;
	private String ip;
	private Socket sock;
	private InputStream in;
	private DataInputStream dIn = null;
	
	public void run() {
		try {
			connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      	try {
       		str = dIn.readUTF();
    		System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void connect() throws UnknownHostException, IOException {
		ip = "192.168.1.90"; //ip de la voiture qui veut rentrer
		sock = new Socket(ip, 1234);//1234 port du vehicule qui veut rentré
		System.out.println("Connected");
		
		in = sock.getInputStream();
		dIn = new DataInputStream(in);
	}

}
