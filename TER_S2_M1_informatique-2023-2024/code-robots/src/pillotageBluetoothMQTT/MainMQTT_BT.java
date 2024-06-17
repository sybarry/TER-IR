package pillotageBluetoothMQTT;

import lejos.hardware.Button;

import java.io.IOException;

import static pillotageBluetoothMQTT.BTConnect.MAC;

public class MainMQTT_BT {
    // IP of the MQTT server, hebergée sur le cloud
    static final String MQTT_SERVER_IP = "141.145.203.36";
    //    private static final String MQTT_SERVER_IP = "192.168.0.188";
    private static final String clientId = "EV3_" + MAC;
    public static Controller ctrl;
    public static boolean BT_disconnected = false;

    public static void main(String[] args) throws IOException {
        try {
            ctrl = new Controller();
            // Bluetooth thread
            Thread T1 = new Thread(new BTConnect());

            // MQTT thread
            // (comment the instantiation and decomment the while loop to test only the Bluetooth connection)
            Runnable mqtt = new Runnable() {
                @Override
                public void run() {
                    try {
                        new MQTTConnect(MQTT_SERVER_IP, clientId, "ev3/topic");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread T2 = new Thread(mqtt);

            /*
             Starts the threads and waits for one of them to finish.
             if button DOWN is pressed, the program is terminated.
             */
            T1.start();
            T2.start();
            while (T1.isAlive() && T2.isAlive()) {
                if (Button.DOWN.isDown())
                    exitProgram();
                Thread.sleep(100);
            }

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }

        exitProgram();
    }


    // Terminates the program
    static void exitProgram() {
        System.exit(1);
    }
}
