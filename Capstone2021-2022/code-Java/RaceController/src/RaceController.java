import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;

/*
 * Ce programme sera execute sur une machine en java pour piloter la course
 */
public class RaceController {
	
	private static ConnectionCommunicationMqttClient mqttClient;
	private static IMessage<?> str = null;
	private static int nbPlayer = 4;
	private static String topicAll = "All";
	
	private static ArrayList<String> listBonus = new ArrayList<String>();
	private static Random random = new Random();
	private static String bonus = "";

	public static void main(String[] args) throws MqttException, IOException, MessageException {
		
		// Ajout de la liste des bonus
		listBonus.add("RedShell");
		listBonus.add("GreenShell");

		//mqttClient = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
        mqttClient = new ConnectionCommunicationMqttClient("localhost", 1883);
        mqttClient.openConnection();
		
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
			mqttClient.subscribe("Car"+i);
			System.out.println("	-> En ecoute sur le canal de la voiture "+ i);
		}
		
		System.out.println();
		System.out.println("Ecrire GO pour commencer la course");
		m = sc.next();
		
		if(m.compareTo("GO") == 0) { // TO FIX : automatisation du lancement de la course
			
			/*for(int i = 1; i<=3; i++) {
				mqttClient.sendMessage(new MessageString("COUNTDOWN:"+i, topicAll));
				System.out.println(i);
				TimeUnit.SECONDS.sleep(1);
			}*/
			
			mqttClient.sendMessage(new MessageString(Command.START, topicAll));
			startTimer = System.currentTimeMillis();
			System.out.println();
			System.out.println("### START ###");
		}
		
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
				
				str = mqttClient.receiveMessage("Car"+i, Command.keyWordInCommand(Command.WANTBONUS));
				if(str != null) {
					String[] s1 = ((String) str.getMessage()).split(":");
					
					if(s1[1].compareTo(Command.messageInCommand(Command.WANTBONUS)) == 0) {
						
						bonus = listBonus.get(random.nextInt(listBonus.size()));
						int sendMalus = 0;
						
						switch(bonus) {
							case "RedShell":
								mqttClient.sendMessage(new MessageString("Bonus:"+bonus, "Car"+i));
								
								sendMalus = random.nextInt(nbPlayer)+1;
								while(sendMalus == i) {
									sendMalus = random.nextInt(nbPlayer)+1;
								}
								
								mqttClient.sendMessage(new MessageString("Malus:"+bonus, "Car"+sendMalus));							
								break;
							case "GreenShell":
								mqttClient.sendMessage(new MessageString("Bonus:"+bonus, "Car"+i));
								
								sendMalus = random.nextInt(nbPlayer)+1;
								mqttClient.sendMessage(new MessageString("Malus:"+bonus, "Car"+sendMalus));
								break;
							default:
								break;
								
						}
						
						mqttClient.removeTreatedMessage((String) str.toString(), "Car"+i);
						str = null; 
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
