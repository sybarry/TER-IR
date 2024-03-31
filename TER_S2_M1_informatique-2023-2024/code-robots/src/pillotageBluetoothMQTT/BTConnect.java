package pillotageBluetoothMQTT;

import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static pillotageBluetoothMQTT.MainMQTT_BT.ctrl;

public class BTConnect implements Runnable {
    /** MAC address du robot */
    static final String MAC = BrickFinder.getLocal().getBluetoothDevice().getBluetoothAddress();

    /** Méthode pour connecter le robot à un client bluetooth.
     * La methode est bloquante, elle attend la connexion d'un client bluetooth <br>
     * @throws IOException si une erreur d'entrée/sortie survient
     * */
    public void connect() throws IOException {
        System.out.println("Waiting for bluetooth client");
        NXTCommConnector connector = Bluetooth.getNXTCommConnector();
        NXTConnection connection = connector.waitForConnection(0, NXTConnection.RAW);
        DataInputStream in = connection.openDataInputStream();
        DataOutputStream out = connection.openDataOutputStream();
        System.out.println("Client connected");

        /** Boucle pour lire les commandes envoyées par le client et les exécuter.
         * Avant de lire une commande, on envoie la vitesse actuelle des moteurs au client. <br>
         * Au cas où le client se déconnecte, on sort de la boucle et la connexion bluetooth (le thread) est fermée. <br>
         * **/
        while (true) {
            out.write(ctrl.sendPayloadAsArray(ctrl.getActualState().getValue()));
            out.flush();
            try{
                byte[] input = new byte[2];
                in.readFully(input);
                if (!MainMQTT_BT.BT_disconnected)
                    executeCommand(ctrl, input);
            } catch (EOFException e) {
                in.close();
                break;
            }
        }

        System.out.println("Bluetooth disconnected");
    }

    /** Méthode pour exécuter une commande envoyée par le client.
     * Les commandes sont des entiers entre 1 et 7. <br>
     * @param ctrl le controlleur du robot
     * @param input la commande à exécuter
     * **/
    private void executeCommand(Controller ctrl, byte[] input) {
        switch (input[0]) {
            case 1:
                ctrl.movingForward();
                break;
            case 2:
                ctrl.movingBackward();
                break;
            case 3:
                ctrl.turnLeft();
                break;
            case 4:
                ctrl.turnRight();
                break;
            case 5:
                ctrl.applySpeed(input[1] * 10);
                break;
            case 7:
                ctrl.stop();
                break;
        }
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}