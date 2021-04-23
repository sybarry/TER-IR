

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
		final String ipClient = "192.168.1.16";

		final ConnectionCommunicationWifiServeur comWifi = new ConnectionCommunicationWifiServeur(port);
	
		comWifi.openConnection();
		comWifi.acceptConnection();
		comWifi.showClient();

		final float a = 2;
		final byte b = 49;
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				System.out.println(comWifi.receiveMessage(ipClient).getMessage());
	       			} catch (IOException | MessageException e) {
	       				// TODO Auto-generated catch block
	       				e.printStackTrace();
	       			}	
    			}
    		}   
		}.start();
		
		new Thread() {
    		public void run() {
    			for(;;) {
	       			try {
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageString(nameLocal), ipClient);
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageInt(3000), ipClient);
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageFloat(a), ipClient);
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageByte(b), ipClient);
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageBoolean(false), ipClient);
	       				Button.DOWN.waitForPressAndRelease();
	       				comWifi.sendMessage(new MessageDouble(10.5), ipClient);
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
