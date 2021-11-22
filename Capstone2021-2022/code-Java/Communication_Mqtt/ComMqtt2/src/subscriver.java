import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class subscriver {

	private static String message = "";
	private static MqttClient client;
	private static SimpleMqttCallBack callBack;
	
	public static void connectMqtt(String serverAddress, String port) throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		client = new MqttClient("tcp://"+serverAddress+":"+port, MqttClient.generateClientId(), persistence);
	
		client.connect();
		System.out.println("Connecte");
	}
	
	public static void main(String[] args) throws MqttException {
		
		connectMqtt("localhost", "1883");
		callBack = new SimpleMqttCallBack();
		client.setCallback(callBack);
		
		
		client.subscribe("iot_data");	
		System.out.println("En ecoute");

		while(true) {
			
			System.out.print(""); // obliger de mettre ca autrement il ne veut pas rentrer dans le if et afficher "coucou", 
								  // c'est bizarre 
			if(callBack.getMsg().compareTo("Hello world from Java") == 0) {
				System.out.println("coucou"); 
				callBack.setMsg(""); 
			}
		}
		// Est ce que 2 msg recu de 2 topic different sont recu de facon sequentielle 
		
	}

}
