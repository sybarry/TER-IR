
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClassComponent {
	
	private Element classNode;

	public ClassComponent(Element e)
	{
		this.classNode = e;
	}
	
	public void getCode(List<String> code)
	{
		NodeList childNodes = classNode.getChildNodes();
		int nbNodes = childNodes.getLength();
		for(int i = 0;i<nbNodes;i++)
		{
			if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element e = (Element)childNodes.item(i);
				if(e.getTagName() == "Name")
				{
					String className = e.getTextContent();
					code.set(1, "public class " + className + "{\n");
					code.set(4, "}");
				}
				else if(e.getTagName() == "ConnectionInfo")
				{
					new ConnectionInfoComponent(e).getCode(code);
				}
				else if(e.getTagName() == "Fonctions")
				{
					new FonctionsComponent(e).getCode(code);
				}
			}
			
		}
		
		
	}

}
