package com.remoteev3.app.ui.main.profile;

import com.remoteev3.app.network.BluetoothService;
import com.remoteev3.app.db.TaskRepository;
import com.remoteev3.app.domain.Task;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteViewModel extends ViewModel {

    private final BluetoothService bluetoothService;
    private final TaskRepository taskRepository;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Inject
    public RemoteViewModel(BluetoothService bluetoothService, TaskRepository taskRepository) {
        this.bluetoothService = bluetoothService;
        this.taskRepository = taskRepository;
    }

    public void insertPartialOpenTask() {
        try {
            this.bluetoothService.writeMessage((byte) 1);
            this.taskRepository.insert(new Task("Ouverture partielle", dateFormat.format(new Date()), "", 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertTotalOpenTask() {
        try {
            this.bluetoothService.writeMessage((byte) 2);
            this.taskRepository.insert(new Task("Ouverture totale", dateFormat.format(new Date()), "", 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertCloseTask() {
        try {
            this.bluetoothService.writeMessage((byte) 3);
            this.taskRepository.insert(new Task("Fermeture", dateFormat.format(new Date()), "", 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



















