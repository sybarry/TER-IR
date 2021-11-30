package ConnectionCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class ConnectionCommunicationMqttClient extends AConnectionCommunication{
	
	private String ipServer; // The identifier of mqtt server
	private int port; // The port of mqtt connection
	private MqttClient client;
	private MqttMessage message;
	private SimpleMqttCallBack callBack; // Notify a client when receiving a message
	//private String topic; // The canal of publish/subscribe of a message
	
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
		
		dIn = new DataInputStream(null); // pour empecher que le programme renvoie une erreur même si on les utilises pas
		dOut = new DataOutputStream(null); // pour empecher que le programme renvoie une erreur même si on les utilises pas
		// parce que par exemple, lorsque qu'on envoie un message, on regarde si dOut n'est pas egale a null 
		// mais pour mqtt on n'a pas besoin de dOut donc il est forcement a null alors que l'envoie de message fonctionne 
	}

	/**
	 * @return the callBack
	 */
	public SimpleMqttCallBack getCallBack() {
		return callBack;
	}

	/**
	 * @param callBack the callBack to set
	 */
	public void setCallBack(SimpleMqttCallBack callBack) {
		this.callBack = callBack;
	}

	/**
	 * @return the topic
	 */
	/*public String getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	/*public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the ipServer
	 */
	public String getIpServer() {
		return ipServer;
	}

	/**
	 * @param ipServer the ipServer to set
	 */
	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the client
	 */
	public MqttClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
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
	
	public void subscribe(String topic) throws MqttException {
		client.subscribe(topic);
	}
	
	public void unsubscribe(String topic) throws MqttException {
		client.unsubscribe(topic);
	}
	
	@Override
	protected void writeMessage(IMessage<?> msg) throws IOException {
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
	public IMessage<?> receiveMessage() throws IOException, MessageException{

		/*while(callBack.lastMessageTopic(topic) == null) { // pour eviter une erreur si lastMessageTopic() renvoie null
			System.out.print(""); // sans ca, ca ne marche pas (bizarre)
		}

		byte[] convertedMessage = callBack.lastMessageTopic(topic).getBytes();

		IMessage<?> msg = Encodeur_Decodeur.decoderMessage(convertedMessage); // decode the message received

		if(msg.getInfoMessage().getWithACK() == true) sendACK(msg); // sends an acknowledgement of receipt if desired by the message
				
		return msg;*/
		
		throw new MessageException("Please use the receiveMessage(String topic, String keyWord) "
				+ "method to specify the topic to the system");
	}
	
	// Pour utiliser cette méthode, il faut l'envoie des corps du message respecte la norme "keyWord:messageBody"
	// et cette fonction, si la norme est bien utiliser regle le probleme de retrouver un ancien message non traité
	// si cela ne traite pas completement le probleme, ca le regle un peu mais faut renvoyer la liste des message 
	// avec ce keyWord
	public IMessage<?> receiveMessage(String topic, String keyWord) throws IOException, MessageException{
		
		IMessage<?> msg = null;
		
		if(callBack.messageWithKeyWord(topic, keyWord) != null) { // pour eviter une erreur si lastMessageTopic() renvoie null
		
			byte[] convertedMessage = callBack.messageWithKeyWord(topic, keyWord).getBytes();
			msg = Encodeur_Decodeur.decoderMessage(convertedMessage); // decode the message received
			if(msg.getInfoMessage().getWithACK() == true) sendACK(msg); // sends an acknowledgement of receipt if desired by the message
		}	
		
		return msg;
	} 
	
	public void removeTreatedMessage(String message, String topic) {
		callBack.removeMsg(topic, message);
	}

	// Dans le programme utilisateur, ouvrire les canaux sur lequel on veux ecouter au debut du main car on ne sait pas 
	// quand on va recevoir un msg
	
	// demander pourquoi sans le System.out.print(""); ca ne fait rien dans receiveMessage (ConnectionCommunicationMqttClient)
}
