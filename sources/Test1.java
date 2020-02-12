package org.testings;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
public class Test1{

	public static void main(String[] args) {
		EV3UltrasonicSensor uSensor = new EV3UltrasonicSensor(SensorPort.S2);
		SampleProvider sampleProvider = uSensor.getDistanceMode();
		float[] sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		System.out.println(sample[0]);
		while(true){
			if(sample[0]>8){
				System.out.println("OUVERT");
				break;
			}else{
				System.out.println("FERME");
				
			}
			sampleProvider = uSensor.getDistanceMode();
			sample = new float[sampleProvider.sampleSize()];
			sampleProvider.fetchSample(sample, 0);
			Delay.msDelay(1000);
		}
		Delay.msDelay(20000);
		Motor.A.setSpeed(90);
		Motor.D.setSpeed(90);
		Motor.A.forward();
		Motor.D.backward();
		Motor.A.rotateTo(-90);
		Motor.D.stop();
		Motor.A.stop();

	}

}
