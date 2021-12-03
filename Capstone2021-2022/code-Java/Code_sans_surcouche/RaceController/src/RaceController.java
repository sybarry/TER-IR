import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private static int nbPlayer = 4;

	public static void main(String[] args) throws MqttException {
	
		//connectMqtt("192.168.1.9", "1883");
		connectMqtt("localhost", "1883");
		callBack = new SimpleMqttCallBack();
		client.setCallback(callBack);
		Scanner sc = new Scanner(System.in);
		String m ="";
	
		HashMap<Integer,Long> playerTimes = new HashMap<Integer,Long>(); // Hashmap des scores d'arrivée de chaque Véhicule
		boolean isFinished=false;
		long startTimer = 0;
		
		
		System.out.println("=> Lancement de la course avec " + nbPlayer + " véhicules");
		
		System.out.println();
		System.out.println("Mise en écoute du controller sur les canaux véhicules :");
		for(int i=1; i<nbPlayer+1; i++) { // Mise en écoute sur les canaux de chaque véhicule
			playerTimes.put(i,(long) 0);
			client.subscribe("car"+i);
			System.out.println("	-> En ecoute sur le canal de la voiture "+ i);
		}
		
		System.out.println();
		System.out.println("Ecrire GO pour commencer la course");
		m = sc.next();
		
		if(m.compareTo("GO") == 0) { // TO FIX : automatisation du lancement de la course
			publishMessage("GO", "all");
			startTimer = System.currentTimeMillis();
			System.out.println();
			System.out.println("### START ###");
		}
		
		/*
		while(!isFinished) {
			System.out.print("");
			if(callBack.getMsg().compareTo("") == -1) {
				playerTimes.replace((int) callBack.getTopic().charAt(3), System.currentTimeMillis() - startTimer);
				isFinished = true;
				for(Long s : playerTimes.values()) {
					if(s.equals(0)) {
						isFinished = false;
					}
				}
			}
		}
		*/
		
		//######## à séparer ############
		
		System.out.println("### END ###");
		
		System.out.println();
		System.out.println("Voici les scores des véhicules stockés dans une hashmap:");
		playerTimes.put(1, (long) 1233);
		playerTimes.put(2,(long) 821);
		playerTimes.put(3,(long) 1652);
		playerTimes.put(4,(long) 1231);
		System.out.println(playerTimes);
		
		System.out.println();
		System.out.println("### TRI ###");
		playerTimes=sortByValue(playerTimes);
		
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
