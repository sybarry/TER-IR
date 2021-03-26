package portailEV3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;



public class Controller{

	private static int remoteControlCode = 0;
	private static boolean app_alive;
	
	private static ContactSensor sensorVehicle = new ContactSensor(SensorPort.S1);
	
	private static Door leftDoor = new Door(MotorPort.A);
	private static Door rightDoor = new Door(MotorPort.B);

	private static State stateDoor;
	private static ArrayList<State> stateList ;
	
	private static ArrayList<String> vehiculeAutorisation;
	private static String vehiculeDemande;
	
	
	private static boolean fermer = true;
	

	
	public static void main(String[] args) throws InterruptedException{


		stateList = new ArrayList<State>();
		stateDoor = State.valueOf("FERME");

		
		vehiculeAutorisation = new ArrayList<String>();
		vehiculeAutorisation.add("ev3");

		
		System.out.println("Connection BT a la telecommande");
		
		
		EcouteBT EBT = new EcouteBT();  //Connection télécommande
		EBT.start();
		
		while(EBT.BTconnect == false) {}
		
		System.out.println("Connection BT reussi");
		
		app_alive = true;
		
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
			if(sensorVehicle.contact()) {
				System.out.println("Connection Wifi au véhicule");
				EcouteWifi EWF = new EcouteWifi(); //Connection Vehicule
				EWF.start();
				while(vehiculeDemande == "" || vehiculeDemande == null) {
					vehiculeDemande = EWF.idVehicule;
				}
				

				System.out.println("Connection Wifi reussi à "+ vehiculeDemande);
				
				if(vehiculeAutorisation.contains(vehiculeDemande) 
						&& (	fermer ||
								(stateDoor == State.valueOf("FERME_PARTIELLE")) ||
								(stateDoor == State.valueOf("OUVERT_PARTIELLE"))
							)
						){
					System.out.println("Acces autorise");
					totalOpening();
					TimeUnit.SECONDS.sleep(20);
			   	    totalClosing();
					vehiculeDemande = "";
					EWF.setIdVehicule("");
					
					// Le client se déconnecte du serveur après 1 minutes
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
		
		

	}
	
	
	
	
	@SuppressWarnings("unlikely-arg-type")
	public static void initialisation() {
		
		if (fermer) {
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
	

	public static void totalOpening() {
		if (stateDoor.name().equals("FERME")) {
			System.out.println("En ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(1800);
			leftDoor.stop(true);
			rightDoor.stop(true); 
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			Delay.msDelay(1800);
			fermer = false;
			Delay.msDelay(1800);
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("Déja ouvert");
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En ouverture.");
			partialOpening();
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En ouverture.");
			partialOpening();
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
			
	}
	
	

	public static void partialOpening() {

		if (stateDoor.name().equals("FERME")) {
			System.out.println("En ouverture partielle.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuverturePartielle");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT_PARTIELLE");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("Déja ouvert totalement");
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("Ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("Ouverture totale.");
			rightDoor.opened();
			leftDoor.opened();
			stateDoor = State.valueOf("EnOuvertureTotale");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);    
			stateDoor = State.valueOf("OUVERT");
			saveState(stateDoor);
			fermer = false;
		}
		
	}
	
	
	
	public static void partialClosing() {
		
		if (stateDoor.name().equals("FERME")) {
			System.out.println("Déja fermer."); 
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("En fermeture partielle");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermeturePartielle");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME_PARTIELLE");
			saveState(stateDoor);
			fermer = false;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(900);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
		
	}
	
	
	public static void totalClosing() {
		
		if (stateDoor.name().equals("FERME")) {
			System.out.println("Déja fermé");
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
		else if(stateDoor.name().equals("OUVERT")) {
			System.out.println("En fermeture totale");
			rightDoor.closed();
			leftDoor.closed();
			stateDoor = State.valueOf("EnFermetureTotale");
		    Delay.msDelay(1800);
			leftDoor.stop(true);
			rightDoor.stop(true);   
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
		else if(stateDoor.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture partielle.");
			partialClosing();
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
		else if(stateDoor.name().equals("FERME_PARTIELLE")) {
			System.out.println("En fermeture partielle.");
			partialClosing();
			stateDoor = State.valueOf("FERME");
			saveState(stateDoor);
			fermer = true;
		}
  }
	
	
	public static void saveState(State st) {
		stateList.add(st);
	}

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
