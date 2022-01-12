package com.remoteev3.app.ui.connection;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import ConnectionCommunication.ConnectionCommunicationBTAndroidClient;

public class ConnectionViewModel extends ViewModel {

    private final ConnectionCommunicationBTAndroidClient bluetoothServiceSurcouche;

    @Inject
    public ConnectionViewModel(ConnectionCommunicationBTAndroidClient bluetoothService) {
        this.bluetoothServiceSurcouche = bluetoothService;
    }

    public Boolean connectWithMac(String mac) {
        bluetoothServiceSurcouche.setMacAdd(mac);
        bluetoothServiceSurcouche.openConnection();
        return true;
    }
}









