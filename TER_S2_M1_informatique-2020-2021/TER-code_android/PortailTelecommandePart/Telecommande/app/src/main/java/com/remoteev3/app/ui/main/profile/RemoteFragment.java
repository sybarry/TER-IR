package com.remoteev3.app.ui.main.profile;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.remoteev3.app.R;
import com.remoteev3.app.util.DebouncedOnClickListener;
import com.remoteev3.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import dagger.android.support.DaggerFragment;

public class RemoteFragment extends DaggerFragment {

    private static final String TAG = "ProfileFragment";

    private RemoteViewModel viewModel;
    private Button buttonOuverturePartielle;
    private Button buttonOuvertureTotale;
    private Button buttonFermeture;
    private long mLastClickTime = 0;


    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ProfileFragment. " + this);
        viewModel = ViewModelProviders.of(this, providerFactory).get(RemoteViewModel.class);
        initButtons(view);
    }

    private void initButtons(View view) {
        buttonOuverturePartielle = view.findViewById(R.id.buttonOuverturePartielle);
        buttonOuverturePartielle.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertPartialOpenTask();
            }
        });
        buttonOuvertureTotale = view.findViewById(R.id.buttonOuvertureTotale);
        buttonOuvertureTotale.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTotalOpenTask();
            }
        });
        buttonFermeture = view.findViewById(R.id.buttonFermeture);
        buttonFermeture.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertCloseTask();
            }
        });
    }
}




















