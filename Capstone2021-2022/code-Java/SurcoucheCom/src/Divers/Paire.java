package Divers;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine - ROCHETEAU Nathan
 */

public class Paire<T,U> {
	private T premier; // The first value of the pair
	private U second; // The second value of the pair
	
	/*
	 * Create an instance for a Paire
	 */
	public Paire() { // Constructor used when instantiating the list of unprocessed messages in SimpleMqttCallBack
		
	}
	
	/*
	 * Create an instance for a Paire
	 */
	public Paire(T premier, U second) {
		this.premier = premier;
		this.second = second;
	}
	
	public T getPremier() {
		return this.premier;
	}
	
	public U getSecond() {
		return this.second;
	}
	
	public void setPremier(T newPremier) {
		this.premier = newPremier;
	}
	
	public void setSecond(U newSecond) {
		this.second = newSecond;
	}
}