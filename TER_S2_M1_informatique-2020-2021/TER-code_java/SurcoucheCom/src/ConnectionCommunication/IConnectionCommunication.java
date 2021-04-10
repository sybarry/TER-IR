package ConnectionCommunication;

import java.io.IOException;

import Message.IMessage;
import Exception.MessageException;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;
	
	// Methode qui permet d'envoyer un message entre deux appareils
	void sendMessage(IMessage<?> msg) throws IOException, MessageException;
	
	// Methode qui permet de recevoir un message entre deux appareils
	Object receiveMessage(IMessage<?> msg) throws IOException, MessageException;

	//boolean sendMessageSynchronizedGen(IMessage<?> msg) throws IOException, InterruptedException;
	
	// Methode qui permet d'envoyer un message entre deux appareils
	boolean sendMessageSynchronized(Object message, int mode) throws IOException, InterruptedException;
	
	// Methode qui permet de recevoir un message entre deux appareils
	Object receiveMessageWithACK(int mode) throws IOException;
}
