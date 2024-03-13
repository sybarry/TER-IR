package pillotageBluetoothMQTT;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Controller {
    State actualState = State.STOPPED;
    private Motor leftMotor;
    private Motor rightMotor;
    public int ratioLeft = 0;
    public int ratioRight = 0;
    private int initial_speed = 50;
    private int max_speed = 500;

    /** Initialisation de la vitesse des deux moteurs **/
    public Controller() {
        this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.C), initial_speed);
        this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.B), initial_speed);
        RegulatedMotor[] T = {this.rightMotor.getMotor()};
        leftMotor.getMotor().synchronizeWith(T);
    }

    // Les méthodes suivantes permettent de contrôler le robot

    /**
    Avancer le robot, si le robot est déjà en train d'avancer, il continue d'avancer. <br>
    Si le robot est en train de tourner à gauche ou à droite, il arrête de tourner et avance
     */
    public void movingForward() {
        leftMotor.getMotor().startSynchronization();
        if (actualState == State.STOPPED) {
            actualState = State.FORWARD;
            leftMotor.movingForward();
            rightMotor.movingForward();
        } else if (actualState == State.TURNING_LEFT) {
            actualState = State.FORWARD;
            ratioLeft = 0;
            ratioRight = 0;
            leftMotor.setActual_speed(rightMotor.getActual_speed());
        } else if (actualState == State.TURNING_RIGHT) {
            actualState = State.FORWARD;
            ratioRight = 0;
            ratioLeft = 0;
            rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioRight);
        } else if (actualState == State.BACKWARD) {
            actualState = State.FORWARD;
            leftMotor.movingForward();
            rightMotor.movingForward();
        } else System.out.println("No forward in this state");
        leftMotor.getMotor().endSynchronization();
    }

    /**
    * Reculer le robot: <br>
     * Si le robot est déjà en train de reculer, il continue de reculer. <br>
    * Si le robot est en train de tourner à gauche ou à droite, il arrête de tourner et recule
    **/
    public void movingBackward() {
        leftMotor.getMotor().startSynchronization();
        leftMotor.stop();
        rightMotor.stop();
        actualState = State.BACKWARD;
        ratioLeft = 0;
        ratioRight = 0;
        if (leftMotor.getActual_speed() > rightMotor.getActual_speed())
            leftMotor.setActual_speed(rightMotor.getActual_speed());
        else rightMotor.setActual_speed(leftMotor.getActual_speed());
        leftMotor.movingBackward();
        rightMotor.movingBackward();
        leftMotor.getMotor().endSynchronization();
    }

    /** Arrêter le robot **/
    public void stop() {
        if (actualState != State.STOPPED) {
            leftMotor.getMotor().startSynchronization();
            leftMotor.stop();
            rightMotor.stop();
            actualState = State.STOPPED;
            leftMotor.getMotor().endSynchronization();
        }
    }

    /**
    * Tourner à gauche: <br>
     * - Si le robot est en train d'avancer, il commence à tourner à gauche <br>
     * - Si le robot est déjà en train de tourner à gauche,
     * il continue de tourner à gauche avec un angle plus grand. L'angle de rotation est limité à 200 (3 appels) <br>
     * - Si le robot essaye de tourner à droite tout en tournant à gauche,
     * il reduit l'angle de rotation à gauche jusqu'à sa stabilisation vers l'avant
     **/
    public void turnLeft() {
        leftMotor.getMotor().startSynchronization();
        if (actualState == State.FORWARD) {
            actualState = State.TURNING_LEFT;
            ratioLeft += 50;
            rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
        }
        else if (actualState == State.TURNING_LEFT) {
            ratioLeft += 50;
            if (ratioLeft >= 200)
        		ratioLeft = 200;
            
            rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
        }
        else if (actualState == State.TURNING_RIGHT) {
            ratioRight -= 50;
            if (ratioRight <= 0) {
            	ratioRight = 0;
            	actualState = State.FORWARD;
            }
            leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
        }
        leftMotor.getMotor().endSynchronization();
    }

    /**
     * Tourner à droite: <br>
     * - Si le robot est en train d'avancer, il commence à tourner à droite <br>
     * - Si le robot est déjà en train de tourner à droite,
     * il continue de tourner à droite avec un angle plus grand. L'angle de rotation est limité à 200 (3 appels) <br>
     * - Si le robot essaye de tourner à gauche tout en tournant à droite,
     * il reduit l'angle de rotation à droite jusqu'à sa stabilisation vers l'avant
     */
    public void turnRight() {
        leftMotor.getMotor().startSynchronization();
        if (actualState == State.FORWARD) {
            actualState = State.TURNING_RIGHT;
            ratioRight += 50;
            leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
        } else if (actualState == State.TURNING_RIGHT) {
            ratioRight += 50;
            if (ratioRight >= 200)
                	ratioRight = 200;
            leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioRight);
        } else if (actualState == State.TURNING_LEFT) {
            ratioLeft -= 50;
            if (ratioLeft <= 0) {
            	ratioLeft = 0;
            	actualState = State.FORWARD;
            }
            rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioLeft);
        }
        leftMotor.getMotor().endSynchronization();
    }

    /** Accélérer le robot, la vitesse maximale est 500 **/
    public void accelerate(int value) {
        if (leftMotor.getActual_speed() + value <= max_speed) {
            leftMotor.getMotor().startSynchronization();
            leftMotor.speedUp(value);
            rightMotor.speedUp(value);
            leftMotor.getMotor().endSynchronization();
        } else System.out.println("Max speed reached");
    }

    /** Décélérer le robot, la vitesse minimale est 0 (Le robot s'arrête dans ce cas) **/
    public void decelerate(int value) {
        leftMotor.getMotor().startSynchronization();
        if (leftMotor.getActual_speed() - value >= 0) {
            leftMotor.speedDown(value);
            rightMotor.speedDown(value);
        } else if (leftMotor.getActual_speed() - value < 0) {
            leftMotor.stop();
            rightMotor.stop();
        } else System.out.println("Min speed reached");
        leftMotor.getMotor().endSynchronization();
    }

    /**
     * Récupérer la vitesse des deux moteurs sous forme de tableau de bytes
     * La vitesse est divisée par 10 pour réduire la taille du message à envoyer, les valeurs sont multipliées par 10 à la réception
     * @return tableau de bytes contenant la vitesse des deux moteurs
     * **/
    public byte[] getSpeedAsArray() {
        return new byte[]{
                (byte) (leftMotor.getActual_speed() / 10),
                (byte) (rightMotor.getActual_speed() / 10)
        };
    }

}