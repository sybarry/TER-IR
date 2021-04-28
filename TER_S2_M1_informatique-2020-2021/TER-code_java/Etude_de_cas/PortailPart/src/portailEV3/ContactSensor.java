/**
  * @file ContactSensor.java
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
import lejos.hardware.sensor.EV3TouchSensor;

public class ContactSensor {

	EV3TouchSensor contactSensor;
	
	
	/*---------------------------------------------------------------------
    |  @Method ContactSensor(Port port) 
    |
    |  @Purpose: This method is the constructor of the class, it initializes a contact sensor
    |	 with the port number passing a parameter.
    |
    |  @Parameters:
    |      port -- This parameter is the port number of the sensor.
    |			   It corresponds to the port on the EV3 to which the sensor is connected.
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public ContactSensor(Port port) {
		this.contactSensor = new EV3TouchSensor(port);
	}

	/*---------------------------------------------------------------------
    |  @Method contact()
    |
    |  @Purpose: This method allows to tell if the sensor is pressed or not.
    |
    |  @Parameters: None.
    |
    |  @Returns:  boolean : True if the sensor is pressed, false otherwise.
    -------------------------------------------------------------------*/
	boolean contact() {

		float[] sample = new float[contactSensor.sampleSize()];
		contactSensor.fetchSample(sample, 0);

		float state = sample[0];
		
		return state == 1;

	}

}
