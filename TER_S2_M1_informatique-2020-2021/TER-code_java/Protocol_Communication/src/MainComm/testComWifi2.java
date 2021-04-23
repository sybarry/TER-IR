package MainComm;

import java.io.IOException;

import Exception.MessageException;
import Message.MessageString;
import Overlay.OverlayBTClient;
import Overlay.OverlayWifiClient;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D4, portail, client
 */

public class testComWifi2 {
	
	static String str = "";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String ip = "192.168.43.144"; // connexion ev3 
		int port = 1234;

		final OverlayWifiClient comWifi = new OverlayWifiClient(port, ip);
		//final ConnectionCommunicationBTClient comBT = new ConnectionCommunicationBTClient("ev3", NXTConnection.PACKET);
		
		comWifi.openConnection();
		//comBT.openConnection();
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		MessageString str = (MessageString) comWifi.receiveMessage();
	               		//IMessage2<?> str = comWifi.receiveMessageGen();  prototype 3
	               		System.out.println(str.getMessage()); 
	               		
	               		/*str = (String) comWifi.receiveMessage(new MessageString()); prototype 2
	            		System.out.println(str);*/
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			} catch (MessageException e) {
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
