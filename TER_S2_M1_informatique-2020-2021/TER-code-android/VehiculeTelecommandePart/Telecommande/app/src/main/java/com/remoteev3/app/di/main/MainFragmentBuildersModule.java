package com.remoteev3.app.di.main;


import com.remoteev3.app.ui.main.tasks.TasksFragment;
import com.remoteev3.app.ui.main.profile.RemoteFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract RemoteFragment contributeRemoteFragment();

    @ContributesAndroidInjector
    abstract TasksFragment contributeTasksFragment();
}
