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
import Message.Encodeur_Decodeur;
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
	
	public Client findClient(String ip) {
		for(int i = 0; i <= listClient.size()-1; i++) {
			if (listClient.get(i).getIp().equals(ip)) return listClient.get(i);
		}
		return null;
	}
	
	@Override
	public void sendMessage(IMessage<?> msg, String... ipReceiver) throws IOException, MessageException {
		Client client = findClient(ipReceiver[0]);

		dOut = client.getOut();
		infoConnection = client.getInfoConnection();
		
		initialisationInfoMessage(msg, false);
		
		if(dOut != null) {
			writeMessage(msg);
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public IMessage<?> receiveMessage(String... ipReceiver) throws IOException, MessageException {
		Client client = findClient(ipReceiver[0]);
		
		dIn = client.getIn();
		
		if(dIn != null) {
			int streamSize = dIn.available();
			while(streamSize == 0) { streamSize = dIn.available();} // car contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donnée, donc si on laisse le read passé sans rien dedans , cela renvoie une erreur
			byte[] convertedMessage = new byte[streamSize]; //pour ne pas allouer plus de case qu'il n'en faut car autrement ca peut créer des problème
			
			dIn.read(convertedMessage); //contrairement a readUTF, readFully() et read() termine meme si il y a rien dans le flux de donnée
			
			IMessage<?> message = Encodeur_Decodeur.decoderMessage(convertedMessage);

			if(message.getWithACK() == true) sendACK(message.getIdMessage()); 
			
			return message;
		}else {
			throw new MessageException("Le flux d'entrée à été mal initialisé");
		}
	}
	
	@Override
	public void sendMessageSynchronized(IMessage<?> msg, String... ipReceiver) throws IOException, MessageException {
		Client client = findClient(ipReceiver[0]);

		dOut = client.getOut();
		infoConnection = client.getInfoConnection();
		
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			ACK(msg, timeOut);
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public void sendMessageAsynchronized(final IMessage<?> msg, String... ipReceiver) throws IOException, MessageException {
		Client client = findClient(ipReceiver[0]);

		dOut = client.getOut();
		infoConnection = client.getInfoConnection();
		
		initialisationInfoMessage(msg, true);
		
		if(dOut != null) {
			writeMessage(msg);
			
			new Thread() {
	    		public void run() {
					try {
						ACK(msg, timeOut);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}   
			}.start();
			
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	public void showClient() {
		for(int i = 0; i <= listClient.size()-1; i++) {
			System.out.println("Client numero "+(i+1)+" : "+listClient.get(i).getIp());
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