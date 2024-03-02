package androidproject.applicationlejosev3.connection;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidproject.applicationlejosev3.utils.MQTTConfig;

public class MQTTService extends Service {
    private volatile boolean disconnected = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            String config_gson = intent.getStringExtra("config");
            MQTTConfig mqttConfig = new Gson().fromJson(config_gson, MQTTConfig.class);
            try {
                final String broker = "tcp://" + mqttConfig.getBroker() + ":1883";
                MqttClient client = new MqttClient(broker, mqttConfig.getClientID(), null);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setUserName(mqttConfig.getUsername());
                options.setPassword(mqttConfig.getPassword().toCharArray());
                client.connect(options);
                client.subscribe(mqttConfig.getTopic());
                client.setCallback(new ListenToMQTT());

                while (!disconnected){
                    Intent it = new Intent("MQTTStatus");
                    if (client.isConnected()) it.putExtra("status", "connected");
                    else it.putExtra("status", "disconnected");
                    sendBroadcast(it);
                    Thread.sleep(1500);
                }
            } catch (MqttException e) {
                Log.e("MQTT", "Error connecting to broker", e);
            } catch (InterruptedException e) {
                Log.e("MQTT", "Error sleeping", e);
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        disconnected = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ListenToMQTT implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            Log.e("MQTT", "Connection lost", cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            String msg = new String(message.getPayload());
            if (msg.equals("quit"))
                disconnected = true;

            // Share the message with other activities
            Intent intent = new Intent("MQTTMessage");
            intent.putExtra("topic", topic);
            intent.putExtra("message", new String(message.getPayload()));
            sendBroadcast(intent);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {}
    }

    public static MQTTConfig getConfigFromPreferences(SharedPreferences sharedPreferences) {
        return new MQTTConfig(
                sharedPreferences.getString("broker", ""),
                sharedPreferences.getString("topic", ""),
                sharedPreferences.getString("clientId", ""),
                sharedPreferences.getString("username", ""),
                sharedPreferences.getString("password", ""),
                sharedPreferences.getString("port", "")
        );
    }

    public static void saveConfigToPreferences(SharedPreferences sharedPreferences, MQTTConfig mqttConfig) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("broker", mqttConfig.getBroker());
        editor.putString("clientId", mqttConfig.getClientID());
        editor.putString("port", mqttConfig.getPort());
        editor.putString("topic", mqttConfig.getTopic());
        editor.putString("username", mqttConfig.getUsername());
        editor.putString("password", mqttConfig.getPassword());
        editor.apply();
    }

    public static boolean checkMQTTConnection(MQTTConfig config) {
        try {
            String broker = "tcp://" + config.getBroker() + ":" + config.getPort();
            MqttClient client = new MqttClient(broker, config.getClientID(), null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(config.getUsername());
            options.setPassword(config.getPassword().toCharArray());
            client.connect(options);
            client.disconnect();
            return true;
        } catch (Exception e) {
            Log.e("MQTT", "Error connecting to broker", e);
            return false;
        }
    }

    public void disconnect() {
        disconnected = true;
    }
}

