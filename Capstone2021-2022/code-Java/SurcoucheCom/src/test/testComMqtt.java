package test;

import java.io.IOException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.MessageString;

public class testComMqtt {
	
	public static void main(String[] args) throws IOException, MessageException {
		
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		
		comMqtt.openConnection();
		comMqtt.setTopic("iot_data");
		
		comMqtt.sendMessage(new MessageString("coucou"));
	}
}
