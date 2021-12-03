package Divers;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine - ROCHETEAU Nathan
 */

public class SimpleMqttCallBack implements MqttCallback{
	
	private ArrayList<Paire<String, String>> listMsg; // Paire(topic, message), the list of unprocessed messages
	
	/*
	 * Create an instance for a SimpleMqttCallBack
	 */
	public SimpleMqttCallBack() {
		listMsg = new ArrayList<Paire<String, String>>();
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection to MQTT broker lost!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		listMsg.add(new Paire<String, String>(topic, new String(message.getPayload())));
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Paire<String, String>> getListMsg() {
		return this.listMsg;
	}
	
	/*
	 * Method that allows you to return the last message received on a topic
	 * 
	 * @param topic The channel where you want to retrieve the message
	 * @return The String message
	 */
	public String lastMessageTopic(String topic){ 
		
		int i = listMsg.size() - 1;

		while((i >= 0) && listMsg.size() != 0){
			if(listMsg.get(i).getPremier().compareTo(topic) == 0) {
				return listMsg.get(i).getSecond();
			}
			i--;
		}
		
		return null;
	}
	
	/*
	 * Method that returns a message from a key word and a topic
	 * 
	 * @param topic The channel where you want to retrieve the message
	 * @param keyWord The keyword of the message you want to retrieve 
	 * @return The string message 
	 */
	public String messageWithKeyWord(String topic, String keyWord) {
		
		int i = listMsg.size() - 1;
		
		while((i >= 0) && listMsg.size() != 0){
			if(listMsg.get(i).getPremier().compareTo(topic) == 0) {
				if(listMsg.get(i).getSecond().contains(keyWord)) {
					return listMsg.get(i).getSecond();
				}
			}
			i--;
		}
		
		return null;
	}
	
	/*
	 * Method to delete a message from the list of unprocessed messages
	 * 
	 * @param message The message you want to delete
	 * @param topic The channel of the message you want to delete
	 */
	public void removeMsg(String topic, String message) {
		boolean remove = false;
		int i = listMsg.size() - 1;
		
		while((remove == false) && (i >= 0)) {
			if((listMsg.get(i).getPremier().compareTo(topic) == 0) && (listMsg.get(i).getSecond().compareTo(message) == 0)) {
				listMsg.remove(i);
				remove = true;
			}
			i--;
		}
	}
	
	/*
	 * Method that displays the content of the list of unprocessed messages
	 */
	public void affichage() {
		System.out.print("[");
		for(int i=0; i<=listMsg.size()-1;i++) {
			System.out.print("["+listMsg.get(i).getPremier()+", "+listMsg.get(i).getSecond()+"]"+" / ");
		}
		System.out.println("]");
	}
	
	// a chaque fois qu'on traite un message dans le code client, il faudra le supprimer de la liste
}
