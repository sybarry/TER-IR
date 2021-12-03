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

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;

public class VehicleController extends Thread {

	private static boolean appliReady;
	private static ConnectionCommunicationBTServer BTServer;
	private static MessageInt transmission;
	
	private static ConnectionCommunicationMqttClient mqttClient;
	private static IMessage<?> str = null;

	private static Motor MotorRight;
	private static Motor MotorLeft;
	private static EV3ColorSensor colorSensor;

	private static int speed = 10;
	private static String topicWithServer = "Car1";
	private static String topicAll = "All";
	
	/*
	 * Main class - launch the application
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, MessageException, MqttException {

		// Bluetooth connection setup
		BTServer = new ConnectionCommunicationBTServer(30, NXTConnection.RAW);
		BTServer.openConnection();
		transmission = new MessageInt(0);
		
		boolean finish = false;
		
		new Thread() { // renvoie une erreur car la telecommande n'utilise pas la surcouche donc envoie pas de message correct
            public void run() {
            	for(;;) {
                	try {
    					transmission = (MessageInt) BTServer.receiveMessage();
    				} catch (IOException | MessageException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
            	}
            }   
        }.start();
		
		// MQTT connection on the server "192.168.1.9", with the port "1883"
        mqttClient = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
        mqttClient.openConnection();
        mqttClient.subscribe(topicAll);
		System.out.println("En ecoute sur le canal all");
		boolean go = false;

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


		// Loop running as long as the application is running
		while (appliReady) {
			
			str = mqttClient.receiveMessage(topicAll, Command.keyWordInCommand(Command.START));
			if(str != null) {
				String[] s1 = ((String) str.getMessage()).split(":"); 
				
				if(s1[1].compareTo(Command.messageInCommand(Command.START)) == 0) {
					go = true;
					mqttClient.removeTreatedMessage((String) str.toString(), topicAll);
					str = null; 
				}
			}
			
			while(go) {
				
				//Goes into a state depending on the signal received
				switch (transmission.getMessage()) {
	
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
				
				if(colorSensor.getColorID() == Color.RED && finish == false) {	
					mqttClient.sendMessage(new MessageString(Command.FINISH, topicWithServer)); // le raceController devra regarder si le message est bien finish avant de mettre le temps dans la hashMap
					finish = true; // pour eviter de renvoyer le message qui permet d'arreter le chrono si on a finit la course 
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
		MotorRight.run(true);
		MotorLeft.run(true);
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
		MotorRight.run(false);
		MotorLeft.run(false);
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
