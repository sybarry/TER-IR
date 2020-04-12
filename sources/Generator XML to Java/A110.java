import java.util.List;

public class A110 extends FunctionDefinition {

	@Override
	public void getCode(List<String> code, String n) {
		code.set(0, code.get(0) + "import lejos.hardware.port.SensorPort;\n"
				+ "import lejos.hardware.sensor.EV3UltrasonicSensor;\n"
				+ "import lejos.robotics.SampleProvider;\n");
		code.set(3, code.get(3) + "private static EV3UltrasonicSensor uSensor;\n"
				+ "public static boolean " + n + "(){\nSampleProvider sampleProvider = uSensor.getDistanceMode();\n"
						+ "float[] sample = new float[sampleProvider.sampleSize()];\n"
						+ "sampleProvider.fetchSample(sample, 0);\n"
						+ "if(sample[0]>8){\nreturn true;\n}\nreturn false;\n}\n");
		
	}

}
