/**
 * 
 */
package PortalPart;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;
import lejos.hardware.port.SensorPort;

public class cPortal {
	cMotor oMotorLeft;
	cMotor oMotorRight;
	cTouchSensor oTouchSensor;
	cWifiPortal oWifiPortal;
	
	
	public cPortal()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,10);
		this.oMotorRight  = new cMotor(2,10);
	    this.oTouchSensor = new cTouchSensor(SensorPort.S1);
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	public cTouchSensor get_oTouchSensor() { return this.oTouchSensor; }
	public cWifiPortal get_oWifiPortal() { return this.oWifiPortal; }
	
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

	

}
