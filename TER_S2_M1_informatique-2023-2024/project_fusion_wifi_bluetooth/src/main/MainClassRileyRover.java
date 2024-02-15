package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LocalBTDevice;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;
import pilottageMQTT.MQTTConnect;
import pilottageMQTT.Utils;

public class MainClassRileyRover {
	
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static int commande=0;

	public static void main(String[] args) throws Exception 
    {
		boolean w_true=true;
		while (w_true) {
            // Afficher le menu
            afficherMenu();

            // Attendre la sélection de l'utilisateur
            int choix = waitForSelection();

            // Exécuter le programme correspondant au choix de l'utilisateur
            switch (choix) {
                case 1:
                	mqttProgram();
                    break;
                case 2:
                	bluetoothProgram();
                    break;
                case 0:
                	w_true = false;
                	break;
            }
        }
		
    }
		
				
	
	
	 private static void afficherMenu() {
	     System.out.println("Bouton Haut MQTT");
	     System.out.println("Bouton Bas Bluetooth");
    }
	 
	 private static int waitForSelection() {
        while (true) {
            if (Button.UP.isDown()) {
                return 1;
            } else if (Button.DOWN.isDown()) {
                return 2; 
            } else if (Button.LEFT.isDown()) return 0;
        }
    }
	 
	// MQTT Connexion configuration
	private static void mqttProgram() throws Exception {
		System.out.println("Connecting using MQTT");
		final String MQTT_SERVER_IP = "192.168.43.150";
		final String clientId = "EV3_" + Utils.generateClientID();
		final String topic = "ev3/topic";

		try {
			new MQTTConnect(MQTT_SERVER_IP, clientId, topic);
		} catch (InterruptedException e) {
			Utils.print("\n\n\n" + e.getMessage());
		} catch (Exception e) {
			Utils.print("\n\n\n" + e.getMessage());
		} finally {
			Delay.msDelay(2000);
			System.exit(0);
		}
	}
	
	
	//Bluetooth with pilote android programme
	
	public static void bluetoothProgram() {	
		System.out.println("Connecting using Bluetooth");
		LocalBTDevice localBTDevice = BrickFinder.getLocal().getBluetoothDevice();

        // Récupérer l'adresse MAC de l'EV3
        String ev3MacAddress = localBTDevice.getBluetoothAddress();

        // Afficher l'adresse MAC de l'EV3
        System.out.println("Adresse MAC de l'EV3 : " + ev3MacAddress);
		
    	connect();

    	boolean stop_app = true;
 		Controller ctrl = new Controller();

    	while(stop_app)
    	{
    		try {
				commande = (int) in.readByte();
				switch(commande)
				{
    				case 1: 
				    	ctrl.movingForward();
	    				break;	
    				case 2 :
	    				ctrl.movingBackward();
	    				break;
    				case 3 : 
	    				ctrl.turnLeft(2);
	    				break;
    				case 4 : 
    					ctrl.turnRight(2);
	    				break;
    				case 5 :
    					ctrl.accelerate(50);
    					break ;
    				case 6 : 
    					ctrl.decelerated(50);
    					break ;
    				case 7 :
    					ctrl.stop();
    					break;
    				case 10 :
    					stop_app = false;
    					//ctrl.ultrasonicSensor.arret();
    					in.close();
    					out.close();
    					break;
    				default :
    					stop_app = false;
    					//ctrl.ultrasonicSensor.arret();
    					in.close();
    					out.close();
    					break;
				}
    		}catch (IOException ioe) {
    			System.out.println("IO Exception readInt");
    		}
    	}
    }
	
	
	  
	public static void connect()
	{  
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		out = BTConnect.openDataOutputStream();
		in = BTConnect.openDataInputStream();
	}


}
