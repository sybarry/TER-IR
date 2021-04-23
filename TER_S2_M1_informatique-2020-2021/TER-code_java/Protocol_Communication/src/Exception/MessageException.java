/**
  * @file MessageException.java
  *
  * @brief ProtocolCommunication
  * @package Exception
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package Exception;

public class MessageException extends Exception {

	private static final long serialVersionUID = 1L;

	/*---------------------------------------------------------------------
    |  @Method MessageException(String s)
    |
    |  @Purpose: This method is the constructor of the class, it calls the constructor of 
    |	the Exception class in order to signal an exception with the parameter as message.
    |
    |  @Parameters:
    |      s -- This parameter is a String which defines the error message.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public MessageException(String s) {
		super(s);
	}
}
