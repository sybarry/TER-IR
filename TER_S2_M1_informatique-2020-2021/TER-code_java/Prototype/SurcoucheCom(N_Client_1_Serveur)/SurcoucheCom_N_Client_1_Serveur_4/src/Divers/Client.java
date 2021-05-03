package Divers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
	Socket client;
	DataInputStream in;
	DataOutputStream out;
	String ip;
	InfoConnection infoConnection;
	
	public Client(Socket client,  DataInputStream in, DataOutputStream out, String ip, InfoConnection infoConnection) {
		this.client = client;
		this.in = in;
		this.out = out;
		this.ip = ip;
		this.infoConnection = infoConnection;
	}
	
	public Socket getClient() {
		return this.client;
	}
	
	public DataInputStream getIn() {
		return this.in;
	}
	
	public DataOutputStream getOut() {
		return this.out;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public InfoConnection getInfoConnection() {
		return this.infoConnection;
	}
	
	public void setClient(Socket newClient) {
		this.client = newClient;
	}
	
	public void setIn(DataInputStream newIn) {
		this.in = newIn;
	}
	
	public void setOut(DataOutputStream newOut) {
		this.out = newOut;
	}
	
	public void setIp(String newIp) {
		this.ip = newIp;
	}
	
	public void setInfoConnection(InfoConnection newInfoConnection) {
		this.infoConnection = newInfoConnection;
	}
}
