package com.remoteev3.app.ui.main.tasks;

import com.remoteev3.app.db.TaskRepository;
import com.remoteev3.app.domain.Task;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TasksViewModel extends ViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    @Inject
    public TasksViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}




















