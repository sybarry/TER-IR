import java.io.IOException;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;

	// Methode qui permet d'envoyer un message entre deux appareils
	void sendMessage(String message) throws IOException;
	
	// Methode qui permet de recevoir un message entre deux appareils
	String receiveMessage() throws IOException;
}
