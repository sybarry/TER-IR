import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class InputTypeComponent {
	
	private String type;
	private Element inputType;
	
	public InputTypeComponent(Element e, String t)
	{
		inputType = e;
		type = t;
	}
	
	public void getCode(List<String> code)
	{
		// un seul type de reception de message pour le moment : texte
		if(type.equals("text"))
		{
			String exit = inputType.hasAttribute("ExitCondition")? inputType.getAttribute("ExitCondition"):"REPLACE WITH VALUE";
			code.set(2, code.get(2) + "String text;\ndo{\ntext = reader.readLine();\n");
			
			// Gestion des arrivées de messages textuels : que faire avec telle ou telle valeur?
			Element inputMapper = (Element)inputType.getElementsByTagName("InputMapper").item(0);
			NodeList mappers = inputMapper.getElementsByTagName("Mapper");
			for(int i = 0; i<mappers.getLength();i++)
			{
				Element map = (Element)mappers.item(i);
				Element guard = (Element)map.getElementsByTagName("guard").item(0);
				String value = guard.getElementsByTagName("value").item(0).getTextContent();
				Element action = (Element)guard.getElementsByTagName("action").item(0);
				String funName = action.getAttribute("name");
				String expect = action.getAttribute("expect");
				Element linkElement = (Element)map.getElementsByTagName("actionReturn").item(0);
				String linkName = linkElement.getAttribute("name");
				
				code.set(2, code.get(2) + "if(text.equals(\"" + value + "\") && "
						+ (expect.equals("true")? "":"!") + funName + "()){\n" + linkName + "();\n}\n");
			}
			code.set(2, code.get(2) + "}while(!text.equals(\"" + exit + "\"));\n");
		}
	}

}
