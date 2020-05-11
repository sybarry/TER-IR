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
		if(connectInfo.hasAttribute("type"))
		{
			// On envoie l'information de type de connexion au composant correspondant
			new ConnectionTypeComponent(connectInfo, connectInfo.getAttribute("type")).getCode(code);
		}
	}

}
