package com.remoteev3.app.Message;;

import com.remoteev3.app.Divers.InfoConnection;

public interface IMessage<T> {

	// M�thode qui permet de renvoyer l'identifiant d'un message
	int getIdMessage();
	
	// M�thode qui permet de renvoyer les information de connexion li� a ce message 
	InfoConnection getInfoConnection();
	
	// M�thode qui permet de renvoyer le type du message
	String getTypeMessage();
	
	// M�thode qui permet de renvoyer le message 
	T getMessage();
	
	// M�thode qui permet de savoir si oui ou non le message doit avoir ou a eu un accus� de reception
	boolean getWithACK();
	
	// M�thode qui permet de modifier l'identifiant du message
	void setIdMessage(int newIdMessage);
	
	// M�thode qui permet de modifier les information de connexion li� au message
	void setInfoConnection(InfoConnection newInfoConnection);
	
	// M�thode qui permet de modifier le type du message
	void setTypeMessage(String newTypeMessage);
	
	// M�thode qui permet de modifier le message
	void setMessage(T newMessage);
	
	// M�thode qui permet de modifier si oui ou non le message doit avoir ou a eu un accus� de reception
	void setWithACK(boolean newWithACK);
	
	// M�thode qui regroupe tout les attributs li� au message en une cha�ne de carat�re 
	String toString();
}
