import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SimpleMqttCallBack implements MqttCallback{
	
	private String msg;
	private String topic;
	
	public SimpleMqttCallBack() {
		this.msg = "";
		this.topic = "";
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection to MQTT broker lost!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//System.out.println("Message received:\n\t"+ new String(message.getPayload()));
		this.msg = new String(message.getPayload());
		this.topic = topic;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public void setMsg(String newMsg) {
		this.msg = newMsg;
	}
	
	public void setTopic(String newTopic) {
		this.topic = newTopic;
	}
}
