package rileyRoverApp;

import componentsEV3.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.MotorPort;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.internal.ev3.EV3Battery;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Battery;


public class VehicleController extends Thread{
	
	private static DataOutputStream donneeSortie; 
	private static DataInputStream donneeEntree;
	private static BTConnection BTLink;
	private static boolean appliReady;
	private static int transmission=0;
	//private Battery battery;
	//Valeur max 8400 min 6300
	
	private static final int port = 1234;
	private static EV3 brick;
	private static ServerSocket server;
	private static Socket client ;
	private static OutputStream out ;
	private static DataOutputStream dOut;
	private static String nameLocal = "";
	
	
	
	
	private static Motor MotorRight;
	private static Motor MotorLeft;
	private static MotionDetector capteurPresence;
	private static ContactSensor capteurContact;
//	private static ColorSensor capteurCouleur;
	
	private static final int 
	//Etats de la machine
		AVANCE=1,
		RECUL=2,
		ARRET=3,
		TOURNE_RightE=16,
		TOURNE_Left=17,
		KLAXONNE=18,
		MODE_AUTOMATIQUE=19,
		MODE_MANUEL=20,
	//Signal de l'arr�t de l'application
		ARRET_APPLI=15,
	//Diff�rentes vitesses possibles pour la voiture
		VITESSE0=4,
		VITESSE1=5,
		VITESSE2=6,
		VITESSE3=7,
		VITESSE4=8,
		VITESSE5=9,
		VITESSE6=10,
		VITESSE7=11,
		VITESSE8=12,
		VITESSE9=13,
		VITESSE10=14;
																		

	private static int vitesse=10;
	private static boolean klaxon = false;
	
	
	/*
	 * Classe main, lanc�e au d�marrage de l'application
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//Mise en place de la connexion bluetooth
		final BTListener EBT = new BTListener();  //Connection t�l�commande
		EBT.start();      
		
		while(EBT.BTconnect == false) {}
		
		System.out.println("Connection BT reussi");
		
		brick = (EV3) BrickFinder.getLocal();
		nameLocal = brick.getName();
		
		//Signalement que l'application est pr�te
		appliPreteAMarcher(true);
		
		//Initialisation des diff�rents composants de l'application
		MotorRight = new Motor(MotorPort.C);
		MotorLeft = new Motor(MotorPort.B);
		capteurPresence = new MotionDetector(SensorPort.S1);
		capteurContact = new ContactSensor(SensorPort.S2);
//		capteurCouleur = new ColorSensor(SensorPort.S3);
		
		RegulatedMotor listMotors[] = {MotorRight.getOneMotor()};
		MotorLeft.getOneMotor().synchronizeWith(listMotors);
		
		//Arrêt des différents Motors par mesure de sécurité
		MotorRight.arret();
		MotorLeft.arret();
		

        
        new Thread() {
    		public void run() {
    			try {
	    			wifiConnection();
    			} catch (IOException e) {
       				// TODO Auto-generated catch block
       				e.printStackTrace();
    			}
	    		for(;;) {
		       		try {
		       			Button.DOWN.waitForPressAndRelease();
		       			dOut.writeUTF(nameLocal);
		       			dOut.flush();
		       		} catch (IOException e) {
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
						EBT.dataOut.writeInt(Battery.getVoltageMilliVolt());
//    					System.out.println(Battery.getVoltageMilliVolt());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        }.start();
        
        
        
        

		//Boucle fonctionnant tant que l'application est en marche
		while(appliReady) {
			//Lecture des bytes envoy�s depuis l'application
			
			transmission = EBT.byteRecu;
			
			//Se place dans un �tat en fonction du signal re�u
			switch(transmission) {
			
				case 1:
					avance();
					break;
				case 2:
					reculSecurise();
					break;
				case 3:
					arretMotor();
					break;
				case 4:
					changementVitesse(0);
					break;
				case 5:
					changementVitesse(1);
					break;
				case 6:
					changementVitesse(2);
					break;
				case 7:
					changementVitesse(3);
					break;
				case 8:
					changementVitesse(4);
					break;
				case 9:
					changementVitesse(5);
					break;
				case 10:
					changementVitesse(6);
					break;
				case 11:
					changementVitesse(7);
					break;
				case 12:
					changementVitesse(8);
					break;
				case 13:
					changementVitesse(9);
					break;
				case 14:
					changementVitesse(10);
					break;
				case 15:
					appliPreteAMarcher(false);
					break;
				case 16:
					tourneRighte();
					break;
				case 17:
					tourneLeft();
					break;
				case 18:
					klaxonne();
					break;
				case 19:
					modeAuto();
					break;
				case 20:
					arretMotor();
					break;
				default:
					break;
			}
		}
	}
	
	
	
	
	
	
	
	
	/*
	 * Permet l'�coute des p�riph�riques bluetooth et la connexion � l'application
	 * Se base sur le boitier NXT (similaire � EV3) pour effectuer la connexion
	 */
	public static void bluetoothConnection(){  
	    System.out.println("En ecoute");
	    BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    BTLink = (BTConnection) nxtCommConnector.waitForConnection(30000, NXTConnection.RAW);
	    donneeSortie = BTLink.openDataOutputStream();
	    donneeEntree = BTLink.openDataInputStream();
	}
	public static void wifiConnection() throws IOException{  
		server = new ServerSocket(port);
		System.out.println("Awaiting client..");
		client = server.accept();
		System.out.println("CONNECTED");
		out = client.getOutputStream();
		dOut = new DataOutputStream(out);
	}
	
