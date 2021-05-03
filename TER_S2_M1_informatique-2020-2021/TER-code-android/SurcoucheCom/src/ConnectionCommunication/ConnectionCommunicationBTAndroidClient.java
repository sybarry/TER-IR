package com.remoteev3.app.ConnectionCommunication;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.remoteev3.app.Divers.InfoConnection;

import javax.inject.Inject;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class ConnectionCommunicationBTAndroidClient extends AConnectionCommunication {

	private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    	private BluetoothAdapter localAdapter;
    	private BluetoothSocket socket_ev3;
    	private BluetoothDevice ev3;
    	private String macAdd; // The mac address of the server you want to connect to

	/*
	 * Create an instance for a Bluetooth client android
	 * 
	 * @param macAdd The mac address of the server you want to connect to
	 */
    	public ConnectionCommunicationBTAndroidClient(String macAdd) {
		this.macAdd = macAdd;
		this.dOut = null;
		this.dIn = null;
		this.socket_ev3 = null;
		this.ev3 = null;
        	initBT();
    	}

	/*
	 * Create an instance for a Bluetooth client android
	 */
	@Inject
        public ConnectionCommunicationBTAndroidClient() {
		this.macAdd = "";
		this.dOut = null;
		this.dIn = null;
		this.socket_ev3 = null;
		this.ev3 = null;
		initBT();
	}

	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}

	/*
	 * Initializes the bluetooth
	 * 
	 * @return True if the Bluetooth is initialized
	 */
	private boolean initBT() {
        	localAdapter = BluetoothAdapter.getDefaultAdapter();
        	return localAdapter.isEnabled();
    	}

    	@Override
    	public void openConnection() throws IOException {
    	    this.ev3 = localAdapter.getRemoteDevice(macAdd);
    	    try {
    	        socket_ev3 = ev3.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
    	        socket_ev3.connect();
    	        Log.d("BLUETOOTH: ", "" + socket_ev3.isConnected());

    	        dOut = new DataOutputStream(socket_ev3.getOutputStream());
    	        dIn = new DataInputStream(socket_ev3.getInputStream());
	            String nameSender = localAdapter.getName();
	            String nameReceiver = ev3.getName();
		           infoConnection = new InfoConnection(nameSender, nameReceiver);
	        } catch (IOException e) {
	            Log.d("Bluetooth", "Err: Device not found or cannot connect " + macAdd);
	        }
	}

  	@Override
  	public void closeConnection() throws IOException {
  		try {
       		    socket_ev3.close();
        	} catch (IOException e) {
        	    e.printStackTrace();
        	}
   	}	
}
