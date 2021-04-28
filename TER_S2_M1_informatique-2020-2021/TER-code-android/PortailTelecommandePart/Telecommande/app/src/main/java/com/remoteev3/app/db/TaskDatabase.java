package com.remoteev3.app.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.remoteev3.app.domain.Task;

@Database(entities = {Task.class}, version = 2)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
}