	public static void modeAuto() throws InterruptedException {
		while(transmission == MODE_AUTOMATIQUE) {
			if(!capteurPresence.obstacleDetect()) {
//				vitesseLumininosite();
				avance();
			}
			else if(capteurPresence.obstacleDetect()) {
				tourneRighte();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/*
	 * Permet � la voiture d'avancer
	 */
	public static void avance() throws InterruptedException {
		System.out.println("AVANCE");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.accelere(vitesse);
		MotorLeft.accelere(vitesse);
		MotorRight.marche(true);
		MotorLeft.marche(true);
		MotorLeft.getOneMotor().endSynchronization();
		//TimeUnit.SECONDS.sleep(1);
	}
	public static void reculSecurise() throws InterruptedException {
		if(!capteurContact.contactDetected()) {
			recul();
		}else {
			arretMotor();
		}
	}
	/*
	 * Permet � la voiture de reculer
	 */
	public static void recul() throws InterruptedException {
		System.out.println("RECUL");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.marche(false);
		MotorLeft.marche(false);
		MotorLeft.getOneMotor().endSynchronization();
		//TimeUnit.SECONDS.sleep(1);
	}
	/*
	 * Arr�te les Motors
	 */
	public static void arretMotor() {
		System.out.println("ARRET");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.arret();
		MotorLeft.arret();
		MotorLeft.getOneMotor().endSynchronization();
	}
	/*
	 * Change la vitesse de la voiture
	 */
	public static void changementVitesse(int nouvelleVitesse) {
		vitesse=nouvelleVitesse;
		System.out.println("VITESSE : "+vitesse);
	}
	/*
	 * Changement statut de l'appli
	 */
	public static void appliPreteAMarcher(boolean statut) {
		System.out.println("APPLI EN FONCTION");
		appliReady=statut;
	}
	/*
	 * Tourne � Righte
	 */
	public static void tourneRighte() throws InterruptedException {
		System.out.println("RightE");
		MotorLeft.marche(true);
		MotorRight.marche(false);
		MotorLeft.accelere(1);
		MotorRight.accelere(1);
		//TimeUnit.SECONDS.sleep(1);
	}
	/*
	 * Tourne � Left
	 */
	public static void tourneLeft() throws InterruptedException {
		System.out.println("Left");
		MotorRight.marche(true);
		MotorLeft.marche(false);
		MotorRight.accelere(1);
		MotorLeft.accelere(1);
		//TimeUnit.SECONDS.sleep(1);
	}
	/*
	 * Permet � la voiture de klaxonner
	 */
	public static void klaxonne() {
		System.out.println("KLAXONNE");
		Sound.buzz();
	}
	
	/*public static void vitesseLumininosite() {
		
		float luminosity = capteurCouleur.colorDetection();

		if (luminosity < 0.03) {

			changementVitesse(1);

		} else if (luminosity < 0.05) {

			changementVitesse(2);

		} else if (luminosity < 0.1) {

			changementVitesse(3);

		} else if (luminosity < 0.2) {

			changementVitesse(4);

		} else {

			changementVitesse(5);

		}
	}*/
}
