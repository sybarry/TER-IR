import java.util.List;

public class A100 extends FunctionDefinition {

	
	@Override
	public void getCode(List<String> code,String n) {
		code.set(0, code.get(0) + "import lejos.hardware.motor.Motor;\n");
		code.set(3, code.get(3) + "public static void " + n + "(){\n"
				+ "Motor.A.setSpeed(90);\nMotor.D.setSpeed(90);\nMotor.A.forward();\nMotor.D.backward();\n"
				+ "Motor.A.rotateTo(-90);\nMotor.D.stop();\nMotor.A.stop();\n}\n");
		
	}
	

}
