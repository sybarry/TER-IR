package com.remoteev3.app;


import android.os.Bundle;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import com.remoteev3.app.db.TaskRepository;
import com.remoteev3.app.network.BluetoothService;

import dagger.android.support.DaggerAppCompatActivity;


public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    public BluetoothService bluetoothService;

    @Inject
    public TaskRepository taskRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}















