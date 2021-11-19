package com.remoteev3.app.di;

import android.app.Application;
import com.remoteev3.app.db.TaskDao;
import com.remoteev3.app.db.TaskDatabase;
import javax.inject.Singleton;
import androidx.room.Room;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    TaskDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, TaskDatabase.class,"task.db").build();
    }

    @Singleton
    @Provides
    static TaskDao provideTaskDao(TaskDatabase database) {
        return database.taskDao();
    }

}
















