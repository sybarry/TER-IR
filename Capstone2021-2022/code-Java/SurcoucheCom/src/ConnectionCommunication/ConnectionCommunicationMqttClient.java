package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import Divers.InfoConnection;
import Divers.SimpleMqttCallBack;
import Exception.MessageException;
import Message.Encodeur_Decodeur;
import Message.IMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine - ROCHETEAU Nathan
 */

public class ConnectionCommunicationMqttClient extends AConnectionCommunication{
	
	private String ipServer; // The identifier of mqtt server
	private int port; // The port of mqtt connection
	private MqttClient client;
	private MqttMessage message;
	private SimpleMqttCallBack callBack; // Notify a client when receiving a message
	
	/*
	 * Create an instance for a Mqtt client 
	 * 
	 * @param port The port of mqtt connection 
	 * @param ipServer The identifier of mqtt server
	 */
	public ConnectionCommunicationMqttClient(String ipServer, int port) {
		this.ipServer = ipServer;
		this.port = port;
		message = new MqttMessage();
		callBack = new SimpleMqttCallBack();
		//topic = "";
		
		dIn = new DataInputStream(null); // To prevent the program from returning an error even if they are not used
		dOut = new DataOutputStream(null); // To prevent the program from returning an error even if they are not used
		// parce que par exemple, lorsque qu'on envoie un message, on regarde si dOut n'est pas egale a null 
		// mais pour mqtt on n'a pas besoin de dOut donc il est forcement a null alors que l'envoie de message fonctionne 
	}

	public SimpleMqttCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(SimpleMqttCallBack callBack) {
		this.callBack = callBack;
	}

	public String getIpServer() {
		return ipServer;
	}

	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MqttClient getClient() {
		return client;
	}

	public void setClient(MqttClient client) {
		this.client = client;
	}

	@Override
	public void openConnection() throws IOException {
		
		try {
			MemoryPersistence persistence = new MemoryPersistence();
			client = new MqttClient("tcp://"+ipServer+":"+port, MqttClient.generateClientId(), persistence);
			client.connect();
			System.out.println("Mqtt client connected");	
			client.setCallback(callBack);
			
			//String ipClient = InetAddress.getLocalHost().getHostAddress(); // ne marche pas parce qu'il arrive pas a trouver l'ip
			String ipClient = "";
			infoConnection = new InfoConnection(ipClient, ipServer);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void closeConnection() throws IOException {
		try {
			client.disconnect();
			System.out.println("Mqtt client disconnected");	
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Method of subscribing to a topic
	 * 
	 * @param topic The channel you want to listen to
	 */
	public void subscribe(String topic) throws MqttException {
		client.subscribe(topic);
	}
	
	/*
	 * method of unsubscribing from a subject
	 * 
	 * @param the channel on which we no longer want to listen
	 */
	public void unsubscribe(String topic) throws MqttException {
		client.unsubscribe(topic);
	}
	
	@Override
	protected void writeMessage(IMessage<?> msg) throws IOException, MessageException {
		
		standarIsRespected(msg);
		
		byte[] messageConverted = Encodeur_Decodeur.encoderMessage(msg); // Encodes the message
		message.setPayload(messageConverted);
		
		try {
			client.publish(msg.getInfoMessage().getTopic(), message);
			System.out.println("Le message a ete envoye");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendACK(IMessage<?> msg) throws IOException { 

		String m = "received message "+msg.getInfoMessage().getIdMessage();
		message.setPayload(m.getBytes());
		
		try {
			client.publish(msg.getInfoMessage().getTopic(), message);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean receiveACK(IMessage<?> msg) throws IOException { 
		
		boolean result = callBack.lastMessageTopic(msg.getInfoMessage().getTopic()).compareTo("received message "+msg.getInfoMessage().getIdMessage()) == 0;
		if(result == true) { 
			callBack.removeMsg(msg.getInfoMessage().getTopic(), "received message "+idMessage);
		}
		
		return result;
	}
	
	
	@Override
	// Do not use this function because it takes a topic by default and does not search for the message with the mqtt message writing standard
	public IMessage<?> receiveMessage() throws IOException, MessageException{
		
		IMessage<?> msg = null;

		while(callBack.lastMessageTopic("defaultTopic") != null) { // pour eviter une erreur si lastMessageTopic() renvoie null
			
			byte[] convertedMessage = callBack.lastMessageTopic("defaultTopic").getBytes();
			msg = Encodeur_Decodeur.decoderMessage(convertedMessage); // decode the message received
			if(msg.getInfoMessage().getWithACK() == true) sendACK(msg); // sends an acknowledgement of receipt if desired by the message
		
			standarIsRespected(msg);
		}
				
		return msg;
	}

	/*
	 *  Method for receiving a message and sending an acknowledgement if required
	 *  
	 *  @param topic The channel where you want to get the message
	 *  @param keyWord The key word of the message you want to get 
	 *  @return The message receive
	 *  @throws IOException If an I/O error occurs 
	 *  @throws MessageException If the standard of writing a message mqtt is not respected 
	 */
	public IMessage<?> receiveMessage(String topic, String keyWord) throws IOException, MessageException{
		
		IMessage<?> msg = null;
		
		if(callBack.messageWithKeyWord(topic, keyWord) != null) { // To avoid an error if messageWithKeyWord() returns null
		
			byte[] convertedMessage = callBack.messageWithKeyWord(topic, keyWord).getBytes();
			msg = Encodeur_Decodeur.decoderMessage(convertedMessage); // decode the message received
			if(msg.getInfoMessage().getWithACK() == true) sendACK(msg); // sends an acknowledgement of receipt if desired by the message
			
			standarIsRespected(msg);
		}	
		
		return msg;
	} 
	
	/*
	 * Method to delete a message
	 * 
	 * @param message The message you want to delete
	 * @param topic The channel of the message you want to delete
	 */
	public void removeTreatedMessage(String message, String topic) {
		callBack.removeMsg(topic, message);
	}
	
	/*
	 * Method of determining whether a message meets the standard of writing a message mqtt is respected 
	 * 
	 * @param msg The message to be sent or to be receive
	 * @throws MessageException If the standard of writing a message mqtt is not respected 
	 */
	private void standarIsRespected(IMessage<?> msg) throws MessageException {
		Pattern pattern = Pattern.compile(".+:.+"); // The standard for writing a mqtt message 
    	Matcher matcher = pattern.matcher((String) msg.getMessage());
        
		if(!matcher.matches()) { // To find out if the message received meets the standard of writing a message mqtt
			throw new MessageException("The standard of writing a message mqtt is not respected");
		}
	}

	// Dans le programme utilisateur, ouvrire les canaux sur lequel on veux ecouter au debut du main car on ne sait pas 
	// quand on va recevoir un msg
	
	// la norme est keyWord:message
}
