package testTER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D4, portail, client
 */

public class mainWifi2 {
	
	static String str = "";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String ip = "192.168.1.22"; // connexion ev3 
		//String ip = "192.168.1.16"; // connexion ordinateur
		Socket client = new Socket(ip, 1234); // port ev3 et ordinateur
		System.out.println("Connected");
		
		InputStream in = client.getInputStream();
		final DataInputStream dIn = new DataInputStream(in);
		
		/*OutputStream out = client.getOutputStream();
		final DataOutputStream dOut = new DataOutputStream(out);*/
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		str = dIn.readUTF();
	            		System.out.println(str);
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
               	}            	
            }   
        }.start();
                
        /*TimeUnit.MINUTES.sleep(1);
        dOut.writeUTF("disconect");
		dOut.flush();*/
		
		//client.close();
		
		while(true) {
			
			/*if(str.contains("ev3")) {
				TimeUnit.SECONDS.sleep(10);
				str = "";
				System.out.println("disconnect");
				client.close();
			}*/
		}
	}
}
