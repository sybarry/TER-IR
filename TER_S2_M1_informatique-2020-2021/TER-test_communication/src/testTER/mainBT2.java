package testTER;

/*
 * pour l'ev3 D2
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class mainBT2 {
	private static DataOutputStream donneeSortie; 
	private static DataInputStream donneeEntree;
	private static BTConnection BTLink;
	private static ArrayList<String> vehiculeAutorise;
	private static String vehicule = "null";
	
	public static void main(String[] args) throws IOException {
		System.out.println("En ecoute");
	    BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    BTLink = (BTConnection) nxtCommConnector.waitForConnection(600000, NXTConnection.PACKET);
	    
		//donneeSortie = BTLink.openDataOutputStream();
		//donneeEntree = BTLink.openDataInputStream();
		    
	    if (BTLink != null) {
	    	
	    	donneeSortie = BTLink.openDataOutputStream();
			donneeEntree = BTLink.openDataInputStream();
	    	
	    	System.out.println("Connexion effectue");	 
	    	
		    vehicule = donneeEntree.readUTF();

		    /*new Thread() {
		        public void run() {
		           	try {
		           		vehicule = donneeEntree.readUTF();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
		        }   
		    }.start();*/
		    
		    System.out.println(vehicule);
		    
		    while(true) {}
		    
		    /*if(vehiculeAutorise.contains(vehicule)) { // mettre le if dans le while enlever le while autrement erreur ou 
		    	//ouverture du portail
		    }else {
		    	System.out.println("Le vehicule "+vehicule+" n'est pas autorisé à rentrer");
		    }*/
	    }else {System.out.println("La connexion n'a pas aboutie");while(true) {}}
	}
}
