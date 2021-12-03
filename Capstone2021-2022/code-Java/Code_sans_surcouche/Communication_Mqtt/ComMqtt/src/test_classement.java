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
		
		// Pour un classement emis a la fin de la course que lorsque toutes les voitures ont fini la course
		// Au lancement de la course 
		long chrono = java.lang.System.currentTimeMillis();		
		
		Paire[] chronoPlayer = new Paire[4];
		for(int i = 0; i <= chronoPlayer.length; i++) {
			chronoPlayer[i] = new Paire<String, Long>("Joueur"+i, (long) 0);
		}
		int k = 0;
		while((long) chronoPlayer[0].getSecond() == 0 && (long) chronoPlayer[1].getSecond() == 0 && (long) chronoPlayer[2].getSecond() == 0 && (long) chronoPlayer[3].getSecond() == 0) {
			
			if (callBack.getTopic() == "Joueur"+k && callBack.getMsg() != "" && (long) chronoPlayer[k].getSecond() == 0) {
				chronoPlayer[k].setSecond(Long.parseLong(callBack.getMsg()));
			}
			if(k == 3) {k = 0;}else {k++;}
		}
		
		chronoPlayer = triBulles(chronoPlayer);
		affichage(chronoPlayer);
		
	}
	
	static Paire[] triBulles(Paire T[]){
		Paire<String, Long> temp;
	    for(int i = T.length-1 ; i>=1 ; i--){
	    	for(int j = 0 ; j<i ; j++) {
	    		if((long) T[j].getSecond() > (long) T[j+1].getSecond()){
	    			temp = T[j+1];
                    T[j+1]=T[j];
                    T[j]=temp; 
	    		}
	    	}
	    }
	      return T;
	 }
	
	static void affichage(Paire T[]) {
		for(int i = 0; i<=T.length; i++) {
			System.out.println(T[i].getPremier() + " : " + T[i].getSecond());
		}
	}
}
