import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConnectionTypeComponent {
	
	private Element connectType;
	private String type;
	
	public ConnectionTypeComponent(Element e, String t)
	{
		connectType = e;
		type = t;
	}
	
	public void getCode(List<String> code)
	{
		// Les types Wifi et USB sont très similaires, ils sont donc traités ensemble
		if(type.equals("Wifi") || type.equals("USB"))
		{
			// Ajout des imports nécessaires
			code.set(0, code.get(0)+ "import java.net.Socket;\nimport java.io.IOException;\n");
			
			//Récupération du rôle : Serveur ou Client
			String role = connectType.getAttribute("role");
			if(role.equals("Client"))
			{
				//Récupération de l'IP et du port
				String ip = connectType.getAttribute("ip");
				String port = connectType.getAttribute("port");
				// Etablissement de la connexion
				code.set(2, "public static void main(String[] args){\ntry(Socket socket = new Socket(\""
						+ ip + "\"," + port + ")){\n");
				
				// Si la classe a besoin d'envoyer des messages à distance (il suffit de rajouter un if([...]"InputType") pour la réception)
				if(connectType.hasAttribute("OutputType"))
				{
					code.set(0, code.get(0) + "import java.io.OutputStream;\nimport java.io.PrintWriter;\n");
					code.set(2, code.get(2) + "OutputStream output = socket.getOutputStream();\n"
							+ "PrintWriter writer = new PrintWriter(output,true);\n");
					new OutputTypeComponent(connectType,connectType.getAttribute("OutputType")).getCode(code);
				}
				
				code.set(2, code.get(2) + "socket.close();\n}\ncatch(IOException e)"
						+ "{\nSystem.out.println(\"Error\");\n}\n}\n");
			}
			else if(role.equals("Server"))
			{
				// Meme principe que pour le client mais avec les spécifités du serveur
				String port = connectType.getAttribute("port");
				code.set(0, code.get(0) + "import java.net.ServerSocket;\n");
				code.set(2, "public static void main(String[] args){\ntry(ServerSocket socket = new ServerSocket("
						+ port + ")){\nSocket connectionSocket = socket.accept();\n");
				
				if(connectType.hasAttribute("InputType"))
				{
					code.set(0, code.get(0) + "import java.io.BufferedReader;\n"
							+ "import java.io.InputStreamReader;\n");
					code.set(2, code.get(2) + "BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));\n");
					new InputTypeComponent(connectType,connectType.getAttribute("InputType")).getCode(code);
				}
				
				code.set(2, code.get(2) + "socket.close();\n}\ncatch(IOException e){\ne.printStackTrace();\n}\n}\n");
			}
		}
	}
}
