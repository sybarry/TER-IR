package ConnectionCommunication;

import java.io.IOException;

public interface IConnectionCommunication {
	
	// Methode qui permet d'etablir une connexion entre deux appareils
	void openConnection() throws IOException;
	
	// Methode qui permet de fermer la connexion entre deux appareils
	void closeConnection() throws IOException;
	
	// Methode qui permet d'envoyer un accusé de récéption d'un message à l'appareil source
	void sendACK(int idMessage) throws IOException;
	
	// Methode qui permet de savoir si on à recu un accusé de récéption pour le message d'identifiant idMessage
	boolean receiveACK(int idMessage) throws IOException;
}
