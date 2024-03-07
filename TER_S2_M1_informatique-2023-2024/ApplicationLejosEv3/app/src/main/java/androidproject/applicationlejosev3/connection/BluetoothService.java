package androidproject.applicationlejosev3.connection;

import static android.support.v4.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidproject.applicationlejosev3.utils.Device;
import androidproject.applicationlejosev3.utils.Utils;

public class BluetoothService {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    BluetoothAdapter localAdapter;
    private BluetoothSocket socket;

    public BluetoothService(Context context){
        localAdapter = initBT(context);
    }

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

    public BluetoothAdapter initBT(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            BluetoothManager localBluetoothManager = context.getSystemService(BluetoothManager.class);
            assert localBluetoothManager != null;
            localAdapter = localBluetoothManager.getAdapter();
        } else localAdapter = BluetoothAdapter.getDefaultAdapter();
        return localAdapter;
    }

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

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e("ConnectThread", "Unable to close socket", e);
            e.printStackTrace();
        }
    }

    // Utile pour envoyer des messages à l'EV3 (pour les fonctionnalités)
    public void sendCommand(int speed){
        BluetoothSocket connSock = socket;
        if(connSock != null){
            try {
                OutputStreamWriter out = new OutputStreamWriter(connSock.getOutputStream());
                out.write(speed);
                out.flush();
                Thread.sleep(250); // wait for the message to be treated by EV3
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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
        for (int i = 0; i < bytesRead; i++)
            intResult[i] = Integer.parseInt(String.valueOf(result[i])) * 10;
        return intResult;
    }

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

    public static String getNameOfDevice(){
        return BluetoothAdapter.getDefaultAdapter().getName();
    }

}
