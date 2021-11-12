
public class Paire<T,U> {
	private T premier;
	private U second;
	
	public Paire() {
		
	}
	
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