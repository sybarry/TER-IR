package test;

import java.io.IOException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.MessageString;

public class testComMqtt {
	
	public static void main(String[] args) throws IOException, MessageException {
		
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		
		comMqtt.openConnection();
		
		//faut que l'utilsataur utilise que ce constructeur si il veut envoye des message via mqtt
		comMqtt.sendMessage(new MessageString("coucou", "iot_data"));
	}
}
