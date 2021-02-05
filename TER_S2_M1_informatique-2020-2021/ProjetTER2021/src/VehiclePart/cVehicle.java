/**
 * 
 */
package VehiclePart;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;
import TouchSensor.cTouchSensor;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class cVehicle {
	cMotor oMotorLeft;
	cMotor oMotorRight;
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private cTouchSensor oTouchSensor;
	
	public cTouchSensor get_oTouchSensor() { return this.oTouchSensor; }
	
	public cVehicle()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,100);
		this.oMotorRight  = new cMotor(2,100);
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	
	public static DataOutputStream getOut() {
		return out;
	}

	public static void setOut(DataOutputStream out) {
		cVehicle.out = out;
	}

	public static DataInputStream getIn() {
		return in;
	}

	public static void setIn(DataInputStream in) {
		cVehicle.in = in;
	}
	
	public void vehiculeForward() {
		oMotorLeft.motorForward(10000);
		oMotorRight.motorForward(10000);
	}
	public void vehiculeBackward() {
		oMotorLeft.motorBackward(10000);
		oMotorRight.motorBackward(10000);
	}

	public void deletePortal() {
		oMotorLeft.motorClose();
		oMotorRight.motorClose();
	}
    public static boolean connectSERVEUR() // Connexion Bluetooth serveur 
	{  
    	boolean result = false;
		System.out.println("En attente");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.RAW);
		if(BTConnect != null)
		{
			setOut(BTConnect.openDataOutputStream());
			setIn(BTConnect.openDataInputStream());
			result=true;
		}
		return result;
		
	}




}
