import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class test_classement {
	
	public static void main(String[] args) throws MqttException {
		
		final SimpleMqttCallBack callBack = new SimpleMqttCallBack();
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId(), persistence);
		client.setCallback(callBack);
		client.connect();
		System.out.println("Connecte");
		
		client.subscribe("all");
		client.subscribe("Joueur0");
		client.subscribe("Joueur1");
		client.subscribe("Joueur2");
		client.subscribe("Joueur3");
		
		// Au lancement de la course 
		long chrono = java.lang.System.currentTimeMillis();		
		
		long[] chronoPlayer = new long[4];
		for(int i = 0; i <= chronoPlayer.length; i++) {
			chronoPlayer[i] = 0;
		}
		int j = 0;
		while(chronoPlayer[0] == 0 && chronoPlayer[1] == 0 && chronoPlayer[2] == 0 && chronoPlayer[3] == 0) {
			
			if (callBack.getTopic() == "Joueur"+j && callBack.getMsg() != "" && chronoPlayer[j] == 0) {
				chronoPlayer[j] = Long.parseLong(callBack.getMsg());
			}
			if(j == 3) {j=0;}else {j++;}
		}
		
		
		
	}

}
