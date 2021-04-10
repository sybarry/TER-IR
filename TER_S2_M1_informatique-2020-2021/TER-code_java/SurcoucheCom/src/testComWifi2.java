
import java.io.IOException;
import ConnectionCommunication.ConnectionCommunicationWifiClient;
import Exception.MessageException;
import Message.MessageString;

/*
 * pour l'ev3 D4, portail, client
 */

public class testComWifi2 {
	
	static String str = "";
	
	public static void main(String[] args) throws IOException, InterruptedException, MessageException {
		
		String ip = "192.168.1.22"; // connexion ev3 
		int port = 1234;

		final ConnectionCommunicationWifiClient comWifi = new ConnectionCommunicationWifiClient(port, ip);
		
		comWifi.openConnection();
		
		new Thread() {
            public void run() {
            	for(;;) {
	               	try {
	               		str = (String) comWifi.receiveMessageGen(new MessageString());
	            		System.out.println(str);
	    			} catch (IOException | MessageException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
               	}            	
            }   
        }.start();
                
        /*TimeUnit.MINUTES.sleep(1);
        dOut.writeUTF("disconect");
		dOut.flush();*/
		
		//client.close();
		
		while(true) {
			
			/*if(str.contains("ev3")) {
				TimeUnit.SECONDS.sleep(10);
				str = "";
				System.out.println("disconnect");
				client.close();
			}*/
		}
	}
}
