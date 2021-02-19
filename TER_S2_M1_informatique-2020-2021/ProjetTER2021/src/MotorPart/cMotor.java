package MotorPart;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */

import Exception.OutOfMotorException;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.*;
import lejos.utility.Delay;

public class cMotor {
	UnregulatedMotor oMotor;
	int nPower;
	int nNumber;

	public cMotor(int nNumber, int nPower) throws OutOfMotorException {
		if(nNumber == 1) {
			this.oMotor = new UnregulatedMotor(MotorPort.A);
			this.nPower = nPower;
			this.oMotor.setPower(this.nPower);
			this.nNumber = nNumber;
			this.oMotor.stop();
		}else {
			if(nNumber == 2) {
				this.oMotor = new UnregulatedMotor(MotorPort.B);	
				this.nPower = nPower;
				this.oMotor.setPower(this.nPower);
				this.oMotor.stop();
				this.nNumber = nNumber;
			}else {
				throw new OutOfMotorException();
			}
		}
	}
	
	public UnregulatedMotor get_oMotor() { return this.oMotor;}
	public int get_nPower() { return this.nPower;}
	public int get_nNumber() { return this.nNumber;}
	
	public void set_nPower(int nPower) { this.nPower = nPower;}
	
	public void motorStop() { oMotor.stop();};
	public void motorForward(int nDelay) {
		oMotor.forward();
		Delay.msDelay(nDelay);
		
	};
	public void motorBackward(int nDelay) {
		oMotor.backward();
		Delay.msDelay(nDelay);
	};
	public void motorClose() {
		oMotor.close();
	};
	
}


