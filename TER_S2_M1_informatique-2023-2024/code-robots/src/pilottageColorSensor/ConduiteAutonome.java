package pilottageColorSensor;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import pilottageMQTT.Action;
import pilottageMQTT.MotorSync;

public class ConduiteAutonome {
    private final NXTRegulatedMotor leftMotor;
    private final NXTRegulatedMotor rightMotor;
    private EV3ColorSensor colorSensor;
    private final EV3 ev3;
    private final TextLCD lcd;
    private final EV3UltrasonicSensor ultrasonicSensor;
    private final Automate automate;
    private final Boolean isdoing = true;

    /*    private final float distanceSeuil = 0.24f;
        SampleProvider distanceProvider;
        float[] distanceSample ;*/
    public ConduiteAutonome(Automate automate) {
        leftMotor = Motor.B;
        rightMotor = Motor.C;
        //	colorSensor = new EV3ColorSensor(SensorPort.S3);
        ev3 = (EV3) BrickFinder.getLocal();
        lcd = ev3.getTextLCD();
        ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);


        this.automate = automate;
    }
	
	/*

	public void test_avec_couleur() throws InterruptedException, RuntimeException {
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];

		// Permet de jaugé la couleur pour connaitre une intervalle
		float lowerBlack = 0.02f;
		float upperBlack = 0.10f;
		
		float lowerRed = 0.20f;
		float upperRed = 0.40f;
		
		float lowerWhite = 0.50f;
		float upperWhite = 0.80f;

		// leftMotor.backward(); // backward because of gears
		// rightMotor.backward();
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);

		while (true) {
			redMode.fetchSample(redSample, 0);

			// Affiche les couleurs
			lcd.clear();
			lcd.drawString(String.valueOf(redSample[0]), 1, 3);
              
			// La bonne direction
			if(detectObstacle()) {
				if (lowerBlack <= redSample[0] && redSample[0] <= upperBlack) {
					
					if(automate.getBlack().equals("left") ) {
					    if(isdoing) {
					    	leftMotor.stop();
							rightMotor.forward();
							Delay.msDelay(3000);
							isdoing = false;
					    }
						forward();
					}else if(automate.getBlack().equals("right")) {
						if(isdoing) {
							leftMotor.forward();
							rightMotor.stop();
							Delay.msDelay(3000);
							isdoing = false;
						}
						
						forward();
					}
					
				} else if(lowerWhite<= redSample[0] && redSample[0] <= upperWhite)  {
					// sync this one					
					if(automate.getWhite().equals("left")) {
						if(isdoing) {
							leftMotor.stop();
							rightMotor.forward();
							Delay.msDelay(3000);
							isdoing = false;
						}
					
						forward();
					}else if(automate.getWhite().equals("right")) {
						if(isdoing) {
							leftMotor.forward();
							rightMotor.stop();
							Delay.msDelay(3000);
							isdoing=false;
						}
						
						forward();
					}
				}else {
					isdoing = true;
					forward();
				}
			}else {
				contourObstacle();
			}
			
			Thread.sleep(50);
			if (Button.DOWN.isDown())
				throw new RuntimeException("Program stopped by user");
		}
	}
	*/

    public void execute() {
        // System.out.print(automate.getAutomate().get("1").getAction());
        execute_Action("1");
    }

    public void execute_Action(String next_etat) {
        if (automate.getAutomate().get(next_etat).getAction() == Action.STOP) return;
        MotorSync.startMotorsSync(Motor.B, Motor.C, automate.getAutomate().get(next_etat).getAction(), automate.getAutomate().get(next_etat).getTemps());
        Delay.msDelay(500);
        execute_Action(automate.getAutomate().get(next_etat).getEtat_Destination());
    }

    public boolean detectObstacle() {


        SampleProvider distanceProvider = ultrasonicSensor.getMode("Distance");

        float[] distanceSample = new float[distanceProvider.sampleSize()];

        float distanceSeuil = 0.15f;

        // Obtenir la distance mesurée
        distanceProvider.fetchSample(distanceSample, 0);

        // La distance est stockée dans le premier élément du tableau

        float distance = distanceSample[0];

        // Afficher la distance mesurée
        //  System.out.println("Distance: " + distance + " m");

        return distance > distanceSeuil;
    }

    public void contourObstacle() {
        leftMotor.stop();
        rightMotor.stop();
    }

    public void forward() {

        leftMotor.setSpeed(100);
        rightMotor.setSpeed(100);

        leftMotor.forward();
        rightMotor.forward();
    }
}
