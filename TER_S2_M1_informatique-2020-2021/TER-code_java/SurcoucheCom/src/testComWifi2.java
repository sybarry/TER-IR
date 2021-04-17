
import java.io.IOException;

import ConnectionCommunication.ConnectionCommunicationBTClient;
import ConnectionCommunication.ConnectionCommunicationWifiClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D4, portail, client
 */

public class testComWifi2 {
	
	static String str = "";
	
	public static void main(String[] args) throws IOException, InterruptedException, MessageException {
		String ip = "192.168.1.22"; // connexion ev3 
		int port = 1234;

		final ConnectionCommunicationWifiClient comWifi = new ConnectionCommunicationWifiClient(port, ip);
		//final ConnectionCommunicationBTClient comBT = new ConnectionCommunicationBTClient("ev3", NXTConnection.PACKET);
		
		comWifi.openConnection();
		//comBT.openConnection();
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		IMessage<?> str = comWifi.receiveMessage();  //prototype 3
	               		System.out.println(str.getMessage()); 
	    			} catch (IOException | MessageException e) {
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
