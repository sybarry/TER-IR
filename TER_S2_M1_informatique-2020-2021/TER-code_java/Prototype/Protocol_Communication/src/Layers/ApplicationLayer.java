/**
  * @file ApplicationLayer.java
  *
  * @brief ProtocoleCommunication
  * @package Layers
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package Layers;

import Message.IMessage;

public class ApplicationLayer {
	//Application data
	protected String data;
	
	/*---------------------------------------------------------------------
    |  @Method ApplicationLayer()
    |
    |  @Purpose: This method is the constructor of the class, it initializes a application data.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public ApplicationLayer()
	{
		data = "";
	}
	
	/*---------------------------------------------------------------------
    |  @Method sendDataApplication(IMessage<?> msg)
    |
    |  @Purpose: This method allows to send a message between two devices seen at the level of the application layer.
    |			
    |  @Parameters: 
    |      msg --  Message to sent
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public String sendDataApplication(IMessage<?> msg) {
		data = (String) msg.getMessage();
		return data;
	}
	
	/*---------------------------------------------------------------------
    |  @Method receiverDataApplication(String msg)
    |
    |  @Purpose: This method allows to receive a message between two devices seen at the level of the application layer
    |			
    |  @Parameters: 
    |      msg --  Message receive
    |
    |  @Returns:  String : Message received converts
    -------------------------------------------------------------------*/
	public String receiverDataApplication(String msg) {
		return msg;
	}

}
