package Message;

import Divers.InfoConnection;

public interface IMessage<T> {

	// Méthode qui permet de renvoyer l'identifiant d'un message
	int getIdMessage();
	
	// Méthode qui permet de renvoyer les information de connexion lié a ce message 
	InfoConnection getInfoConnection();
	
	// Méthode qui permet de renvoyer le type du message
	String getTypeMessage();
	
	// Méthode qui permet de renvoyer le message 
	T getMessage();
	
	// Méthode qui permet de savoir si oui ou non le message doit avoir ou a eu un accusé de reception
	boolean getWithACK();
	
	// Méthode qui permet de modifier l'identifiant du message
	void setIdMessage(int newIdMessage);
	
	// Méthode qui permet de modifier les information de connexion lié au message
	void setInfoConnection(InfoConnection newInfoConnection);
	
	// Méthode qui permet de modifier le type du message
	void setTypeMessage(String newTypeMessage);
	
	// Méthode qui permet de modifier le message
	void setMessage(T newMessage);
	
	// Méthode qui permet de modifier si oui ou non le message doit avoir ou a eu un accusé de reception
	void setWithACK(boolean newWithACK);
	
	// Méthode qui regroupe tout les attributs lié au message en une chaîne de caratère 
	String toString();
}
