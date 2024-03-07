package pillotageBluetoothMQTT;

import lejos.robotics.RegulatedMotor;

public class Motor {
	private final RegulatedMotor motor;

	public Motor(RegulatedMotor _motor, int initial_speed) {
		this.motor =_motor;
		this.motor.setSpeed(initial_speed);
	}

	public int getActual_speed() {
		return motor.getSpeed();
	}

	public void setActual_speed(int actual_speed) {
		motor.setSpeed(actual_speed);
	}

	public void movingBackward() {
		motor.backward();
	}

	public void movingForward() {
		motor.forward();
	}

	public RegulatedMotor getMotor() {
		return motor;
	}

	public void stop() {
		motor.stop();
	}

	public void speedUp(int value) {
		int new_speed = motor.getSpeed() + value;
		motor.setSpeed(new_speed);
	}

	public void speedDown(int value) {
		int new_speed = motor.getSpeed() - value;
		motor.setSpeed(new_speed);
	}

}
