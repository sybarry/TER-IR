package test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;
import lejos.utility.Delay;

public class testComMqtt2 {

	static IMessage<?> str = new MessageString("");
	
	/*public static void main(String[] args) throws IOException, MessageException, MqttException {
		
		//final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
		
		comMqtt.openConnection();
		comMqtt.subscribe("iot_data");
		comMqtt.setTopic("iot_data");
		
		new Thread() {
            public void run() {
            	for(;;) {
            		try {
            			//str = comMqtt.receiveMessage();
            			str = comMqtt.receiveMessage();
            			TimeUnit.MICROSECONDS.sleep(200); // (Pour execution PC) autrement il execute la fonction avant qu'on ai traité le msg et renvoie des erreurs
            			//Delay.msDelay(200); // Pour lejos
            		} catch (IOException | MessageException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               	}            	
            }   
        }.start();
		
		while(true) {

			String s1 = (String) str.getMessage();
			System.out.print("");*/ // demander pourquoi sans ca, ca ne marche pas 
			
			/*if(s1.compareTo("coucou") == 0) {
				System.out.println("hfd;nhvfdvnfdkj");
				comMqtt.removeTreatedMessage((String) str.toString());
				str = new MessageString("");
			}*/
			
			/*System.out.print(str.getMessage());
			if(str.getMessage() instanceof String) {
				String s = (String) str.getMessage();
				if(s.compareTo("") != 0) {
					comMqtt.removeTreatedMessage((String) str.toString());
					str = new MessageString("");
				}
			}*/
		/*}
	}*/
	
	public static void main(String[] args) throws IOException, MessageException, MqttException, InterruptedException {
		
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		//final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
		
		comMqtt.openConnection();
		comMqtt.subscribe("iot_data");
		comMqtt.setTopic("iot_data");
		
		while(true) {
			
			str = comMqtt.receiveMessage("iot_data", "test");
			//TimeUnit.MICROSECONDS.sleep(200); 
			System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
			
			if(str != null) {
				String s1 = (String) str.getMessage();
				System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
				
				if(s1.compareTo("") != 0) {
					String[] s2 = s1.split(":");
					System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
					
					if(s2[1].compareTo("coucou") == 0) {
						System.out.println("hfd;nhvfdvnfdkj");
						comMqtt.removeTreatedMessage((String) str.toString());
					}
				}
			}
		}
	}
	
	// probleme d'affichage du message en double sur l'ev3 a cause de la concurrence et de l horloge interne
	// lejos a une fonction delay qui doit remplacer timeunit de java (PC)
}
