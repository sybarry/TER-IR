package Components;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor {
	
	
	public EV3ColorSensor colorSensor;
		
	public ColorSensor(Port port) {
		this.colorSensor = new EV3ColorSensor(port);
	}
	

	
	public float colorDetection() {
		
		SampleProvider dataSensor = this.colorSensor.getAmbientMode();
		int offsetSample = 0;
		float[] sample = new float[dataSensor.sampleSize()]; 
		
		this.colorSensor.fetchSample(sample, offsetSample);	
		
		float luminosity = (float)sample[0];
		
		return luminosity;
		
	}
}