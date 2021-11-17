import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/*
 * Ce programme sera execute sur une machine en java pour piloter la course
 */
public class RaceController {
	
	private static MqttClient client;
	private static MqttMessage message = new MqttMessage();
	private static SimpleMqttCallBack callBack;

	public static void main(String[] args) throws MqttException {
		
		//connectMqtt("192.168.1.9", "1883");
		connectMqtt("localhost", "1883");
		callBack = new SimpleMqttCallBack();
		client.setCallback(callBack);
		Scanner sc = new Scanner(System.in);
		String m ="";

		System.out.println("Ecrire GO pour commencer la course");
		m = sc.next();
		
		if(m.compareTo("GO") == 0) {
			publishMessage("GO", "all");
		}
	}  
	
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

}
