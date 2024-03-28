package pillotageBluetoothMQTT;

/**
 *  Enumération pour les états du robot
 *  STOP = 1, FORWARD = 2, BACKWARD = 3, TURNING_LEFT = 4, TURNING_RIGHT = 5
 *  */
public enum State {
	STOPPED(1), FORWARD(2), BACKWARD(3), TURNING_LEFT(4), TURNING_RIGHT(5);

	private final int value;

	State(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
