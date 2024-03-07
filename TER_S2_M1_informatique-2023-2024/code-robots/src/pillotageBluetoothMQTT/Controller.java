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

    public Controller() {
        this.leftMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.C), initial_speed);
        this.rightMotor = new Motor(new EV3LargeRegulatedMotor(MotorPort.B), initial_speed);
        RegulatedMotor[] T = {this.rightMotor.getMotor()};
        leftMotor.getMotor().synchronizeWith(T);
    }

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
        } else System.out.println("Must stop first");
        leftMotor.getMotor().endSynchronization();
    }

    public void movingBackward() {
        leftMotor.getMotor().startSynchronization();
        if (actualState == State.STOPPED) {
            leftMotor.movingBackward();
            rightMotor.movingBackward();
            actualState = State.BACKWARD;
        } else if (actualState == State.TURNING_LEFT) {
            leftMotor.setActual_speed(rightMotor.getActual_speed() + ratioLeft);
            actualState = State.BACKWARD;
        } else if (actualState == State.TURNING_RIGHT) {
            rightMotor.setActual_speed(leftMotor.getActual_speed() + ratioRight);
            actualState = State.BACKWARD;
        } else System.out.println("Must stop first");
        leftMotor.getMotor().endSynchronization();
    }

    public void stop() {
        leftMotor.getMotor().startSynchronization();
        leftMotor.stop();
        rightMotor.stop();
        actualState = State.STOPPED;
        leftMotor.getMotor().endSynchronization();
    }

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

    public void accelerate(int value) {
        if (leftMotor.getActual_speed() + value <= max_speed) {
            leftMotor.getMotor().startSynchronization();
            leftMotor.speedUp(value);
            rightMotor.speedUp(value);
            leftMotor.getMotor().endSynchronization();
        } else System.out.println("Max speed reached");
    }

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

    public byte[] getSpeedAsArray() {
        return new byte[]{
                (byte) (leftMotor.getActual_speed() / 10),
                (byte) (rightMotor.getActual_speed() / 10)
        };
    }

}