import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConnectionInfoComponent {
	
	private Element connectInfo;
	
	public ConnectionInfoComponent(Element e)
	{
		connectInfo = e;
	}
	
	public void getCode(List<String> code)
	{
		NodeList childNodes = connectInfo.getChildNodes();
		int nbNodes = childNodes.getLength();
		for(int i = 0;i<nbNodes;i++)
		{
			if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element e = (Element)childNodes.item(i);
				if(e.getTagName() == "ConnectionType")
				{
					new ConnectionTypeComponent(connectInfo, e.getTextContent()).getCode(code);
				}
			}
			
			
		}
	}

}
