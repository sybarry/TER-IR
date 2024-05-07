package pilottageColorSensor;

import lejos.utility.Delay;

/**
 * Main class for the color sensor piloting.
 */
public class Main {

    public static void main(String[] args) {
        ConduiteByColor m = new ConduiteByColor();

        try {
            m.test_avec_couleur();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Program stopped.");
        } finally {
            Delay.msDelay(1500);
        }

        System.exit(0);
    }
}
