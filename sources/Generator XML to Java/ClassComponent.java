
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
		// Récupération de la déclaration de la classe
		ClassDeclaration(code);
		NodeList childNodes = classNode.getChildNodes();
		int nbNodes = childNodes.getLength();
		// Pour le moment, la balise class a 2 sous-balises balises possibles :
		// ConnectionInfo et Fonctions.
		for(int i = 0;i<nbNodes;i++)
		{
			if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element e = (Element)childNodes.item(i);
				// traitement de la balise ConnectionInfo
				if(e.getTagName() == "ConnectionInfo")
				{
					new ConnectionInfoComponent(e).getCode(code);
				}
				// traintement de la balise Fonctions
				else if(e.getTagName() == "Fonctions")
				{
					new FonctionsComponent(e).getCode(code);
				}
			}
		}
		
	}
	
	private void ClassDeclaration(List<String> code)
	{
		String className = classNode.getAttribute("name");
		code.set(1, "public class " + className + "{\n");
		code.set(4, "}");
	}

}
