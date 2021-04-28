package com.remoteev3.app.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

@Singleton
public class BluetoothService {

    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    BluetoothAdapter localAdapter;
    BluetoothSocket socket_ev3;
    BluetoothDevice ev3;

    @Inject
    public BluetoothService() {
        initBT();
    }

    private boolean initBT() {
        localAdapter = BluetoothAdapter.getDefaultAdapter();
        return localAdapter.isEnabled();
    }

    public boolean connectToEV3(String macAdd) {
        this.ev3 = localAdapter.getRemoteDevice(macAdd);
        try {
            socket_ev3 = ev3.createRfcommSocketToServiceRecord(UUID
                    .fromString(SPP_UUID));
            socket_ev3.connect();
            Log.d("BLUETOOTH: ", "" + socket_ev3.isConnected());
            return true;
        } catch (IOException e) {
            Log.d("Bluetooth", "Err: Device not found or cannot connect " + macAdd);
            return false;
        }
    }

    public void writeMessage(byte msg) throws InterruptedException {
        BluetoothSocket connSock;
        connSock = socket_ev3;
        if (connSock != null) {
            try {
                OutputStreamWriter out = new OutputStreamWriter(connSock.getOutputStream());
                out.write(msg);
                out.flush();
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Error
        }
    }

    public void disconnect() {
        try {
            socket_ev3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

















