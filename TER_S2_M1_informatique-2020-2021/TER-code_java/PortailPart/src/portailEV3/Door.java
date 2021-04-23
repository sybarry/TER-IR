/**
  * @file Door.java
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

public class Door {

	//Attributs
	Motor motor;
	Boolean open, close, opening, closing, stopping;
	

	/*---------------------------------------------------------------------
    |  @Method Door(Port port)
    |
    |  @Purpose: This method is the constructor of the class, it initializes a door with a motor.
    |			
    |
    |  @Parameters:
    |      port --  The port defines the motor connection input to the EV3.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public Door(Port port) {
		motor = new Motor(port);
		open = close = opening = closing = stopping = false;
	}
	
	/*---------------------------------------------------------------------
    |  @Method getOpening() 
    |
    |  @Purpose: This method allows you to know if the door is opening.
    |
    |  @Parameters: None.
    |
    |  @Returns:  Boolean : Returns true if the gate is opening, false otherwise
    -------------------------------------------------------------------*/
	public Boolean getOpening() {
		return opening;
	}
	
	/*---------------------------------------------------------------------
    |  @Method setOpening(Boolean opening) 
    |
    |  @Purpose: This method allows to say that the door opens
    |
    |  @Parameters:
    |      opening -- This parameter allows to say the state of the door
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void setOpening(Boolean opening) {
		this.opening = opening;
	}

	/*---------------------------------------------------------------------
    |  @Method getClosing() 
    |
    |  @Purpose: This method allows you to know if the door is closing.
    |
    |  @Parameters: None.
    |
    |  @Returns:  Boolean : Returns true if the gate is closing, false otherwise
    -------------------------------------------------------------------*/
	public Boolean getClosing() {
		return closing;
	}

	/*---------------------------------------------------------------------
    |  @Method setClosing(Boolean closing)
    |
    |  @Purpose: This method allows to say that the door closes
    |
    |  @Parameters:
    |      closing -- This parameter allows to say the state of the door
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void setClosing(Boolean closing) {
		this.closing = closing;
	}

	/*---------------------------------------------------------------------
    |  @Method opened() 
    |
    |  @Purpose: This method simply opens the door
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void opened() {
		this.motor.push();
		this.opening = true;
		this.stopping = false;
	}
	
	/*---------------------------------------------------------------------
    |  @Method stop(boolean open)
    |
    |  @Purpose: This method stops the opening or closing of the door.
    |
    |  @Parameters:
    |      open -- This parameter is used to say whether we stop during opening or closing.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void stop(boolean open) {
		this.motor.stop();
		this.opening = this.closing = false;
		this.open = open;
		this.stopping = true;
	}

	/*---------------------------------------------------------------------
    |  @Method closed() 
    |
    |  @Purpose: This method simply closes the door
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	void closed() {

		this.motor.pull();
		this.closing = true;
		this.stopping = false;

	}

	/*---------------------------------------------------------------------
    |  @Method openPosition() 
    |
    |  @Purpose: This method allows you to know if the door is open.
    |
    |  @Parameters: None.
    |
    |  @Returns:  boolean : Returns true if the door is open, false otherwise.
    -------------------------------------------------------------------*/
	boolean openPosition() {
		return this.open;
	}

	/*---------------------------------------------------------------------
    |  @Method closePosition() 
    |
    |  @Purpose: This method allows you to know if the door is close.
    |
    |  @Parameters: None.
    |
    |  @Returns:  boolean : Returns true if the door is close, false otherwise.
    -------------------------------------------------------------------*/
	boolean closePosition() {
		return this.close;
	}
	
}
