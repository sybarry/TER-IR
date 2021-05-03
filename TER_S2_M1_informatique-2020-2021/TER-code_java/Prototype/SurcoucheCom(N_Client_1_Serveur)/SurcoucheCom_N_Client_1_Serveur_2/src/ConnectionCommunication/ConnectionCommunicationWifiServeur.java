package ConnectionCommunication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Divers.Client;
import Divers.InfoConnection;
import Exception.MessageException;
import Message.IMessage;

public class ConnectionCommunicationWifiServeur extends AConnectionCommunication {
	
	private ServerSocket server;
	private int port;
	private ArrayList<Client> listClient;
	
	public ConnectionCommunicationWifiServeur(int port) {
		this.port = port;
		this.server = null;
		this.listClient = new ArrayList<Client>();
		this.dOut = null;
		this.dIn = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		server = new ServerSocket(port);
	}
	
	public void acceptConnection() throws IOException {
		System.out.println("Awaiting client..");
		Socket client = this.server.accept();
		
		System.out.println("CLIENT CONNECTED");
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		DataInputStream dIn = new DataInputStream(in);
		DataOutputStream dOut = new DataOutputStream(out);
		
		String ip = client.getInetAddress().getHostAddress();
		InfoConnection infoConnection = new InfoConnection(client.getLocalAddress().getHostAddress(), client.getInetAddress().getHostAddress());
		
		Client cl = new Client(client, dIn, dOut, ip, infoConnection);
		
		listClient.add(cl);
	}
	
	public void streamLoading(String ipReceiver) {
		Client client = findClient(ipReceiver);

		dOut = client.getOut();
		dIn = client.getIn();
		infoConnection = client.getInfoConnection();
	}
	
	public Client findClient(String ip) {
		for(int i = 0; i <= listClient.size()-1; i++) {
			if (listClient.get(i).getIp().equals(ip)) return listClient.get(i);
		}
		return null;
	}
	
	public void sendMessage(IMessage<?> msg, String ipReceiver) throws IOException, MessageException {
		streamLoading(ipReceiver);
		AsendMessage(msg);
	}
	
	public IMessage<?> receiveMessage(String ipReceiver) throws IOException, MessageException {
		streamLoading(ipReceiver);
		return AreceiveMessage();
	}
	
	public void sendMessageSynchronized(IMessage<?> msg, String ipReceiver) throws IOException, MessageException {
		streamLoading(ipReceiver);
		AsendMessageSynchronized(msg);
	}
	
	public void sendMessageAsynchronized(final IMessage<?> msg, String ipReceiver) throws IOException, MessageException {
		streamLoading(ipReceiver);
		AsendMessageAsynchronized(msg);
	}
	
	public void showClient() {
		for(int i = 0; i <= listClient.size()-1; i++) {
			System.out.println("Client n°"+(i+1)+" : "+listClient.get(i).getIp());
		}
	}

	@Override
	public void closeConnection() throws IOException {
		for(int i = 0; i <= listClient.size()-1; i++) {
			listClient.get(i).getClient().close();
		}
		server.close(); 
		System.out.println("Server connection closed");
	}
	
	public void closeConnectionForClient(String ipReceiver) throws IOException {
		Client client = findClient(ipReceiver);
		client.getClient().close(); 
		/*dOut.close();
		dIn.close();*/
		System.out.println("Client connection "+ipReceiver+" closed");
	}
}