package portailEV3;


import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.*;

public class Motor {

	UnregulatedMotor motor;

	Motor(Port port) {
		this.motor = new UnregulatedMotor(port);
		this.motor.setPower(10);
	}

	void push() {
		this.motor.forward();
		//this.motor.rotate(5);
	}

	void pull() {
		this.motor.backward();
		//this.motor.rotate(-5);
	}

	void stop() {
		this.motor.stop();
	}
}

