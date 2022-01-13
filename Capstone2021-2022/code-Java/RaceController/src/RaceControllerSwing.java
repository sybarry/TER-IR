

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

import ConnectionCommunication.ConnectionCommunicationMqttClient;
import Exception.MessageException;
import Message.IMessage;
import Message.MessageString;

/*
 * Ce programme sera execute sur une machine en java pour piloter la course
 */
public class RaceControllerSwing {
	
	public static ConnectionCommunicationMqttClient mqttClient;
	public static IMessage<?> str = null;
	public static IMessage<?> str2 = null;
	public static int nbPlayerMax = 4;
	public static String topicAll = "All";
	public static HashMap<Integer,Long> playerTimes = new HashMap<Integer,Long>(); // Hashmap des scores d'arrivée de chaque Véhicule
	
	public static ArrayList<String> listBonus = new ArrayList<String>();
	public static Random random = new Random();
	public static String bonus = "";
	public static boolean isReady = false;
	public static boolean isLaunched=false;
	public static int currentNbPlayer = 3;
	
	public static Fenetre fenetre = new Fenetre();

	public static void main(String[] args) throws MqttException, IOException, MessageException {
		
		// Ajout de la liste des bonus
		listBonus.add("RedShell");
		listBonus.add("GreenShell");
		
		//TODO Afficher la JFrame
		fenetre.setVisible(true);

		while(!isLaunched) {
			System.out.print("");
		}
		
		//mqttClient = new ConnectionCommunicationMqttClient("192.168.1.9", 1883);
        mqttClient = new ConnectionCommunicationMqttClient("localhost", 1883);
        mqttClient.openConnection();
        fenetre.displayConnectedPlayers();
        fenetre.write("=> Launch of the race with " + nbPlayerMax + " vehicles");
		fenetre.write("");
		fenetre.write("Listening of the controller on the vehicle channels :");
		
		for(int i=1; i<nbPlayerMax+1; i++) { // Mise en écoute sur les canaux de chaque véhicule
			playerTimes.put(i,(long) 0);
			mqttClient.subscribe("Car"+i);
			fenetre.write("	-> Listening on the channel of the car n°"+ i);
		}
		
		fenetre.write("");
		fenetre.write("Waiting for all participants...");
		while(currentNbPlayer!=nbPlayerMax) {
			for(int i=1; i<nbPlayerMax+1; i++) { 
				str = mqttClient.receiveMessage("Car"+i, Command.keyWordInCommand(Command.READY));
			
				if(str != null) {
					String[] s1 = ((String) str.getMessage()).split(":");
				
					if(s1[1].compareTo(Command.messageInCommand(Command.READY)) == 0) {
						currentNbPlayer++;
						fenetre.displayConnectedPlayers();
						fenetre.write("Connection of the car n°"+i);
						mqttClient.removeTreatedMessage((String) str.toString(), "Car"+i);
						str = null; 
					}
				}
			}
		}
		fenetre.enableRaceLaunch();
	}  
	
	public static void startRace() throws IOException, MessageException, InterruptedException {
		boolean isFinished=false;
		
		for(int i=3; i>=1; i--) {
			mqttClient.sendMessage(new MessageString("COUNTDOWN:"+i, topicAll));
			fenetre.write(String.valueOf(i));
			TimeUnit.SECONDS.sleep(1);
		}
		
		mqttClient.sendMessage(new MessageString(Command.START, topicAll));
		long startTimer = System.currentTimeMillis();
		fenetre.write("### START ###");
		int a=0;
		
		
		
		while(!isFinished) { // a tester
			for(int i=1; i<(nbPlayerMax+1); i++) {
				
				str = mqttClient.receiveMessage("Car"+i, Command.keyWordInCommand(Command.FINISH));
				str2 = mqttClient.receiveMessage("Car"+i, Command.keyWordInCommand(Command.WANTBONUS));
				if(str != null) {
					String[] s1 = ((String) str.getMessage()).split(":");
					
					if(s1[1].compareTo(Command.messageInCommand(Command.FINISH)) == 0) {
						playerTimes.replace(i, System.currentTimeMillis() - startTimer);
						fenetre.write("The vehicle n°"+i+" has finished the race in "+((double) playerTimes.get(i))/1000+" seconds.");
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
				if(str2 != null) {
					String[] s2 = ((String) str.getMessage()).split(":");
					if(s2[1].compareTo(Command.messageInCommand(Command.WANTBONUS))==0) {
						
						bonus = listBonus.get(random.nextInt(listBonus.size()));
						int sendPenalty = 0;
						
						switch(bonus) {
							case "RedShell":
								mqttClient.sendMessage(new MessageString("Bonus:"+bonus, "Car"+i));
								
								sendPenalty = random.nextInt(nbPlayerMax)+1;
								while(sendPenalty == i) {
									sendPenalty = random.nextInt(nbPlayerMax)+1;
								}
								
								mqttClient.sendMessage(new MessageString("Malus:"+bonus, "Car"+sendPenalty));
								fenetre.write("The vehicle n°"+i+" has launched a red shell on the vehicle n°"+sendPenalty+" !");
								break;
							case "GreenShell":
								mqttClient.sendMessage(new MessageString("Bonus:"+bonus, "Car"+i));
								
								sendPenalty = random.nextInt(nbPlayerMax)+1;
								if(i==sendPenalty) {
									fenetre.write("Oops, the vehicle n°"+i+" took his own green shell !");
								}else {
									fenetre.write("The vehicle n°"+i+" has thrown a green shell to the vehicle n°"+sendPenalty+" !");
								}
								mqttClient.sendMessage(new MessageString("Malus:"+bonus, "Car"+sendPenalty));
								break;
							default:
								break;
								
						}
						
						mqttClient.removeTreatedMessage((String) str2.toString(), "Car"+i);
						str2 = null;
					}
				}
			}
		}
		
		fenetre.write("### END ###");
		playerTimes=sortByValue(playerTimes);
		fenetre.write("Standings :");
		String Standings = "STANDINGS:";
		int k = 1;
		for(Integer i : playerTimes.keySet()) {
			String playerPosition = "    "+k+" - Player "+ i +" : " + ((double) (playerTimes.get(i))/1000);
			fenetre.write(playerPosition);
			Standings = Standings + playerPosition + "\n";
			k++;
		}
		mqttClient.sendMessage(new MessageString(Standings, topicAll));
		
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

