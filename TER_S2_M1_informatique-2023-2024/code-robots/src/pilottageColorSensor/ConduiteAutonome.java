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

import java.util.ArrayList;

/**
 * Classe permettant de piloter le robot depuis un automate
 */
public class ConduiteAutonome {
    private static final EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
    private NXTRegulatedMotor leftMotor, rightMotor;
    private EV3ColorSensor colorSensor;
    private final EV3 ev3;
    private final TextLCD lcd;
    private final Automate automate;
    private final Boolean isdoing = true;

	/**
	 * Constructeur de la classe ConduiteAutonome
	 * @param automate automate à utiliser
	 */
    public ConduiteAutonome(Automate automate) {
        ev3 = (EV3) BrickFinder.getLocal();
        lcd = ev3.getTextLCD();
        this.automate = automate;
    }

	/**
	 * Méthode permettant de piloter le robot en fonction d'une chaine de caractères
	 *
	 * @param mots chaine de caractères à utiliser
	 */
    public void execute(String mots) {
        ArrayList<Action> TabAction = new ArrayList<Action>();
        char[] charArray = mots.toCharArray();
        for (char c : charArray)
            TabAction.add(automate.Action("" + c));

        execute_Action("1", 0, TabAction);
    }

    /** Permet d'executer l'automate en entier jusqu'a tomber sur un etat final
     * chaque appel on lui passe l'etat suivant ainsi de suite.
	 *
     * @param next_etat etat suivant
	 * @param i indice de l'action
	 * @param action liste des actions
     * */
    public void execute_Action(String next_etat, int i, ArrayList<Action> action) {
        if (automate.getAutomate().get(next_etat).get(0).getAction() == Action.STOP) {
            if (i == action.size())
                System.out.print("Match");
            else System.out.print("Not Match");
            return;
        }

        if (automate.getAutomate().get(next_etat).size() > 1) {
            for (Action_Etat m : automate.getAutomate().get(next_etat)) {
                if (m.getAction() == action.get(i)) {
                    MotorSync.startMotorsSync(Motor.B, Motor.C, m.getAction(), m.getTemps());
                    Delay.msDelay(500);
                    execute_Action(m.getEtat_Destination(), i + 1, action);
                    return;
                }
            }
            System.out.print("Not Match");
        }
		else {
            if (automate.getAutomate().get(next_etat).get(0).getAction() == action.get(i)) {
                MotorSync.startMotorsSync(Motor.B, Motor.C, automate.getAutomate().get(next_etat).get(0).getAction(),
                        automate.getAutomate().get(next_etat).get(0).getTemps());
                Delay.msDelay(500);
                execute_Action(automate.getAutomate().get(next_etat).get(0).getEtat_Destination(), i + 1, action);
                return;
            }

            System.out.print("Not Match");
        }
    }

	/**
	 * Méthode permettant de detecter un obstacle à l'aide du capteur ultrasonique du robot (distance > 0.15m)
	 * @return true si un obstacle est détecté, false sinon
	 */
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

	/**
	 * Méthode permettant de contourner un obstacle, en arrêtant les moteurs
	 */
    public void contourObstacle() {
        leftMotor.stop();
        rightMotor.stop();
    }
}
