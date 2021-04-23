package ConnectionCommunication;

import java.io.IOException;

import Exception.MessageException;
import Message.IMessage;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;
	
	// Methode qui permet d'envoyer un accusé de récéption d'un message à l'appareil source
	void sendACK(int idMessage) throws IOException;
	
	// Methode qui permet de savoir si on à recu un accusé de récéption pour le message d'identifiant idMessage
	boolean receiveACK(int idMessage) throws IOException;
	
	// Methode qui permet d'envoyer un message entre deux appareils
	void sendMessage(IMessage<?> msg, String... ipReceiver) throws IOException, MessageException;
	
	// Methode qui permet de recevoir un message
	IMessage<?> receiveMessage(String... ipReceiver) throws IOException, MessageException;
	
	// Methode qui permet d'envoyer un message entre deux appareils de manière synchrone
	void sendMessageSynchronized(IMessage<?> msg, String... ipReceiver) throws IOException, InterruptedException, MessageException;
	
	// Methode qui permet d'envoyer un message entre deux appareils de manière asynchrone
	void sendMessageAsynchronized(final IMessage<?> msg, String... ipReceiver) throws IOException, InterruptedException, MessageException;
}
