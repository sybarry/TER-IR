package VehicleApp;

import Components.*;

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
import lejos.robotics.RegulatedMotor;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;

public class VehicleController extends Thread {

	private static BTConnection BTLink;
	private static boolean appliReady;
	private static int transmission = 0;
	// private Battery battery;
	// Valeur max 8400 min 6300

	private static final int port = 1234;
	private static EV3 brick;
	private static ServerSocket server;
	private static Socket client;
	private static OutputStream out;
	private static DataOutputStream dOut;
	private static String nameLocal = "";

	private static Motor MotorRight;
	private static Motor MotorLeft;
	private static MotionDetector motionSensor;
	private static ContactSensor contactSensor;

	private static int speed = 10;
	
	final static BTListener EBT = new BTListener();
	private static final int MODE_AUTOMATIC = 19;

	/*
	 * Main class - launch the application
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		// bluetooth connection setup
		EBT.start();

		while (BTListener.BTconnect == false) {
		}

		System.out.println("Connection BT success");

		brick = (EV3) BrickFinder.getLocal();
		nameLocal = brick.getName();

		// Signals the application to be ready
		ready(true);

		// Initialisation of the application components
		MotorRight = new Motor(MotorPort.C);
		MotorLeft = new Motor(MotorPort.B);
		motionSensor = new MotionDetector(SensorPort.S1);
		contactSensor = new ContactSensor(SensorPort.S2);
		// colorSensor = new ColorSensor(SensorPort.S3);

		RegulatedMotor listMotors[] = { MotorRight.getOneMotor() };
		MotorLeft.getOneMotor().synchronizeWith(listMotors);

		// Stops the different Motors due to security issues
		MotorRight.stop();
		MotorLeft.stop();

		new Thread() {
			public void run() {
				try {
					wifiConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (;;) {
					try {
						Button.DOWN.waitForPressAndRelease();
						dOut.writeUTF(nameLocal);
						dOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();


		// Loop running as long as the application is running
		while (appliReady) {
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
				stopMotors();
				break;
			case 4:
				changeSPEED(0);
				break;
			case 5:
				changeSPEED(1);
				break;
			case 6:
				changeSPEED(2);
				break;
			case 7:
				changeSPEED(3);
				break;
			case 8:
				changeSPEED(4);
				break;
			case 9:
				changeSPEED(5);
				break;
			case 10:
				changeSPEED(6);
				break;
			case 11:
				changeSPEED(7);
				break;
			case 12:
				changeSPEED(8);
				break;
			case 13:
				changeSPEED(9);
				break;
			case 14:
				changeSPEED(10);
				break;
			case 15:
				ready(false);
				break;
			case 16:
				turnRight();
				break;
			case 17:
				turnLeft();
				break;
			case 18:
				honks();
				break;
			case 19:
				modeAuto();
				break;
			case 20:
				stopMotors();
				break;
			default:
				break;
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

	public static void wifiConnection() throws IOException {
		server = new ServerSocket(port);
		System.out.println("Awaiting client..");
		client = server.accept();
		System.out.println("CONNECTED");
		out = client.getOutputStream();
		dOut = new DataOutputStream(out);
	}

	public static void modeAuto() throws InterruptedException {
		while (transmission == MODE_AUTOMATIC) {
			if (!motionSensor.obstacleDetect()) {
				// SPEEDLumininosite();
				forward();
			} else if (motionSensor.obstacleDetect()) {
				turnRight();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
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
		if (!contactSensor.contactDetected()) {
			backWard();
		} else {
			stopMotors();
		}
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
	 * Change the car's SPEED
	 */
	public static void changeSPEED(int newSPEED) {
		speed = newSPEED;
		System.out.println("SPEED : " + speed);
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

	/*
	 * Allows the car to honk
	 */
	public static void honks() {
		System.out.println("HONKS");
		Sound.buzz();
	}

}
