/**
  * @file Motor.java
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


import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.*;

public class Motor {

	//Attribut
	UnregulatedMotor motor;

	/*---------------------------------------------------------------------
    |  @Method Motor(Port port)
    |
    |  @Purpose: This method is the constructor of the class, it initializes a motor.
    |			
    |
    |  @Parameters:
    |      port --  The port defines the motor connection input to the EV3.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	Motor(Port port) {
		this.motor = new UnregulatedMotor(port);
		this.motor.setPower(10);
		this.motor.stop();
	}


	/*---------------------------------------------------------------------
    |  @Method push() 
    |
    |  @Purpose: This method simply motor forward.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void push() {
		this.motor.forward();
		//this.motor.rotate(5);
	}

	/*---------------------------------------------------------------------
    |  @Method pull() 
    |
    |  @Purpose: This method simply motor backward.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void pull() {
		this.motor.backward();
		//this.motor.rotate(-5);
	}

	/*---------------------------------------------------------------------
    |  @Method stop() 
    |
    |  @Purpose: This method simply motor stop.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void stop() {
		this.motor.stop();
	}
}

