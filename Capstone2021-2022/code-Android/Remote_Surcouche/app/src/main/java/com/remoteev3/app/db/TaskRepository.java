package com.remoteev3.app.db;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.remoteev3.app.domain.Task;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    @Inject
    public TaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
        allTasks = taskDao.getAllTasks();
    }

    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAllTasks() {
        new DeleteAllTasksAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteAllTasks();
            return null;
        }
    }

}
