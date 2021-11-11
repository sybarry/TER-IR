import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import lejos.hardware.Button;

public class publisher {
	
	public static void main(String[] args) throws MqttException {
		
		MemoryPersistence persistence = new MemoryPersistence();
		final MqttMessage message = new MqttMessage();
		final MqttClient client = new MqttClient("tcp://192.168.1.9:1883", MqttClient.generateClientId(), persistence);
		
		client.connect();
		System.out.println("Connecte");
		
		new Thread() {
			public void run() {
				for(;;) {
					try {
						Button.UP.waitForPressAndRelease();
						client.disconnect();
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		while(true) {
			message.setPayload("Hello world from Java".getBytes());
			Button.DOWN.waitForPressAndRelease();
			client.publish("iot_data", message);
			System.out.println("Le message a ete envoye");
		}

	}

}
