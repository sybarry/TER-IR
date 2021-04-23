package com.remoteev3.app.di;

import com.remoteev3.app.di.connection.ConnectionModule;
import com.remoteev3.app.di.connection.ConnectionScope;
import com.remoteev3.app.di.connection.ConnectionViewModelsModule;
import com.remoteev3.app.di.main.MainFragmentBuildersModule;
import com.remoteev3.app.di.main.MainModule;
import com.remoteev3.app.di.main.MainScope;
import com.remoteev3.app.di.main.MainViewModelsModule;
import com.remoteev3.app.ui.connection.ConnectionActivity;
import com.remoteev3.app.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ConnectionScope
    @ContributesAndroidInjector(
            modules = {ConnectionViewModelsModule.class, ConnectionModule.class})
    abstract ConnectionActivity contributeConnectionActivity();


    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
