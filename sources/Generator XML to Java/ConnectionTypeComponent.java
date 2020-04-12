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
		if(type.equals("Wifi") || type.equals("USB"))
		{
			code.set(0, code.get(0)+ "import java.net.Socket;\nimport java.io.IOException;\n");
			
			String role = connectType.getElementsByTagName("Role").item(0).getTextContent();
			if(role.equals("Client"))
			{
				
				String ip = connectType.getElementsByTagName("IP").item(0).getTextContent();
				String port = connectType.getElementsByTagName("Port").item(0).getTextContent();
				code.set(2, "public static void main(String[] args){\ntry(Socket socket = new Socket(\""
						+ ip + "\"," + port + ")){\n");
				
				if(connectType.getElementsByTagName("OutputType").getLength() != 0)
				{
					code.set(0, code.get(0) + "import java.io.OutputStream;\nimport java.io.PrintWriter;\n");
					code.set(2, code.get(2) + "OutputStream output = socket.getOutputStream();\n"
							+ "PrintWriter writer = new PrintWriter(output,true);\n");
					new OutputTypeComponent(connectType,connectType.getElementsByTagName("OutputType").item(0).getTextContent()).getCode(code);
				}
				
				code.set(2, code.get(2) + "socket.close();\n}\ncatch(IOException e)"
						+ "{\nSystem.out.println(\"Error\");\n}\n}\n");
			}
			else if(role.equals("Server"))
			{
				String port = connectType.getElementsByTagName("Port").item(0).getTextContent();
				code.set(0, code.get(0) + "import java.io.ServerSocket:\n");
				code.set(2, "public static void main(String[] args{\ntry(ServerSocket socket = new ServerSocket("
						+ port + ")){\nSocket connectionSocket = socket.accept();\n");
				
				if(connectType.getElementsByTagName("InputType").getLength() != 0)
				{
					code.set(0, code.get(0) + "import java.io.BufferedReader;\n"
							+ "import java.io.InputStreamReader;\n");
					code.set(2, code.get(2) + "BufferedReader reader = new BuffuredReader(new InputStreamReader(connectionSocket.getInputStream()));\n");
					new InputTypeComponent(connectType,connectType.getElementsByTagName("InputType").item(0).getTextContent()).getCode(code);
				}
				
				code.set(2, code.get(2) + "socket.close();\n}\ncatch(IOException e){\ne.printStackTrace();\n}\n}\n");
			}
		}
	}
}
