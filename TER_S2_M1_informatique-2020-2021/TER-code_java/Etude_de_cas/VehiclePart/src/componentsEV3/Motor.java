package componentsEV3;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.*;


public class Motor {
	
	//Module correspondant au moteur connecté à la voiture
	private EV3LargeRegulatedMotor oneMotor;
	
	/*
	 * Constructeur de la classe moteur
	 * @param port : le port de la brique EV3
	 */
	public Motor(Port port) {
		this.oneMotor = new EV3LargeRegulatedMotor(port);
	}
	
	/*
	 * Fait tourner le moteur dans un sens ou dans l'autre
	 * @param avance : true pour aller vers l'avant, false pour aller vers l'arrière
	 */
	public void run(boolean avance) {
		if(avance) {
			this.oneMotor.forward();
		}
		else {
			this.oneMotor.backward();
		}
	}
	
	
	/*
	 * Permet au moteur d'accélérer 
	 * @param intensite : Vitesse choisi pour le moteur
	 */
	public void speedUp(int intensite) {
		this.oneMotor.setSpeed(intensite*360);
	}
	
	/*
	 * Arrête le moteur
	 */
	public void stop() {
		this.oneMotor.stop();
	}
	
	/*
	 * Retourne la vitesse du moteur
	 * @return la vitesse actuelle du moteur
	 */
	public int getSpeed(){
		return this.oneMotor.getSpeed();
	}
	
	/*
	 * @return true si le moteur avance
	 */
	public boolean isMoving(){
		return this.oneMotor.isMoving();
	}
	
	/*
	 * @return true si le moteur est arrêté
	 */
	public boolean isStalled(){
		return this.oneMotor.isStalled();
	}

	public EV3LargeRegulatedMotor getOneMotor() {
		return oneMotor;
	}
	
	
}
