package MainComm;


import java.io.IOException;

import Exception.MessageException;
import Message.MessageString;
import Overlay.OverlayBTServer;
import Overlay.OverlayWifiServer;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D2, vehicule, serveur
 */

public class testComWifi {
	
	private static EV3 brick;
	public static final int port = 1234;
	
	public static void main(String[] args) throws IOException, InterruptedException{
		
		brick = (EV3) BrickFinder.getLocal();
		final String nameLocal1 = brick.getName();

		final OverlayWifiServer comWifi = new OverlayWifiServer(port);
		//final ConnectionCommunicationBTServeur comBT = new ConnectionCommunicationBTServeur(60000, NXTConnection.RAW);
		
		comWifi.openConnection();
		//comBT.openConnection();
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageString(nameLocal1));
	       			} catch (IOException  e) {
	       				// TODO Auto-generated catch block
	       				e.printStackTrace();
	       			} catch (MessageException e) {
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
