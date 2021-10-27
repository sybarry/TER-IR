package Components;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.*;


public class Motor {
	
	// Module corresponding to the engine connected to the car
	private EV3LargeRegulatedMotor oneMotor;
	

	/*
	 * Engine class manufacturer
	 * @param port: the port of the EV3 brick
	 */
	public Motor(Port port) {
		this.oneMotor = new EV3LargeRegulatedMotor(port);
	}
	
	/*
	* Turns the motor in either direction
	* @param advance: true to go forward, false to go backward
	*/
	public void run(boolean avance) {
		if(avance) {
			this.oneMotor.forward();
		}
		else {
			this.oneMotor.backward();
		}
	}
	
	
	/*
	* Allows the engine to accelerate
	* @param intensity: Speed ​​chosen for the motor
	*/
	public void speedUp(int intensite) {
		this.oneMotor.setSpeed(intensite*360);
	}
	

	/*
	* Stop the engine
	*/
	public void stop() {
		this.oneMotor.stop();
	}
	

	/*
	* Returns the motor speed
	* @return the current motor speed
	*/
	public int getSpeed(){
		return this.oneMotor.getSpeed();
	}
	

	/*
	* @return true if the motor is moving forward
	*/
	public boolean isMoving(){
		return this.oneMotor.isMoving();
	}
	
	/*
	* @return true if the engine is stopped
	*/
	public boolean isStalled(){
		return this.oneMotor.isStalled();
	}

	public EV3LargeRegulatedMotor getOneMotor() {
		return oneMotor;
	}
	
	
}
