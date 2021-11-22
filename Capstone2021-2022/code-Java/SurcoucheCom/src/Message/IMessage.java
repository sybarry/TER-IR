package Message;

import Divers.InfoConnection;
import Divers.InfoMessage;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public interface IMessage<T> {
	
	/*
	 * Method to return the connection information related to this generic message 
	 * 
	 * @return the connection information related to this generic message 
	 */
	InfoConnection getInfoConnection();
	
	/*
	 * Method for returning generic message characteristics
	 * 
	 * @return the generic message characteristics
	 */
	InfoMessage getInfoMessage();
	
	/*
	 * Method which allows return the generic message
	 * 
	 * @return the generic message 
	 */
	T getMessage();
	
	/*
	 * Method to change the connection information related to this generic message
	 * 
	 *  @param newInfoConnection The new connection information related to this generic message
	 */
	void setInfoConnection(InfoConnection newInfoConnection);
	
	/*
	 * Method to change the generic message characteristics
	 * 
	 * @return newInfoMessage The new generic message characteristics
	 */
	void setInfoMessage(InfoMessage newInfoMessage);
	
	/*
	 * Method to change the generic message
	 * 
	 * @param The new generic message
	 */
	void setMessage(T newMessage);
	
	/*
	 * Method that groups all the attributes related to the generic message into a string
	 * 
	 * @return The string corresponding to the concatenation of the attributes of the generic message class
	 */
	String toString();
}
