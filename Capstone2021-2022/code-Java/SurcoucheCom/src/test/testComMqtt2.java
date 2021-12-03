package test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;
import lejos.utility.Delay;

public class testComMqtt2 {

	static IMessage<?> str = new MessageString("");
	
	public static void main(String[] args) throws IOException, MessageException, MqttException, InterruptedException {
		
		final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("localhost", 1883);
		//final ConnectionCommunicationMqttClient comMqtt = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
		
		comMqtt.openConnection();
		// faire tout les subcribe avant de commencer le while(true)
		comMqtt.subscribe("iot_data");
		
		while(true) {
			
			str = comMqtt.receiveMessage("iot_data", "test"); // (topic, keyWord)
			TimeUnit.MICROSECONDS.sleep(200); // mieux de mettre ca directement dans la surcouche mais impossible car
			//Delay.msDelay(200);			  // lejos n'accepte TimeUnit, il utilise Delay (de lejos)
			//System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
			
			/*if(str != null) {
				String s1 = (String) str.getMessage();
				//System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
				
				if(s1.compareTo("") != 0) { // est ce utile de le mettre parce que ca veut dire qu'on a envoye ""
					String[] s2 = s1.split(":");
					//System.out.print(""); // demander pourquoi sans ca, ca ne marche pas 
					
					if((s2[0].compareTo("test") == 0) && (s2[1].compareTo("coucou") == 0)) {
						System.out.println("hfd;nhvfdvnfdkj");
						comMqtt.removeTreatedMessage((String) str.toString(), "iot_data");
						str = null;
					}
				}
			}*/
			
			if(str != null) { // normalement c'est mieux d'utiliser ca, moins de truc inutile
				String[] s1 = ((String) str.getMessage()).split(":"); // avec la norme de message mqtt "keyWord:msg"
					
				if((s1[0].compareTo("test") == 0) && (s1[1].compareTo("coucou") == 0)) { // pour savoir si on as bien recu ce qu'on voulait mais normalement pas besoin de mettre le "coucou"
					System.out.println("hfd;nhvfdvnfdkj"); // traitement du message 
					comMqtt.removeTreatedMessage((String) str.toString(), "iot_data"); // suppression du message
					str = null; // pour eviter que d'autre traite le message 
				}
			}
		}
	}
	
	// probleme d'affichage du message en double sur l'ev3 a cause de la concurrence et de l horloge interne
	// lejos a une fonction delay qui doit remplacer timeunit de java (PC)
	
	// peut etre modifier le SimpleCallBack 
	
	// parler des problème d'identifiant du sender parce que cela depend de la machine, certaine n'accepte pas innetadress, etc
}
