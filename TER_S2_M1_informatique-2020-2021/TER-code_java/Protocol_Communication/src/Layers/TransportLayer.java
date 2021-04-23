/**
  * @file TransportLayer.java
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
import Message.MessageString;
import Message.MessageFactory;


public class TransportLayer extends ApplicationLayer {
	
	//segment transport
	protected String segement;
	protected String headingSegementTransport;
	
	/*---------------------------------------------------------------------
    |  @Method TransportLayer()
    |
    |  @Purpose: This method is the constructor of the class, it initializes a segment.
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public TransportLayer()
	{
		segement = "";
	}
	
	/*---------------------------------------------------------------------
    |  @Method sendSegmentTransport(IMessage<?> msg)
    |
    |  @Purpose: This method allows to send a message between two devices seen at the level of the transport layer.
    |			
    |  @Parameters: 
    |      msg --  Message to sent
    |	   overlay -- Overlay to sent
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public String sendSegmentTransport(IMessage<?> msg) {
		headingSegementTransport = msg.getIdMessage()+"&&"+msg.getTypeMessage()+"&&";
		segement = headingSegementTransport+sendDataApplication(msg);
		return segement;
	}
	
	
	/*---------------------------------------------------------------------
    |  @Method receiverSegmentTransport(String msg)
    |
    |  @Purpose: This method allows to receive a message between two devices seen at the level of the transport layer
    |			
    |  @Parameters: 
    |      msg --  Message receive
    |
    |  @Returns:  String : Message received converts
    -------------------------------------------------------------------*/
	public  IMessage<?> receiverSegmentTransport(String msg) {
		String[] message = msg.split("&&");
		

		switch(message[1]) {
		case "String":
			return  new MessageString(Integer.parseInt(message[0]),message[1],receiverDataApplication(message[2]));
		case "int":
			return new MessageFactory().createMessage( new MessageString(Integer.parseInt(message[0]), message[1], receiverDataApplication(message[2])));
		case "float":
			return new MessageFactory().createMessage( new MessageString(Integer.parseInt(message[0]), message[1], receiverDataApplication(message[2])));
		case "boolean":
			return new MessageFactory().createMessage( new MessageString(Integer.parseInt(message[0]), message[1], receiverDataApplication(message[2])));
		case "byte":
			return new MessageFactory().createMessage( new MessageString(Integer.parseInt(message[0]), message[1], receiverDataApplication(message[2])));
		}
		return null;
	}
}
