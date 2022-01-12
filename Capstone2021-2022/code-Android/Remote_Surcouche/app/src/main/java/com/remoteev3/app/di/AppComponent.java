package com.remoteev3.app.di;

import android.app.Application;

import com.remoteev3.app.BaseApplication;
import com.remoteev3.app.network.BluetoothService;

import javax.inject.Inject;
import javax.inject.Singleton;

import ConnectionCommunication.ConnectionCommunicationBTAndroidClient;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    ConnectionCommunicationBTAndroidClient bluetoothServiceSurcouche();

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}







