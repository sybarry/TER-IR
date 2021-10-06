package componentsEV3;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class MotionDetector {
	
	//Module correspondant au capteur de présence
	public EV3UltrasonicSensor motionDetector;
	
	//Choosen distance (in meters) to detect an abstacle or move
	public final double DISTANCE_PRESENCE=0.05;
	
	/*
	 * Constructeur de la classe presenceCapteur
	 * @param port : le port de la brique EV3
	 */
	public MotionDetector(Port port) {
		this.motionDetector = new EV3UltrasonicSensor(port);
	}
	
	/*
	 * Renvoi true si un obstacle est détecté à la distance donnée par DISTANCE_PRESENCE
	 */
	public boolean obstacleDetect() {
		boolean unObstacle = false;
		SampleProvider donneurDistance = this.motionDetector.getDistanceMode();
	
		float[] sample = new float[donneurDistance.sampleSize()]; 
		int offsetSample = 0;
		
		this.motionDetector.fetchSample(sample, offsetSample);
		float distanceObjet = (float)sample[0];
		
		if(distanceObjet<DISTANCE_PRESENCE) {
			unObstacle = true;
		}
		
		return unObstacle;
	}
}
