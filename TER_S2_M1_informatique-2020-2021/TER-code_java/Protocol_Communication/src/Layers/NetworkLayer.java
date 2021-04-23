/**
  * @file NetworkLayer.java
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

public class NetworkLayer extends TransportLayer {
	
	//packet network
	protected String packet;
	protected String headingPacketNetwork;
	
	/*---------------------------------------------------------------------
    |  @Method NetworkLayer()
    |
    |  @Purpose: This method is the constructor of the class, it initializes a packet.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public NetworkLayer()
	{
		packet = "";
	}
	
	/*---------------------------------------------------------------------
    |  @Method sendPacketNetwork(IMessage<?> msg, AOverlay overlay)
    |
    |  @Purpose: This method allows to send a message between two devices seen at the level of the network layer.
    |			
    |  @Parameters: 
    |      msg --  Message to sent
    |	   overlay -- Overlay to sent
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public String sendPacketNetwork(IMessage<?> msg, AOverlay overlay ) {
		headingPacketNetwork = overlay.get_addressSender()+"##"+overlay.get_addressReceiver()+"##";
		packet = headingPacketNetwork+sendSegmentTransport(msg);
		return packet;
	}
	
	/*---------------------------------------------------------------------
    |  @Method receiverPacketNetwork(String msg)
    |
    |  @Purpose: This method allows to receive a message between two devices seen at the level of the network layer
    |			
    |  @Parameters: 
    |      msg --  Message receive
    |
    |  @Returns:  String : Message received converts
    -------------------------------------------------------------------*/
	public  IMessage<?> receiverPacketNetwork(String msg) {
		String[] message = msg.split("##");
		
		return receiverSegmentTransport(message[2]) ;
	}

}
