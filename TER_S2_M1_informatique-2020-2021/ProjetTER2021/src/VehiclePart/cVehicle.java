/**
 * 
 */
package VehiclePart;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import MotorPart.cMotor;

public class cVehicle {
	cMotor oMotorLeft;
	cMotor oMotorRight;
	cWifiVehicle oWifiVehicle;
	
	public cVehicle()throws Exception
	{
		this.oMotorLeft   = new cMotor(1,100);
		this.oMotorRight  = new cMotor(2,100);
	}
	
	public cMotor get_oMotorLeft() { return this.oMotorLeft; }
	public cMotor get_oMotorRight() { return this.oMotorRight; }
	public cWifiVehicle get_oWifiVehicle() { return this.oWifiVehicle; }
	
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

}
