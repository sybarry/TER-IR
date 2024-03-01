package pillotageBluetoothMQTT;

import lejos.robotics.RegulatedMotor;

public class Motor {
	private RegulatedMotor motor;
	private int actual_speed;
	private int previousSpeed;

	public void setMotor(RegulatedMotor motor) {
		this.motor = motor;
	}

	public int getPreviousSpeed() {
		return previousSpeed;
	}

	public void setPreviousSpeed(int previousSpeed) {
		this.previousSpeed = previousSpeed;
	}

	public Motor(RegulatedMotor _motor, int initial_speed) {
		this.motor =_motor;
		this.actual_speed = initial_speed;
		this.previousSpeed = initial_speed;
	}

	public int getActual_speed() {
		return actual_speed;
	}

	public void setActual_speed(int actual_speed) {
		this.actual_speed = actual_speed;
		motor.setSpeed(actual_speed);
	}

	public void movingBackward() {
//		setSpeed(50);
		motor.backward();
	}

	public void movingForward() {
//		setSpeed(50);
		motor.forward();
	}

	public RegulatedMotor getMotor() {
		return motor;
	}

	public void stop() {
		motor.stop();
	}

	public void rotateHalf() {
		motor.rotate(90);
	}

	public void speedUp(int value) {
		this.previousSpeed = actual_speed;
		actual_speed += value;
		motor.setSpeed(actual_speed);
	}

	public void speedDown(int value) {
		this.previousSpeed = actual_speed;
		actual_speed -= value;
		motor.setSpeed(actual_speed);
	}

}
