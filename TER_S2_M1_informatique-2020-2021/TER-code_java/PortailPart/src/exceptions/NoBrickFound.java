/**
  * @file NoBrickFound.java
  *
  * @brief PortailPart
  * @package exceptions
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/

package exceptions;

public class NoBrickFound extends RuntimeException {


	private static final long serialVersionUID = 1L;		//serialVersionUID

	
	/*---------------------------------------------------------------------
    |  @Method NoBrickFound(String message)
    |
    |  @Purpose: This method is the constructor of the class, it calls the constructor of 
    |	the RuntimeException class in order to signal an exception with the parameter as message.
    |
    |  @Parameters:
    |      message -- This parameter is a String which defines the error message.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public NoBrickFound(String message) {
		super(message);
	}

}
