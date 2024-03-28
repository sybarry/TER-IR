package pillotageBluetoothMQTT;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTConnect {
    /** Credentials pour se connecter au broker MQTT */
    final String username = "ev3";
    final String password = "omelette";

    /** Constructeur pour la classe MQTTConnect.
     * @param broker_IP l'adresse IP du broker MQTT
     * @param clientID l'identifiant du client
     * @param topic le topic auquel le client doit s'abonner
     * @throws Exception si une erreur survient lors de la connexion au broker
     * **/
    public MQTTConnect(String broker_IP, final String clientID, String topic) throws Exception {
        final String broker = "tcp://" + broker_IP + ":1883";
        MqttClient client = new MqttClient(broker, clientID);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());

        client.connect(connOpts);
        System.out.println("Connected to broker: " + broker);
        client.subscribe(topic);
        client.setCallback(new listenToMQTT());

        /** Boucle infinie.
         * C'est pour garder le thread actif et écouter les messages du client. <br>
         * **/
        while (true)
            continue;
    }

    /** Classe pour écouter les messages du client MQTT.
     * La classe implémente l'interface MqttCallback. <br>
     * Si un message est reçu, on exécute la commande correspondante. <br>
     * **/
    private static class listenToMQTT implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("MQTT Connection lost: " + cause.getMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            try {
                String payload = new String(message.getPayload());
                switch (payload) {
                    case "go":
                        MainMQTT_BT.ctrl.movingForward();
                        break;
                    case "back":
                        MainMQTT_BT.ctrl.movingBackward();
                        break;
                    case "stop":
                        MainMQTT_BT.ctrl.stop();
                        break;
                    case "disconnect":
                        MainMQTT_BT.BT_disconnected = true;
                        break;
                    case "connect":
                        MainMQTT_BT.BT_disconnected = false;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }

}