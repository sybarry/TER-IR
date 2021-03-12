package rileyRoverApp;

import composantsEV3.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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


public class VoitureControleur extends Thread{
	
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
	
	
	
	
	private static Moteur moteurDroit;
	private static Moteur moteurGauche;
	private static PresenceCapteur capteurPresence;
	private static ContactSensor capteurContact;
//	private static ColorSensor capteurCouleur;
	
	private static final int 
	//Etats de la machine
		AVANCE=1,
		RECUL=2,
		ARRET=3,
		TOURNE_DROITE=16,
		TOURNE_GAUCHE=17,
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
	public static void main(String[] args) throws IOException {
		
		//Mise en place de la connexion bluetooth
		bluetoothConnection();        
		//wifiConnection();
		
		brick = (EV3) BrickFinder.getLocal();
		nameLocal = brick.getName();
		
		//Signalement que l'application est pr�te
		appliPreteAMarcher(true);
		
		//Initialisation des diff�rents composants de l'application
		moteurDroit = new Moteur(MotorPort.C);
		moteurGauche = new Moteur(MotorPort.B);
		capteurPresence = new PresenceCapteur(SensorPort.S1);
		capteurContact = new ContactSensor(SensorPort.S2);
//		capteurCouleur = new ColorSensor(SensorPort.S3);
		
		RegulatedMotor listMotors[] = {moteurDroit.getUnMoteur()};
		moteurGauche.getUnMoteur().synchronizeWith(listMotors);
		
		//Arrêt des différents moteurs par mesure de sécurité
		moteurDroit.arret();
		moteurGauche.arret();
		
		// Création d'un thread pour écouter la télécommande.
        new Thread() {
            public void run() {
            	for(;;) {
                	try {
    					transmission = (int) donneeEntree.readByte();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
            	}

            }   
        }.start();
        
		new Thread() {
            public void run() {
               	try {
               		wifiConnection();
               		dOut.writeUTF(nameLocal);
            		dOut.flush();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}	
            }   
        }.start();
		
        
        new Thread() {
        	public void run() {
        		for(;;) {
    				try {
						donneeSortie.writeInt(Battery.getVoltageMilliVolt());
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
			//transmission = (int) donneeEntree.readByte();
			
			//Se place dans un �tat en fonction du signal re�u
			switch(transmission) {
				case AVANCE:
					avance();
					break;
				case RECUL:
					reculSecurise();
					break;
				case ARRET:
					arretMoteur();
					break;
				case VITESSE0:
					changementVitesse(0);
					break;
				case VITESSE1:
					changementVitesse(1);
					break;
				case VITESSE2:
					changementVitesse(2);
					break;
				case VITESSE3:
					changementVitesse(3);
					break;
				case VITESSE4:
					changementVitesse(4);
					break;
				case VITESSE5:
					changementVitesse(5);
					break;
				case VITESSE6:
					changementVitesse(6);
					break;
				case VITESSE7:
					changementVitesse(7);
					break;
				case VITESSE8:
					changementVitesse(8);
					break;
				case VITESSE9:
					changementVitesse(9);
					break;
				case VITESSE10:
					changementVitesse(10);
					break;
				case ARRET_APPLI:
					appliPreteAMarcher(false);
					break;
				case TOURNE_DROITE:
					tourneDroite();
					break;
				case TOURNE_GAUCHE:
					tourneGauche();
					break;
				case KLAXONNE:
					klaxonne();
					break;
				case MODE_AUTOMATIQUE:
					modeAuto();
					break;
				case MODE_MANUEL:
					arretMoteur();
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
	
	public static void modeAuto() {
		while(transmission == MODE_AUTOMATIQUE) {
			if(!capteurPresence.obstacleDetect()) {
//				vitesseLumininosite();
				avance();
			}
			else if(capteurPresence.obstacleDetect()) {
				tourneDroite();
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
	public static void avance() {
		System.out.println("AVANCE");
		moteurGauche.getUnMoteur().startSynchronization();
		moteurDroit.accelere(vitesse);
		moteurGauche.accelere(vitesse);
		moteurDroit.marche(true);
		moteurGauche.marche(true);
		moteurGauche.getUnMoteur().endSynchronization();
	}
	public static void reculSecurise() {
		if(!capteurContact.contactDetected()) {
			recul();
		}else {
			arretMoteur();
		}
	}
	/*
	 * Permet � la voiture de reculer
	 */
	public static void recul() {
		System.out.println("RECUL");
		moteurGauche.getUnMoteur().startSynchronization();
		moteurDroit.marche(false);
		moteurGauche.marche(false);
		moteurGauche.getUnMoteur().endSynchronization();
	}
	/*
	 * Arr�te les moteurs
	 */
	public static void arretMoteur() {
		System.out.println("ARRET");
		moteurGauche.getUnMoteur().startSynchronization();
		moteurDroit.arret();
		moteurGauche.arret();
		moteurGauche.getUnMoteur().endSynchronization();
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
	 * Tourne � droite
	 */
	public static void tourneDroite() {
		System.out.println("DROITE");
		moteurGauche.marche(true);
		moteurDroit.marche(false);
		moteurGauche.accelere(1);
		moteurDroit.accelere(1);
	}
	/*
	 * Tourne � gauche
	 */
	public static void tourneGauche() {
		System.out.println("GAUCHE");
		moteurDroit.marche(true);
		moteurGauche.marche(false);
		moteurDroit.accelere(1);
		moteurGauche.accelere(1);	
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
