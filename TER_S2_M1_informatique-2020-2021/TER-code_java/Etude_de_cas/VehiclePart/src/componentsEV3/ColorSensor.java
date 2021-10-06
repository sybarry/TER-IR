package componentsEV3;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor {
	
	//Module correspondant au capteur de contact
	public EV3ColorSensor colorSensor;
		
	public ColorSensor(Port port) {
		this.colorSensor = new EV3ColorSensor(port);
	}
	
	/* True si objet touche
	   False sinon */
	
	public float colorDetection() {
		
		SampleProvider dataSensor = this.colorSensor.getAmbientMode();
		int offsetSample = 0;
		float[] sample = new float[dataSensor.sampleSize()]; 
		
		this.colorSensor.fetchSample(sample, offsetSample);	
		
		float luminosity = (float)sample[0];
		
		return luminosity;
		
	}
}