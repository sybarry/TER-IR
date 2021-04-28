/**
  * @file PresenceSensor.java
  *
  * @brief PortailPart
  * @package portailEV3
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package portailEV3;


import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class PresenceSensor {
	
	//Module corresponding to the presence sensor.
	public EV3UltrasonicSensor aSensor;
	
	//Distance chosen (in meters) for the detection of an obstacle.
	public final double DISTANCE_PRESENCE=0.13;
	
	/*---------------------------------------------------------------------
    |  @Method PresenceSensor(Port port)
    |
    |  @Purpose: This method is the constructor of the class, it initializes a sensor.
    |			
    |
    |  @Parameters:
    |      port --  The port defines the motor connection input to the EV3.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public PresenceSensor(Port port) {
		this.aSensor = new EV3UltrasonicSensor(port);
	}
	
	/*
	 * Returns true if an obstacle is detected at the distance given by DISTANCE_PRESENCE
	 */
	/*---------------------------------------------------------------------
    |  @Method PresenceSensor(Port port)
    |
    |  @Purpose: This method returns true if an obstacle is detected at the distance given by DISTANCE_PRESENCE
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  true if an obstacle is detected at the distance given by DISTANCE_PRESENCE, false otherwise
    -------------------------------------------------------------------*/
	public boolean obstacleDetect() {
		boolean aObstacle = false;
		SampleProvider donorDistance = this.aSensor.getDistanceMode();
	
		float[] sample = new float[donorDistance.sampleSize()]; 
		int offsetSample = 0;
		
		this.aSensor.fetchSample(sample, offsetSample);
		float distanceObject = (float)sample[0];
		
		if(distanceObject<DISTANCE_PRESENCE) {
			aObstacle = true;
		}
		
		return aObstacle;
	}
}
