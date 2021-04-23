/**
  * @file Controller.java
  *
  * @brief PortailPart
  * @package portailEV3
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package portailEV3;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;



public class Controller{

	//Attributs of application
	private static int remoteControlCode = 0;
	private static boolean app_alive;
	
	//Distance sensor
	private static PresenceSensor sensorDistance = new PresenceSensor(SensorPort.S2);
	
	//Motors of door
	private static Door leftDoor = new Door(MotorPort.A);
	private static Door rightDoor = new Door(MotorPort.B);

	//State of door
	private static State stateDoor;
	private static ArrayList<State> stateList ;
	private static boolean close = true;
	
	//Attributs for vehicle
	private static ArrayList<String> vehiculeAutorisation;
	private static String vehiculeDemande;
	private static ListenWifi EWF;
	private static ListenBT EBT;
	
	
	
	/*---------------------------------------------------------------------
    |  @Method main(String[] args) 
    |
    |  @Purpose: This method is the "main" of the portal part, it is this class that must be launched on the EV3
    |
    |  @Parameters:
    |      args -- This parameter is the default parameter
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void main(String[] args) throws InterruptedException{

		//Initialisation of attributs
		stateList = new ArrayList<State>();
		stateDoor = State.valueOf("FERME");
		

		vehiculeAutorisation = new ArrayList<String>();
		vehiculeAutorisation.add("ev3");

		
		//Thread : Connexion BT for the control remote
		System.out.println("Connection BT a la telecommande");
		EBT = new ListenBT();
		EBT.start();
		while(ListenBT.BTconnect == false) {}
		System.out.println("Connection BT reussi");
		app_alive = true;
		
		//Thread : Connxion WiFi for the vehicle
		new Thread() {
			public void run() {
				for(;;) {
					if(sensorDistance.obstacleDetect()) {//sensorVehicle.contact()
						System.out.println("Connection Wifi au véhicule");
						EWF = new ListenWifi(); //Connection Vehicule
						EWF.start();
						while(vehiculeDemande == "" || vehiculeDemande == null) {
							vehiculeDemande = ListenWifi.idVehicle;
						}
						

						System.out.println("Connection Wifi reussi à "+ vehiculeDemande);
					}
					
				}
				
			}
		}.start();

			
		//Remote control management
		while(app_alive){

			remoteControlCode = EBT.byteRecu;  //Lecture de la commande via la télécommande
			
			switch(remoteControlCode){ // revoir ce switch car quand default, il ferme le portail alors qu'il ne devrait peut etre rien faire
				
				// Ouvrir la porte partiellement
				case 1:
					partialOpening();
					break;

				case 2: 
					totalOpening();
					break;	 
					
				case 3 : 
					totalClosing();
					break;
					
				 default:	 
					 break;
			}
			
			if(vehiculeAutorisation.contains(vehiculeDemande) 
					&& (	close ||
							(stateDoor == State.valueOf("FERME_PARTIELLE")) ||
							(stateDoor == State.valueOf("OUVERT_PARTIELLE"))
						)
					){
				System.out.println("Acces autorise");
				totalOpening();
				while(sensorDistance.obstacleDetect()) {}
				TimeUnit.SECONDS.sleep(10);
		   	    totalClosing();
				vehiculeDemande = "";
				EWF.setIdVehicle("");
				
				// The client disconnects from the server after 1 minutes
				/*new Thread() {
		            public void run() {
		            	try {
							TimeUnit.MINUTES.sleep(1);
							EWF.disconnect();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}           	
		            }   
		        }.start();*/
			}
				
			
		}
		
		

	}
	
	
	
	/*---------------------------------------------------------------------
    |  @Method initialisation() 
    |
    |  @Purpose: This method initializes the portal in the closed state.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void initialisation() {
		
		if (close) {
			stateDoor = State.valueOf("FERME");
		} 
		else {
			LCD.clear();
			LCD.drawString("Erreur lors de l'initialisation", 0, 5);
				Delay.msDelay(5000);
				LCD.clear();
				LCD.refresh();
		}
	
	}
	


	/*---------------------------------------------------------------------
    |  @Method totalOpening() 
    |
    |  @Purpose: This method opens the entire portal.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void totalOpening() {
		if (stateDoor.name().equals("FERME")) {
			System.out.println("En ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(1900);
			leftDoor.stop(true);
			rightDoor.stop(true); 
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			Delay.msDelay(1900);
			close = false;
			Delay.msDelay(1900);
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("Déja ouvert");
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En ouverture.");
			partialOpening();
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En ouverture.");
			partialOpening();
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
			
	}
	
	
	/*---------------------------------------------------------------------
    |  @Method partialOpening() 
    |
    |  @Purpose: This method allows the portal to be partially opened
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void partialOpening() {

		if (stateDoor.name().equals("FERME")) {
			System.out.println("En ouverture partielle.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuverturePartielle");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT_PARTIELLE");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("Déja ouvert totalement");
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("Ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("Ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			close = false;
		}
		
	}
	
	
	/*---------------------------------------------------------------------
    |  @Method partialClosing() 
    |
    |  @Purpose: This method allows the portal to be partially closed
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void partialClosing() {
		
		if (stateDoor.name().equals("FERME")) {
			System.out.println("Déja fermer."); 
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("En fermeture partielle");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermeturePartielle");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME_PARTIELLE");
			saveState(stateDoor);
			close = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(950);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
		
	}
	
	/*---------------------------------------------------------------------
    |  @Method totalClosing() 
    |
    |  @Purpose: This method closes the entire portal.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void totalClosing() {
		
		if (stateDoor.name().equals("FERME")) {
			System.out.println("Déja fermé");
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(1900);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture partielle.");
			partialClosing();
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En fermeture partielle.");
			partialClosing();
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			close = true;
		}
  }
	
	/*---------------------------------------------------------------------
    |  @Method saveState(State st)
    |
    |  @Purpose: This method saves the state
    |
    |  @Parameters: 
 	|		args -- This parameter is the default parameter
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void saveState(State st) {
		stateList.add(st);
	}

	/*---------------------------------------------------------------------
    |  @Method displayStateList()
    |
    |  @Purpose: This method display the state 
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void displayStateList() {
		for (int i =0 ; i<stateList.size();i++) {
		
			switch(stateList.get(i)){
			
			case EnOuvertureTotale:
				System.out.println("porte en Ouverture Totale");
				break;

			case EnOuverturePartielle: 
				System.out.println("porte en Ouverture Partielle");
				break;	 
				
			case EnFermetureTotale : 
				System.out.println("porte en Fermeture Totale");
				break;
				
			case EnFermeturePartielle : 
				System.out.println("porte en Fermeture Partielle");
				break;
				
			case FERME : 
				System.out.println("porte Fermée");
				break;
				
			case OUVERT : 
				System.out.println("porte Ouverte");
				break;
				
			case OUVERT_PARTIELLE : 
				System.out.println("porte Ouvert Partielle");
				break;
				
			case FERME_PARTIELLE : 
				System.out.println("porte en Fermé Partielle");
				break;
				
			case ARRET : 
				System.out.println("porte en Arrêt");
				break;
				
			case INCONNU : 
				System.out.println("Porte en Etat Inconnu");
				break;
				
			 default:
				   break;
		  }
			
		}
	}
}
