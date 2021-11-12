import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import lejos.hardware.Button;

public class publisher {
	
	private static MqttClient client;
	private static MqttMessage message = new MqttMessage();
	
	public static void connectMqtt(String serverAddress, String port) throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		client = new MqttClient("tcp://"+serverAddress+":"+port, MqttClient.generateClientId(), persistence);
	
		client.connect();
		System.out.println("Connecte");
	}

	public static void publishMessage(String msg, String topic) throws MqttPersistenceException, MqttException {
		message.setPayload(msg.getBytes());
		client.publish(topic, message);
		System.out.println("Le message a ete envoye");
	}
	
public static void main(String[] args) throws MqttException {
		
		connectMqtt("192.168.1.9", "1883");
		
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
			Button.DOWN.waitForPressAndRelease();
			publishMessage("Hello world from Java", "iot_data");
		}

	}

}
