

import java.io.IOException;

import ConnectionCommunication.ConnectionCommunicationWifiServeur;
import Exception.MessageException;
import Message.MessageString;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;

/*
 * pour l'ev3 D2, vehicule, serveur
 */

public class testComWifi {
	
	private static EV3 brick;
	public static final int port = 1234;
	
	public static void main(String[] args) throws IOException, InterruptedException, MessageException {
		
		brick = (EV3) BrickFinder.getLocal();
		final String nameLocal1 = brick.getName();

		final ConnectionCommunicationWifiServeur comWifi = new ConnectionCommunicationWifiServeur(port);
		
		comWifi.openConnection();
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessageGen(new MessageString(nameLocal1));
	       			} catch (IOException | MessageException e) {
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
