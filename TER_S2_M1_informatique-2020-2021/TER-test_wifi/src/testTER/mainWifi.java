package testTER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Wifi;
import lejos.hardware.ev3.EV3;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D2, vehicule, serveur
 */

public class mainWifi {
	
	private static EV3 brick;
	public static final int port = 1234;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		brick = (EV3) BrickFinder.getLocal();
		final String nameLocal1 = brick.getName();
		
		ServerSocket server = new ServerSocket(port);
		System.out.println("Awaiting client..");
		Socket client = server.accept();
		System.out.println("CONNECTED");
		
		OutputStream out = client.getOutputStream();
		final DataOutputStream dOut = new DataOutputStream(out);
		
		/*InputStream in = client.getInputStream();
		final DataInputStream dIn = new DataInputStream(in);*/
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				Button.DOWN.waitForPressAndRelease();
	       				dOut.writeUTF(nameLocal1);
	       				dOut.flush();
	       			} catch (IOException e) {
	       				// TODO Auto-generated catch block
	       				e.printStackTrace();
	       			}	
    			}
    		}   
		}.start();
		
		/*String str = dIn.readUTF();
		if(str == "disconect") {
			client.close();
		}*/
		
		while(true) {
		}		
	}
}
