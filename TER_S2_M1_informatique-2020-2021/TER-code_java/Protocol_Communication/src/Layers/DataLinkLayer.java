/**
  * @file DataLinkLayer.java
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
import Overlay.AOverlay;

public class DataLinkLayer extends NetworkLayer{
	//Frame NetworkLayer
	protected String frame;
	protected String headingFrameLiaison;
	protected String controlFrameLiaison;
	
	/*---------------------------------------------------------------------
    |  @Method DataLinkLayer()
    |
    |  @Purpose: This method is the constructor of the class, it initializes a frame.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public DataLinkLayer()
	{
		frame = "";
	}
	
	/*---------------------------------------------------------------------
    |  @Method sendFrameDataLink(IMessage<?> msg, AOverlay overlay)
    |
    |  @Purpose: This method allows to send a message between two devices seen at the level of the data link layer.
    |			
    |  @Parameters: 
    |      msg --  Message to sent
    |	   overlay -- Overlay to sent
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public String sendFrameDataLink(IMessage<?> msg, AOverlay overlay ) {
		headingFrameLiaison = "1@@";
		controlFrameLiaison = "@@1";
		frame = headingFrameLiaison+sendPacketNetwork(msg,overlay)+controlFrameLiaison;
		return frame;
	}
	
	/*---------------------------------------------------------------------
    |  @Method receiveFrameDataLink(String msg, AOverlay overlay)
    |
    |  @Purpose: This method allows to receive a message between two devices seen at the level of the application layer
    |			
    |  @Parameters: 
    |      msg --  Message receive
    |      overlay --  overlay receive
    |
    |  @Returns:  String : Message received converts
    -------------------------------------------------------------------*/
	public IMessage<?> receiveFrameDataLink(String frameReceiver, AOverlay overlay) {
		String[] message = frameReceiver.split("@@");
		
		return receiverPacketNetwork(message[1]) ;
	}

}
