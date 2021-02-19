/**
 * 
 */
package VehiclePart;

import java.io.IOException;

import CommunicationBT.CommunicationBT;
/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;
import TouchSensor.cTouchSensor;
import lejos.hardware.port.SensorPort;


public class cVehicle {
	cMotor oMotorLeft;
	cMotor oMotorRight;
	private cTouchSensor oTouchSensor;
	CommunicationBT bt;
	
	public cTouchSensor get_oTouchSensor() { return this.oTouchSensor; }
	
	public cVehicle()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,100);
		this.oMotorRight  = new cMotor(2,100);
		this.oTouchSensor = new cTouchSensor(SensorPort.S1);
		this.bt = new CommunicationBT();
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	

	
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
    public boolean  activationBT(String nameServeur) // Connexion Bluetooth serveur 
	{  
    	return bt.ConnexionClient(nameServeur);
    
	}
    
    public boolean demandeAcces() throws IOException{
    	boolean result = false;
    	bt.SendMsg(bt.getBrick().getName());
    	String reponse = bt.ReceiveMsg();
    	if(reponse == "OK") {
    		result = true;
    	}
    	return result;
    	
    }
    
    




}
