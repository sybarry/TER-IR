package testTER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D4, portail, client
 */

public class mainWifi2 {
	
	public static void main(String[] args) throws IOException {
		
		String ip = "192.168.1.22"; // connexion ev3 
		//String ip = "192.168.1.16"; // connexion ordinateur
		Socket sock = new Socket(ip, 1234); // port ev3 et ordinateur
		System.out.println("Connected");
		
		InputStream in = sock.getInputStream();
		final DataInputStream dIn = new DataInputStream(in);
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		String str = dIn.readUTF();
	            		System.out.println(str);
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
               	}            	
            }   
        }.start();
        
		//sock.close();
		
		while(true) {}
	}

}
