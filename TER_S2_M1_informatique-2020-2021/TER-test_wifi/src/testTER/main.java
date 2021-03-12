package testTER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

/*
 * pour l'ev3 D4
 */

public class main {
	
	private static EV3 brick;
	private static DataOutputStream donneeSortie; 
	private static DataInputStream donneeEntree;
	
	public static void main(String[] args) throws IOException {
		brick = (EV3) BrickFinder.getLocal();
		
		System.out.println("Connexion en cours");
		
		BTConnector bt = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnection btc = (BTConnection) bt.connect("EV3", NXTConnection.PACKET);
		
		//donneeSortie = btc.openDataOutputStream();
		//donneeEntree = btc.openDataInputStream();
		
	    if (btc != null) {
	    	
	    	donneeSortie = btc.openDataOutputStream();
			donneeEntree = btc.openDataInputStream();
	    	
	    	System.out.println("Connexion accepte");
		
			final String nameLocal1 = brick.getName();

			donneeSortie.writeUTF(nameLocal1); 
			donneeSortie.flush();

			/*new Thread() {
	            public void run() {
	               	try {
	               		donneeSortie.writeUTF(nameLocal1);
	               		donneeSortie.flush();
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}	
	            }   
	        }.start();*/
			
			while(true) {}
	    } else {System.out.println("La connexion n'a pas aboutie");while(true) {}}
	}
}
