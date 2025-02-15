package VehicleApp;

import Components.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import lejos.hardware.Bluetooth;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;

public class VehicleController extends Thread {

	private static BTConnection BTLink;
	private static boolean appliReady;
	private static int transmission = 0;

	private static MqttClient client;
	private static SimpleMqttCallBack callBack;
	private static MqttMessage message = new MqttMessage();

	private static Motor MotorRight;
	private static Motor MotorLeft;
	private static EV3ColorSensor colorSensor;

	private static int speed = 10;
	
	final static BTListener EBT = new BTListener();

	/*
	 * Main class - launch the application
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, MqttException {

		// Bluetooth connection setup
		EBT.start();

		while (BTListener.BTconnect == false) {
		}

		System.out.println("Connection BT success");
		
		// MQTT connection on the server "192.168.1.9", with the port "1883"
		connectMqtt("192.168.43.164", "1883");
		callBack = new SimpleMqttCallBack();
		client.setCallback(callBack);
		client.subscribe("all");	
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
			
			if(callBack.getMsg().compareTo("GO") == 0 && callBack.getTopic().compareTo("all") == 0) {
				go = true;
			}
			
			while(go) {
				//Reading bytes sent from the application
				transmission = EBT.byteRecu;
				
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
				
				if(colorSensor.getColorID() == Color.RED) {	// Ajout	
					publishMessage("Finish", "Car:1"); // le raceController devra regarder si le message est bien finish avant de mettre le temps dans la hashMap
				}
			}
		}
	}


	/*
	* Allows listening to bluetooth devices and connecting to �
	* the application is based on the NXT box (similar to EV3) for
	* make the connection
	*/
	public static void bluetoothConnection() {
		System.out.println("En ecoute");
		BTConnector nxtCommConnector = (BTConnector) Bluetooth
				.getNXTCommConnector();
		BTLink = (BTConnection) nxtCommConnector.waitForConnection(30000,
				NXTConnection.RAW);
		DataOutputStream outputData = BTLink.openDataOutputStream();
		DataInputStream inputData = BTLink.openDataInputStream();
		if(inputData!=null && outputData != null) {
		}
	}
	
	public static void connectMqtt(String serverAddress, String port) throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		client = new MqttClient("tcp://"+serverAddress+":"+port, MqttClient.generateClientId(), persistence);
	
		client.connect();
		System.out.println("MQTT Connecte");
	}
	
	public static void publishMessage(String msg, String topic) throws MqttPersistenceException, MqttException {
		message.setPayload(msg.getBytes());
		client.publish(topic, message);
		System.out.println("Le message a ete envoye");
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
