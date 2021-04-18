

import java.io.IOException;

import ConnectionCommunication.ConnectionCommunicationBTServeur;
import ConnectionCommunication.ConnectionCommunicationWifiServeur;
import Exception.MessageException;
import Message.MessageBoolean;
import Message.MessageByte;
import Message.MessageDouble;
import Message.MessageFloat;
import Message.MessageInt;
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
		final String nameLocal = brick.getName();

		final ConnectionCommunicationWifiServeur comWifi = new ConnectionCommunicationWifiServeur(port);
		//final ConnectionCommunicationBTServeur comBT = new ConnectionCommunicationBTServeur(60000, NXTConnection.RAW);
		
		comWifi.openConnection();
		//comBT.openConnection();
		final float a = 2;
		final byte b = 49;
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageString(nameLocal));
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageInt(3000));
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageFloat(a));
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageByte(b));
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageBoolean(false));
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageDouble(10.5));
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
