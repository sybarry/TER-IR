package com.remoteev3.app.di.main;

import com.remoteev3.app.di.ViewModelKey;
import com.remoteev3.app.ui.main.tasks.TasksViewModel;
import com.remoteev3.app.ui.main.profile.RemoteViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RemoteViewModel.class)
    public abstract ViewModel bindRemoteViewModel(RemoteViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel.class)
    public abstract ViewModel bindTasksViewModel(TasksViewModel viewModel);
}




