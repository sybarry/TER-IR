package com.remoteev3.app.di.connection;

import com.remoteev3.app.di.ViewModelKey;
import com.remoteev3.app.ui.connection.ConnectionViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ConnectionViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ConnectionViewModel.class)
    public abstract ViewModel bindConnectionViewModel(ConnectionViewModel viewModel);
}
