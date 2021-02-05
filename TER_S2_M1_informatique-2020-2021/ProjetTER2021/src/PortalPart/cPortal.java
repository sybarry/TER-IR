/**
 * 
 */
package PortalPart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;
import TouchSensor.cTouchSensor;
import lejos.hardware.Bluetooth;
import lejos.hardware.port.SensorPort;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class cPortal {
	private cMotor oMotorLeft;
	private cMotor oMotorRight;
	private cTouchSensor oTouchSensor;
	private static DataOutputStream out; 
	private static DataInputStream in;
	private static BTConnection BTConnect;
	private static BTConnector BTconnector;
	
	
	public cPortal()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,10);
		this.oMotorRight  = new cMotor(2,10);
	    this.oTouchSensor = new cTouchSensor(SensorPort.S1);
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	public cTouchSensor get_oTouchSensor() { return this.oTouchSensor; }
	
	public static DataOutputStream getOut() {
		return out;
	}

	public static void setOut(DataOutputStream out) {
		cPortal.out = out;
	}

	public static DataInputStream getIn() {
		return in;
	}

	public static void setIn(DataInputStream in) {
		cPortal.in = in;
	}

	
	public void openPoral() {
		oMotorLeft.motorForward(2200);
		oMotorRight.motorForward(2200);
	}
	public void closePortal() {
		oMotorLeft.motorBackward(2200);
		oMotorRight.motorBackward(2200);
	}

	public void deletePortal() {
		oMotorLeft.motorClose();
		oMotorRight.motorClose();
		oTouchSensor.close();
	}
	
	public void addVehicle() {}
	public void deleteVehicle() {}
	
    public static boolean connectCLIENT() // Connexion Bluetooth client 
	{  
    	boolean result = false;
    	String target = "";
		System.out.println("Recherche");
	    BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    
	    try
	    {
	      // Le fichier d'entrée
	      File file = new File("BDD_Vehicle.txt");    
	      // Créer l'objet File Reader
	      FileReader fr = new FileReader(file);  
	      // Créer l'objet BufferedReader        
	      BufferedReader br = new BufferedReader(fr);     
	      while((target = br.readLine()) != null && result == false)
	      {
	    	BTConnect = (BTConnection) BTconnector.connect(target, NXTConnection.RAW);
	  		if(BTConnect != null)
	  		{
	  			setOut(BTConnect.openDataOutputStream());
	  			setIn(BTConnect.openDataInputStream());
	  			result = true;
	  		}
 
	      }
	      fr.close();    
	    }
	    catch(IOException e)
	    {
	      e.printStackTrace();
	    }
		
		return result;
	}


	

}
