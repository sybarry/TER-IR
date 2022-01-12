package com.remoteev3.app.ui.main.profile;

import android.util.Log;

import com.remoteev3.app.db.TaskRepository;
import com.remoteev3.app.domain.Task;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ConnectionCommunication.ConnectionCommunicationBTAndroidClient;
import Message.IMessage;
import Message.MessageInt;
import Exception.MessageException;
import Message.MessageString;

public class RemoteViewModel extends ViewModel {

    private final ConnectionCommunicationBTAndroidClient bluetoothServiceSurcouche;
    private final TaskRepository taskRepository;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Inject
    public RemoteViewModel(ConnectionCommunicationBTAndroidClient bluetoothService, TaskRepository taskRepository) {
        this.bluetoothServiceSurcouche = bluetoothService;
        this.taskRepository = taskRepository;
    }

    public void insertTask(int byteToSend, String task) throws IOException, MessageException {
        this.bluetoothServiceSurcouche.sendMessage(new MessageInt(byteToSend));
        this.taskRepository.insert(new Task(task, dateFormat.format(new Date()), "", 1));
    }

    public String insertTask(String task) throws IOException, MessageException {
        IMessage message = this.bluetoothServiceSurcouche.receiveMessage();
        this.taskRepository.insert(new Task(task.concat(" \""+message+"\""),dateFormat.format(new Date()), "", 1));
        return message.getMessage().toString();
    }
}



















