package VehicleApp;

import Components.*;
import ConnectionCommunication.ConnectionCommunicationBTServer;
import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageInt;
import Message.MessageString;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jfree.util.WaitingImageObserver;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class VehicleController extends Thread {

	private static boolean appliReady;
	
	// private static ConnectionCommunicationBTServer BTServer;  La communication entre le véhicule et la télécommande ne fonctionne pas avec la surcouche, donc nous utilisons la communication sans surcouche
	private static BTListener BTServer;
	
	// private static MessageInt transmission;
	private static int transmission = 0;
	
	private static ConnectionCommunicationMqttClient mqttClient;
	private static IMessage<?> str = null;
	private static IMessage<?> bonus = null;
	private static IMessage<?> malus = null;

	private static Motor MotorRight;
	private static Motor MotorLeft;
	private static EV3ColorSensor colorSensor;

	private static int speed = 10;
	private static String topicWithServer = "Car2"; // a changer pour chaque vehicule 1..N
	private static String topicAll = "All";
	
	
	private static boolean finish = false;
	private static boolean bonusActivated = false;
	private static boolean go = false;
	/*
	 * Main class - launch the application
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, MessageException, MqttException {

		// Bluetooth connection setup
		
		// BTServer = new ConnectionCommunicationBTServer(30, NXTConnection.RAW);
		// BTServer.openConnection();
		// transmission = new MessageInt(0);
		BTServer = new BTListener();
		BTServer.start();
		System.out.println("Connection BT success");
		
		// MQTT connection on the server "192.168.1.9", with the port "1883"
        //mqttClient = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
        mqttClient = new ConnectionCommunicationMqttClient("192.168.43.164", 1883);
        mqttClient.openConnection();
        mqttClient.subscribe(topicAll);
        mqttClient.subscribe(topicWithServer);
		System.out.println("En ecoute sur le canal "+topicAll);


		// Signals the application to be ready
		ready(true);

		// Initialisation of the application components
		MotorRight = new Motor(MotorPort.A);
		MotorLeft = new Motor(MotorPort.B);
		colorSensor = new EV3ColorSensor(SensorPort.S3);
		

		RegulatedMotor listMotors[] = { MotorRight.getOneMotor() };
		MotorLeft.getOneMotor().synchronizeWith(listMotors);

		// Stops the different Motors due to security issues
		MotorRight.stop();
		MotorLeft.stop();
		
		mqttClient.sendMessage(new MessageString(Command.READY, topicWithServer));
		
		//***************THREAD capteur de couleur **************//
		
		new Thread() {
            public void run() {
            	for(;;) {
            		try {
						str = mqttClient.receiveMessage(topicAll, "STANDINGS");
						if(str != null) {
	        				String[] s1 = ((String) str.getMessage()).split(":");
	        				
	        				BTServer.sendMessage(s1[1]);
	        				
	        				mqttClient.removeTreatedMessage((String) str.toString(), topicAll);
	        				str = null; 
	        			}
					} catch (IOException | MessageException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
		}.start();
		
		new Thread() {
            public void run() {
            	while(!finish) {
                	try {
        				//******************FINISH A paralleliser **********/
        				if(colorSensor.getColorID() == Color.RED && finish == false) {	
        					finish = true; // pour eviter de renvoyer le message qui permet d'arreter le chrono si on a finit la course 
        					mqttClient.sendMessage(new MessageString(Command.FINISH, topicWithServer)); // le raceController devra regarder si le message est bien finish avant de mettre le temps dans la hashMap
        					BTServer.sendMessage("finish");
        				}
        				
        				//****************BONUS a paralleliser*************//
        				/*if(colorSensor.getColorID() == Color.YELLOW && bonusActivated == false) {	
        					bonusActivated=true;
        					mqttClient.sendMessage(new MessageString(Command.WANTBONUS, topicWithServer)); // le raceController devra regarder si le message est bien finish avant de mettre le temps dans la hashMap
        					
        					bonus = mqttClient.receiveMessage(topicWithServer,"Bonus") ;
        					while( bonus == null) {
        						bonus = mqttClient.receiveMessage(topicWithServer,"Bonus") ;
        					}
        					
        					String[] s1 = ((String) bonus.getMessage()).split(":"); //pour recuperer le corp du message 
        					//BTServer.sendMessage(new MessageString("Vous avez obtenu le bonus "+s1[1]));
        					
        					switch(s1[1]) {
        						case "RedShell":
        							System.out.println("BONUS redShell");
        							mqttClient.removeTreatedMessage((String) bonus.toString(), topicWithServer);
        							bonus = null; 
        							break;
        						case "GreenShell":
        							System.out.println("BONUS greenShell");
        							mqttClient.removeTreatedMessage((String) bonus.toString(), topicWithServer);
        							bonus = null; 
        							break;
        						default:
        							break;
        							
        					}
        					bonusActivated=false;
        				}*/
    					
    				} catch (IOException | MessageException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
            	}
            }   
        }.start();
        
        
        //***************THREAD malus **************//
		/*new Thread() {
            public void run() {
            	while(finish == false) {
                	try {
                		malus = mqttClient.receiveMessage(topicWithServer,"Malus") ;
    					while( malus == null) {
    						malus = mqttClient.receiveMessage(topicWithServer,"Malus");
    						Delay.msDelay(1000);
    					}
        				String[] s1 = ((String) malus.getMessage()).split(":"); 
        				//BTServer.sendMessage(new MessageString("Vous avez recu le malus "+s1[1]));
        				
        				switch(s1[1]) {
        					
        					case "RedShell":
        						System.out.println("Malus redShell");
        						go=false;
        						Delay.msDelay(1000);
        						go=true;
        						mqttClient.removeTreatedMessage((String) malus.toString(), topicWithServer);
        						malus = null; 
        						break;
        					case "GreenShell":
        						System.out.println("Malus greenShell");
        						go=false;
        						Delay.msDelay(1000);
        						go=true;
        						mqttClient.removeTreatedMessage((String) malus.toString(), topicWithServer);
    							malus = null; 
        						break;
        					default : 
        						break;
        				
        						
        				}
    				} catch (IOException | MessageException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
            	}
            }   
        }.start();*/


		// Loop running as long as the application is running
		while (appliReady) {
			
			str = mqttClient.receiveMessage(topicAll, "COUNTDOWN");
			if(str != null) {
				String[] s1 = ((String) str.getMessage()).split(":"); 
				
				System.out.println(s1[1]);
				
				BTServer.sendMessage(s1[1]);
				
				mqttClient.removeTreatedMessage((String) str.toString(), topicAll);
				str = null; 
			}
			
	        //***************Go de départ **************//
			str = mqttClient.receiveMessage(topicAll, Command.keyWordInCommand(Command.START));
			if(str != null) {
				String[] s1 = ((String) str.getMessage()).split(":"); 
				
				if(s1[1].compareTo(Command.messageInCommand(Command.START)) == 0) {
					mqttClient.removeTreatedMessage((String) str.toString(), topicAll);
					str = null; 
					go = true;
					
					System.out.println(s1[1]);
					BTServer.sendMessage(s1[1]);
				}
			}
			
			//*************** Gestion des commandes **************//
			while(go) {
				transmission = BTServer.byteRecu;
				
				//Goes into a state depending on the signal received
				
				switch (transmission) {
				case 1:
					forward();
					break;
				case 2:
					secureBackward();
					break;
				case 3:
					turnRight();
					break;
				case 4:
					turnLeft();
					break;
				case 5:
					stopMotors();
					break;
				default:
					break;
				}
			}
		}
	}

	/*
	 * Allows the car to move forward
	 */
	public static void forward() throws InterruptedException {
		System.out.println("FORWARD");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.speedUp(speed);
		MotorLeft.speedUp(speed);
		MotorRight.run(true); // false for revers motors
		MotorLeft.run(true); // false for revers motors
		MotorLeft.getOneMotor().endSynchronization();
		// TimeUnit.SECONDS.sleep(1);
	}

	public static void secureBackward() throws InterruptedException {
			backWard();
	}

	/*
	 * Allows the car to back up
	 */
	public static void backWard() throws InterruptedException {
		System.out.println("BACKWARD");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.run(false); // true for revers motors
		MotorLeft.run(false); // true for revers motors
		MotorLeft.getOneMotor().endSynchronization();
		// TimeUnit.SECONDS.sleep(1);
	}

	/*
	 * Stop the Motors
	 */
	public static void stopMotors() {
		System.out.println("STOP");
		MotorLeft.getOneMotor().startSynchronization();
		MotorRight.stop();
		MotorLeft.stop();
		MotorLeft.getOneMotor().endSynchronization();
	}

	/*
	 * App status change
	 */
	public static void ready(boolean statut) {
		System.out.println("APPLICATION READY");
		appliReady = statut;
	}

	/*
	 * Turn right
	 */
	public static void turnRight() throws InterruptedException {
		System.out.println("Right");
		MotorLeft.run(true);
		MotorRight.run(false);
		MotorLeft.speedUp(1);
		MotorRight.speedUp(1);
		// TimeUnit.SECONDS.sleep(1);
	}

	/*
	 * Turn left
	 */
	public static void turnLeft() throws InterruptedException {
		System.out.println("Left");
		MotorRight.run(true);
		MotorLeft.run(false);
		MotorRight.speedUp(1);
		MotorLeft.speedUp(1);
		// TimeUnit.SECONDS.sleep(1);
	}

}
