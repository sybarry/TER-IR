import java.util.List;

// Fonction très contextuelle, permet de quitter la boucle si le mot "connect" est entré dans la console en input
public class A111 extends FunctionDefinition {

	
	@Override
	public void getCode(List<String> code,String n) {
		code.set(0, code.get(0) + "import java.util.Scanner;\n");
		code.set(3, code.get(3) + "private static Scanner sc;\npublic static void " + n + "(){\n"
				+ "String text = \"\";\nsc = new Scanner(System.in);\ndo{\ntext = sc.nextLine();\n}while(!text.equals(\"connect\"));\n}\n");
		
	}
	

}