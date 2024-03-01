package pillotageBluetoothMQTT;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.remote.nxt.*;
import java.io.*;

public class BTConnect implements Runnable {
	static final String MAC = BrickFinder.getLocal().getBluetoothDevice().getBluetoothAddress();


	public void connect() throws IOException {
		System.out.println("Waiting for bluetooth client");
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		NXTConnection connection = connector.waitForConnection(0, NXTConnection.RAW);
		DataInputStream in = connection.openDataInputStream();
		System.out.println("Client connected");

		while (true) {
			int n = in.read();
			if (!MainMQTT_BT.BT_disconnected)
				executeCommand(MainMQTT_BT.ctrl, n);

			if (n == -1) {
				in.close();
				break;
			}
		}

		System.out.println("Bluetooth disconnected");
	}

	private void executeCommand(Controller ctrl, int command) {
		switch(command) {
			case 1: ctrl.movingForward(); break;
			case 2: ctrl.movingBackward(); break;
			case 3: ctrl.turnLeft(); break;
			case 4: ctrl.turnRight(); break;
			case 5: ctrl.accelerate(50); break;
			case 6: ctrl.decelerate(50); break;
			case 7: ctrl.stop(); break;
		}
	}


	@Override
	public void run() {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}