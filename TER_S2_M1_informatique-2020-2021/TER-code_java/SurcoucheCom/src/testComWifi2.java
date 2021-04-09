
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

public class testComWifi2 {
	
	static String str = "";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String ip = "192.168.1.22"; // connexion ev3 
		int port = 1234;

		final ConnectionCommunicationWifiClient comWifi = new ConnectionCommunicationWifiClient(port, ip);
		
		comWifi.openConnection();
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		str = (String) comWifi.receiveMessage(1);
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
