package ConnectionCommunication;

import java.io.IOException;

import Exception.MessageException;
import Message.IMessage;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;
	
	// Methode qui permet d'envoyer un accus� de r�c�ption d'un message � l'appareil source
	void sendACK(int idMessage) throws IOException;
	
	// Methode qui permet de savoir si on � recu un accus� de r�c�ption pour le message d'identifiant idMessage
	boolean receiveACK(int idMessage) throws IOException;
	
	// Methode qui permet d'envoyer un message entre deux appareils
	void sendMessage(IMessage<?> msg, String... ipReceiver) throws IOException, MessageException;
	
	// Methode qui permet de recevoir un message
	IMessage<?> receiveMessage(String... ipReceiver) throws IOException, MessageException;
	
	// Methode qui permet d'envoyer un message entre deux appareils de mani�re synchrone
	void sendMessageSynchronized(IMessage<?> msg, String... ipReceiver) throws IOException, InterruptedException, MessageException;
	
	// Methode qui permet d'envoyer un message entre deux appareils de mani�re asynchrone
	void sendMessageAsynchronized(final IMessage<?> msg, String... ipReceiver) throws IOException, InterruptedException, MessageException;
}
