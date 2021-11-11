import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class subscriver {

	static String message = "";
	
	public static void main(String[] args) throws MqttException {
		
		final SimpleMqttCallBack callBack = new SimpleMqttCallBack();
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId(), persistence);
		client.setCallback(callBack);
		client.connect();
		System.out.println("Connecte");
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
