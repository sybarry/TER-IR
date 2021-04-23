package com.remoteev3.app.ui.main.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private SeekBar vitesse;
    private TextView vitesseindicateur;
    private ImageButton tournerDroite;
    private ImageButton tournerGauche;
    private ImageButton klaxon;
    private ImageButton avancer;
    private ImageButton reculer;
    private Button arret;
    private Button frein;
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

    @SuppressLint("ClickableViewAccessibility")
    private void initButtons(View view) {
        this.avancer = (ImageButton) view.findViewById(R.id.avancer);

        avancer.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(1,"Avancer");
            }
        });

        this.reculer = (ImageButton) view.findViewById(R.id.reculer);
        reculer.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(2,"Reculer");
            }
        });

        this.tournerDroite = (ImageButton) view.findViewById(R.id.tourneDroite);
        tournerDroite.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(16,"Tourne à droite");
            }
        });

        this.tournerGauche = (ImageButton) view.findViewById(R.id.tourneGauche);
        tournerDroite.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(17,"Tourne à gauche");
            }
        });

        this.frein = (Button) view.findViewById(R.id.frein);
        frein.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(3,"Freinner");
            }
        });

        this.klaxon = (ImageButton) view.findViewById(R.id.klaxon);
        this.klaxon.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(18,"Klaxonne");
            }
        });
/*
        this.vitesse = (SeekBar) view.findViewById(R.id.vitesse);
        vitesse.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertCloseTask();
            }
        });

        this.vitesseindicateur = (TextView) view.findViewById(R.id.vitesseindicateur);
        vitesseindicateur.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertCloseTask();
            }
        });
*/
        this.arret = (Button) view.findViewById(R.id.arret);
        this.arret.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                viewModel.insertTask(15,"Arret de l'application");
            }
        });
    }
}




















