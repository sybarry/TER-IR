import java.util.List;

import org.w3c.dom.Element;

public class OutputTypeComponent {
	
	private String type;
	private Element outputType;
	
	public OutputTypeComponent(Element e,String t)
	{
		type = t;
		outputType = e;
	}
	
	public void getCode(List<String> code)
	{
		// un seul type de message "envoyables" pour le moment : du texte
		if(type.equals("text"))
		{
			// quel message doit être envoyé pour fermer la connexion
			String exit = outputType.getAttribute("ExitCondition");
			// gestion de l'envoie de messages (par un Scanner ici, car text)
			code.set(0, code.get(0) + "import java.util.Scanner;\n");
			code.set(2, code.get(2) + "Scanner sc = new Scanner(System.in);\nString text;\n"
					+ "do{\ntext = sc.nextLine();\nwriter.println(text);\n}while(!text.equals(\""
					+ exit + "\"));\n");
		}
	}

}
