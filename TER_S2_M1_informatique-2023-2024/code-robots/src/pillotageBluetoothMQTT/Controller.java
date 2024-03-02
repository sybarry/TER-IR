package pillotageBluetoothMQTT;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Controller {
	State actualState = State.STOPPED;
	private Motor leftMotor;
	private Motor rightMotor;
	private int ratioLeft = 50;
	private int ratioRight = 50;
	private int initial_speed = 100;

	public Controller()
	{
		this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.C), initial_speed);
		this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.B), initial_speed);

		RegulatedMotor[] T = {this.rightMotor.getMotor()};
		leftMotor.getMotor().synchronizeWith(T);
		leftMotor.setActual_speed(initial_speed);
		rightMotor.setActual_speed(initial_speed);
	}

	public void movingForward () {
		if(actualState == State.STOPPED) {
			leftMotor.getMotor().startSynchronization();
			leftMotor.movingForward();
			rightMotor.movingForward();
			actualState = State.FORWARD;
			leftMotor.getMotor().endSynchronization();
		} else if(actualState == State.TURNING_LEFT) {
			leftMotor.getMotor().startSynchronization();
			leftMotor.setActual_speed(initial_speed);
			rightMotor.setActual_speed(initial_speed);
			actualState = State.FORWARD;
			leftMotor.getMotor().endSynchronization();
		} else if (actualState == State.TURNING_RIGHT) {
			leftMotor.getMotor().startSynchronization();
			leftMotor.setActual_speed(initial_speed);
			rightMotor.setActual_speed(initial_speed);
			actualState = State.FORWARD;
			leftMotor.getMotor().endSynchronization();
		} else System.out.println("Must stop first");
	}

	public void movingBackward() {
		if(actualState == State.STOPPED) {
			leftMotor.getMotor().startSynchronization();
			leftMotor.movingBackward();
			rightMotor.movingBackward();
			actualState = State.BACKWARD;
			leftMotor.getMotor().endSynchronization();
		} else System.out.println("Must stop first");
	}

	public void stop() {
		leftMotor.getMotor().startSynchronization();
		leftMotor.stop();
		rightMotor.stop();
		actualState = State.STOPPED;
		leftMotor.getMotor().endSynchronization();
	}

	public void turnLeft() {
		if (actualState == State.FORWARD){
			actualState = State.TURNING_LEFT;
			leftMotor.setActual_speed(leftMotor.getPreviousSpeed());
			rightMotor.setActual_speed(leftMotor.getPreviousSpeed());

			leftMotor.getMotor().startSynchronization();
			leftMotor.setActual_speed(leftMotor.getActual_speed());
			rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
			leftMotor.getMotor().endSynchronization();
		} else if (actualState == State.TURNING_LEFT) {
			ratioLeft += 50;
			rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
		} else if (actualState == State.TURNING_RIGHT) {
			ratioRight -= 50;
			leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
		}
	}

	public void turnRight() {
		if (actualState == State.FORWARD){
			actualState = State.TURNING_RIGHT;
			rightMotor.setActual_speed(rightMotor.getPreviousSpeed());
			leftMotor.setActual_speed(rightMotor.getPreviousSpeed());

			leftMotor.getMotor().startSynchronization();
			leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
			rightMotor.setActual_speed(rightMotor.getActual_speed());
			leftMotor.getMotor().endSynchronization();
		}
		else if(actualState == State.TURNING_RIGHT) {
			ratioRight += 50;
			leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
		} else if (actualState == State.TURNING_LEFT) {
			ratioLeft -= 50;
			rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
		}
	}

	public void accelerate(int value) {
		leftMotor.getMotor().startSynchronization();
		leftMotor.speedUp(value);
		rightMotor.speedUp(value);
		leftMotor.getMotor().endSynchronization();
	}

	public void decelerate(int value) {
		if(leftMotor.getActual_speed() - value <= 0 || rightMotor.getActual_speed() - value <= 0)
			stop();
		else {
			leftMotor.getMotor().startSynchronization();
			leftMotor.speedDown(value);
			rightMotor.speedDown(value);
			leftMotor.getMotor().endSynchronization();
		}
	}

}