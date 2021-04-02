import java.io.IOException;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;

	// Methode qui permet d'envoyer un message entre deux appareils
	void sendMessage(Object message, int mode) throws IOException;
	
	// Methode qui permet d'envoyer un message entre deux appareils
	boolean sendMessageWithACK(Object message, int mode) throws IOException, InterruptedException;
	
	// Methode qui permet de recevoir un message entre deux appareils
	Object receiveMessage(int mode) throws IOException;
	
	// Methode qui permet de recevoir un message entre deux appareils
	Object receiveMessageWithACK(int mode) throws IOException;
}
