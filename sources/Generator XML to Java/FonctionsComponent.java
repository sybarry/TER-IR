import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FonctionsComponent {
	
	private Element funcSection;
	private Map<String,FunctionDefinition> actions;
	
	
	public FonctionsComponent(Element e)
	{
		funcSection = e;
		actions = new HashMap<String,FunctionDefinition>();
		actions.put("A100", new A100());
		actions.put("A101", new A101());
		actions.put("A110", new A110());
	}
	
	public void getCode(List<String> code)
	{
		NodeList func = funcSection.getElementsByTagName("Fonction");
		for(int i = 0; i<func.getLength();i++)
		{
			if(func.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element e = (Element)func.item(i);
				String name = e.getElementsByTagName("Name").item(0).getTextContent();
				String key = e.getElementsByTagName("action").item(0).getTextContent();
				actions.get(key).getCode(code, name);
			}
		}
	}
}
