package test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;

public class testComMqtt2 {

	static IMessage<?> str = new MessageString("");
	
	public static void main(String[] args) throws IOException, MessageException, MqttException {
		
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		
		comMqtt.openConnection();
		comMqtt.subscribe("iot_data");
		comMqtt.setTopic("iot_data");
		
		new Thread() {
            public void run() {
            	for(;;) {
            		try {
            			str = comMqtt.receiveMessage();
            			TimeUnit.MICROSECONDS.sleep(100); // autrement il execute la fonction avant qu'on ai traité le msg et renvoie des erreurs
					} catch (IOException | MessageException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               	}            	
            }   
        }.start();
		
		while(true) {
			String s1 = (String) str.getMessage();
			System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
			if(s1.compareTo("coucou") == 0) {
				System.out.println("hfd;nhvfdvnfdkj");
				comMqtt.removeTreatedMessage((String) str.toString());
				str = new MessageString("");
			}
			
			/*System.out.print(str.getMessage());
			if(str.getMessage() instanceof String) {
				String s = (String) str.getMessage();
				if(s.compareTo("") != 0) {
					comMqtt.removeTreatedMessage((String) str.toString());
					str = new MessageString("");
				}
			}*/
		}
	}
}
