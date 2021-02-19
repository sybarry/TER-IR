package CommunicationBT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class CommunicationBT {
	private DataOutputStream out; 
	private DataInputStream in;
	private BTConnection BTConnect;
	private BTConnector BTconnector;
	private EV3 brick;
	
	
	public CommunicationBT(){
		
	}
	
	public boolean ConnexionServeur() {
    	boolean result = false;
    	
		System.out.println("En attente d'un client");
		BTConnector BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnect = (BTConnection) BTconnector.waitForConnection(30000, NXTConnection.PACKET);
		if(BTConnect != null)
		{
			setOut(BTConnect.openDataOutputStream());
			setIn(BTConnect.openDataInputStream());
			System.out.println("Connecté");
			result=true;
		}
		return result;
		
	}
	
	public boolean ConnexionClient(String serveurName) {
		boolean result = false;
		
		System.out.println("Recherche du serveur");
		setBrick((EV3) BrickFinder.getLocal());
	    BTconnector = (BTConnector) Bluetooth.getNXTCommConnector();
	    
	    BTConnect = (BTConnection) BTconnector.connect(serveurName, NXTConnection.PACKET);
  		if(BTConnect != null)
  		{
  			setOut(BTConnect.openDataOutputStream());
  			setIn(BTConnect.openDataInputStream());
  			System.out.println("Connecté");
  			result = true;
  		}
	   	
		return result;
	}
	
	public String ReceiveMsg() throws IOException {
		String msg = in.readUTF();
		
		return msg;
	}
	
	public void SendMsg(String Msg) throws IOException {
		out.writeUTF(Msg); 
		out.flush();
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public EV3 getBrick() {
		return brick;
	}

	public void setBrick(EV3 brick) {
		this.brick = brick;
	}
	
	
	

}
