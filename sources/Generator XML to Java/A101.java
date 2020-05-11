import java.util.List;

//Dans notre étude de cas, cette fonction correspond à la fermeture des portes
//Concrètement, sur les ports A et D, rotation de 90° vers la droite pour A et vers la gauche pour D
public class A101 extends FunctionDefinition {

	@Override
	public void getCode(List<String> code, String n) {
		code.set(0, code.get(0) + "import lejos.hardware.motor.Motor;\n");
		code.set(3, code.get(3) + "public static void " + n + "(){\n"
				+ "Motor.A.setSpeed(90);\nMotor.D.setSpeed(90);\nMotor.A.forward();\nMotor.D.forward();\n"
				+ "Motor.A.rotateTo(0);\nMotor.D.stop();\nMotor.A.stop();\n}\n");
		
	}

}
