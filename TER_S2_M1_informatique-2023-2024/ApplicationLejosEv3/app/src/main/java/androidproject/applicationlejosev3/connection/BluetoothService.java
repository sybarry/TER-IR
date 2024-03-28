package androidproject.applicationlejosev3.connection;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidproject.applicationlejosev3.utils.Device;
import androidproject.applicationlejosev3.utils.Utils;

/** Classe qui gère la connexion Bluetooth avec l'EV3
 *  Elle permet de se connecter à un périphérique, d'envoyer des commandes et de recevoir des données
 *  Elle permet aussi de récupérer la liste des périphériques appairés
 */
@SuppressLint("MissingPermission")
public class BluetoothService {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    BluetoothAdapter localAdapter;
    private BluetoothSocket socket;
    private byte[] output = new byte[2];

    /** Constructeur 1 de la classe
     * Initialise le Bluetooth
     * @param context : contexte de l'application
     */
    public BluetoothService(Context context){
        localAdapter = initBT(context);
    }

    /** Constructeur 2 de la classe
     * Initialise le Bluetooth et crée un socket pour se connecter à un périphérique
     * @param context : contexte de l'application
     * @param d : périphérique auquel on veut se connecter
     */
    public BluetoothService(Context context, Device d) {
        localAdapter = initBT(context);
        BluetoothSocket tmp = null;
        BluetoothDevice device = localAdapter.getRemoteDevice(d.getMacAddress());
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
        } catch (IOException e) {
            Utils.toast(context, "Erreur de connexion au périphérique");
            e.printStackTrace();
        }
        socket = tmp;
    }

    /** Méthode qui initialise le Bluetooth
     * @param context : contexte de l'application
     * @return : l'adaptateur Bluetooth
     */
    public BluetoothAdapter initBT(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            BluetoothManager localBluetoothManager = context.getSystemService(BluetoothManager.class);
            assert localBluetoothManager != null;
            localAdapter = localBluetoothManager.getAdapter();
        } else localAdapter = BluetoothAdapter.getDefaultAdapter();
        return localAdapter;
    }


    /** Méthode qui retourne la liste des périphériques appairés depuis les paramètres Bluetooth de l'appareil
     * @return : la liste des périphériques appairés
     */
    public List<Device> getPairedBluetoothDevices() {
        Set<BluetoothDevice> pairedDevices = localAdapter.getBondedDevices();
        List<Device> devices = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pairedDevices.stream()
                    .map(device -> new Device(device.getName(), device.getAddress()))
                    .forEach(devices::add);
        }
        return devices;
    }

    /** Méthode qui se connecte à un périphérique
     * @param EV3 : périphérique auquel on veut se connecter
     * @return : true si la connexion a réussi, false sinon
     */
    public boolean connectToDevice(Device EV3){
        BluetoothDevice device = localAdapter.getRemoteDevice(EV3.getMacAddress());
        BluetoothSocket tmp;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
            socket = tmp;
            socket.connect();
            return true;
        } catch (IOException e) {
            Log.e("ConnectBT", "Unable to connect to device. " + e.getMessage());
            return false;
        }
    }

    /** Méthode qui se déconnecte du périphérique
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e("ConnectThread", "Unable to close socket", e);
            e.printStackTrace();
        }
    }


    /** Méthode qui envoie une commande au périphérique
     * @param overall_speed : vitesse globale du robot à envoyer
     * @param action : action à envoyer
     */
    public void sendCommand(Integer action, @Nullable Integer overall_speed){
        output[0] = action.byteValue();
        if (overall_speed != null)
            output[1] = overall_speed.byteValue();
        BluetoothSocket connSock = socket;
        if(connSock != null){
            try {
                OutputStream out = connSock.getOutputStream();
                out.write(output);
                out.flush();
                Thread.sleep(250); // wait for the message to be treated by EV3
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Méthode qui reçoit la vitesse du robot
     * La vitesse est reçue sous forme de tableau d'entiers et multipliée par 10
     * @throws IOException : exception si la connexion est perdue
     * @return : la collection de vitesses reçues (contenant 2 vitesses pour les 2 moteurs)
     */
    public int[] receiveSpeed() throws IOException {
        BluetoothSocket connSock = socket;
        if (!connSock.isConnected())
            return null;

        InputStream in = connSock.getInputStream();
        byte[] buffer = new byte[1024]; // adjust the size as needed
        int bytesRead = in.read(buffer);
        if (bytesRead == -1)
            return null;
        byte[] result = new byte[bytesRead];
        System.arraycopy(buffer, 0, result, 0, bytesRead);
        int[] intResult = new int[bytesRead];
        for (int i = 0; i < bytesRead; i++) {
            if (i<2){
                intResult[i] = Integer.parseInt(String.valueOf(result[i])) * 10;
            } else {
                intResult[i] = Integer.parseInt(String.valueOf(result[i]));
            }
        }
        return intResult;
    }

    /** Méthode qui vérifie si l'application a les permissions pour utiliser le Bluetooth
     * @param context : contexte de l'application
     * @param activity : activité de l'application
     * @return : true si l'application a les permissions, false sinon
     */
    public static boolean checkBTPermissions(Context context, Activity activity) {
        boolean bluetoothAvailable = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
        if (!bluetoothAvailable) {
            Utils.toast(context, "Bluetooth not available on this device");
            return false;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH}, 1);
        return false;
    }

    /** Méthode qui retourne le nom de l'appareil
     * @return : le nom de l'appareil
     */
    public static String getNameOfDevice(){
        return BluetoothAdapter.getDefaultAdapter().getName();
    }

}
