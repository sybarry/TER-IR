import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FonctionsComponent {
	
	private Element funcSection;
	private Map<String,FunctionDefinition> actions;
	
	
	/* ##### ESSENTIEL #####
	 * 
	 * Ce constructeur va initialiser les fonctions basiques 
	 * pr�-�crites et identifi�e par la clef "Axxx".
	 * TOUTE NOUVELLE FONCTION DOIT ETRE AJOUTEE AU HASHMAP actions
	 */
	public FonctionsComponent(Element e)
	{
		funcSection = e;
		actions = new HashMap<String,FunctionDefinition>();
		actions.put("A100", new A100());
		actions.put("A101", new A101());
		actions.put("A110", new A110());
		actions.put("A111",new A111());
		// ex si je veux ajouter une nouvelle fonction :
		// actions.put("monID",new monID());
	}
	
	public void getCode(List<String> code)
	{
		NodeList func = funcSection.getElementsByTagName("fonction");
		// Cr�ation des fonctions
		for(int i = 0; i<func.getLength();i++)
		{
			if(func.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element e = (Element)func.item(i);
				// R�cup�ration du nom de la fonction
				String name = e.getAttribute("name");
				//R�cup�ration de son ID : ex ==> A101
				String key = e.getAttribute("actionId");
				// R�cup�ration du code correspondant
				actions.get(key).getCode(code, name);
			}
		}
	}
}
