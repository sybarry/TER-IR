/**
 * 
 */
package PortalPart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import CommunicationBT.CommunicationBT;
/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;
import TouchSensor.cTouchSensor;
import lejos.hardware.port.SensorPort;

public class cPortal {
	private cMotor oMotorLeft;
	private cMotor oMotorRight;
	private cTouchSensor oTouchSensor;
	private CommunicationBT bt;
	
	public cPortal()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,10);
		this.oMotorRight  = new cMotor(2,10);
	    this.oTouchSensor = new cTouchSensor(SensorPort.S1);
	    this.bt 		  = new CommunicationBT();
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	public cTouchSensor get_oTouchSensor() { return this.oTouchSensor; }
	

	
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

	
    public boolean activationBT() // Connexion Bluetooth client 
	{  
    	return bt.ConnexionServeur();
    	
	}
    public boolean autoriserAcces() throws IOException
    {
    	String target;
    	boolean result =false;
    	 try
 	    {
 	      // Le fichier d'entre
 	      File file = new File("BDD_Vehicle.txt");    
 	      // Crer l'objet File Reader
 	      FileReader fr = new FileReader(file);  
 	      // Crer l'objet BufferedReader        
 	      BufferedReader br = new BufferedReader(fr);   
 	      String vehicle = bt.ReceiveMsg();
 	      
 	      while((target = br.readLine()) != null && result == false)
 	      {
 	  		if(vehicle == target)
 	  		{
 	  			result = true;
 	  		}
  
 	      }
 	      fr.close();    
 	    }
 	    catch(IOException e)
 	    {
 	      e.printStackTrace();
 	    }
    	 if(result)
    	 {
    		 bt.SendMsg("OK");
    	 }
    	 else
    	 {
    		 bt.SendMsg("NA");
    	 }
    	 return result;

    }
    

	

}
