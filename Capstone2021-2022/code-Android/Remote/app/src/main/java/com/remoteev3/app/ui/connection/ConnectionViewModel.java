package com.remoteev3.app.ui.connection;

import com.remoteev3.app.network.BluetoothService;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import ConnectionCommunication.ConnectionCommunicationBTAndroidClient;

public class ConnectionViewModel extends ViewModel {

    /*private final BluetoothService bluetoothService;

    @Inject
    public ConnectionViewModel(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
    }

    public Boolean connectWithMac(String mac) {
        return bluetoothService.connectToEV3(mac);
    }*/

    private ConnectionCommunicationBTAndroidClient comBT;

    @Inject
    public ConnectionViewModel(ConnectionCommunicationBTAndroidClient comBT) {
        this.comBT = comBT;
    }

    public Boolean connectWithMac(String mac) {
        comBT = new ConnectionCommunicationBTAndroidClient(mac);
        return comBT != null;
    }
}









