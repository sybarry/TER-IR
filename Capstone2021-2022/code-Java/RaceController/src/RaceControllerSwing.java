

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;

/*
 * Ce programme sera execute sur une machine en java pour piloter la course
 */
public class RaceControllerSwing {
	
	private static ConnectionCommunicationMqttClient mqttClient;
	private static IMessage<?> str = null;
	private static int nbPlayer = 4;
	private static String topicAll = "All";
	private static HashMap<Integer,Long> playerTimes = new HashMap<Integer,Long>(); // Hashmap des scores d'arrivée de chaque Véhicule
	

	public static void main(String[] args) throws MqttException, IOException, MessageException {
		
		//TODO Instancier une JFrame
		Fenetre fenetre = new Fenetre();
		//TODO Afficher la JFrame
		fenetre.setVisible(true);

		//mqttClient = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
        mqttClient = new ConnectionCommunicationMqttClient("localhost", 1883);
        mqttClient.openConnection();
		
		System.out.println("=> Lancement de la course avec " + nbPlayer + " véhicules");
		
		System.out.println();
		System.out.println("Mise en écoute du controller sur les canaux véhicules :");
		for(int i=1; i<nbPlayer+1; i++) { // Mise en écoute sur les canaux de chaque véhicule
			playerTimes.put(i,(long) 0);
			mqttClient.subscribe("Car"+i);
			System.out.println("	-> En ecoute sur le canal de la voiture "+ i);
		}	
		
	}  
	
	public static void startRace() throws IOException, MessageException {
		boolean isFinished=false;
		
		mqttClient.sendMessage(new MessageString(Command.START, topicAll));
		long startTimer = System.currentTimeMillis();
		System.out.println();
		System.out.println("### START ###");
		
		while(!isFinished) { // a tester

			for(int i=1; i<nbPlayer+1; i++) { 
				str = mqttClient.receiveMessage("Car"+i, Command.keyWordInCommand(Command.FINISH));
				
				if(str != null) {
					String[] s1 = ((String) str.getMessage()).split(":");
					
					if(s1[1].compareTo(Command.messageInCommand(Command.FINISH)) == 0) {
						playerTimes.replace(i, System.currentTimeMillis() - startTimer);
						mqttClient.removeTreatedMessage((String) str.toString(), "Car"+i);
						str = null; 
						
						isFinished = true;
						for(Long s : playerTimes.values()) {
							if(s.equals((long) 0)) {
								isFinished = false;
							}
						}
					}
				}
			}
		}
		
		System.out.println("### END ###");
		
		System.out.println();
		System.out.println("Classement :");
		int k = 1;
		for(Integer i : playerTimes.keySet()) {
			System.out.println("	"+k+" - Joueur "+ i +" : " + playerTimes.get(i));
			k++;
		}
	}
	
	public static HashMap<Integer, Long> sortByValue(HashMap<Integer, Long> hm){
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Long> > list =
               new LinkedList<Map.Entry<Integer, Long> >(hm.entrySet());
        
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Long> >() {
            public int compare(Map.Entry<Integer, Long> o1,
                               Map.Entry<Integer, Long> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
         
        // put data from sorted list to hashmap
        HashMap<Integer, Long> temp = new LinkedHashMap<Integer, Long>();
        for (Entry<Integer, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}

