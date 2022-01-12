package com.remoteev3.app.ui.main.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dagger.android.support.DaggerFragment;

public class RemoteFragment extends DaggerFragment {


    private static final String TAG = "ProfileFragment";

    private RemoteViewModel viewModel;
    private ImageButton tournerDroite;
    private ImageButton tournerGauche;
    private ImageButton avancer;
    private ImageButton reculer;
    private Button frein;
    private TextView text;

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
        initText(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initButtons(View view) {
        this.avancer = (ImageButton) view.findViewById(R.id.avancer);
        this.reculer = (ImageButton) view.findViewById(R.id.reculer);
        this.tournerDroite = (ImageButton) view.findViewById(R.id.tourneDroite);
        this.tournerGauche = (ImageButton) view.findViewById(R.id.tourneGauche);
        this.frein = (Button) view.findViewById(R.id.frein);

        new Thread(){
            @Override
            public void run() {
                avancer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insertTask(1,"Avancer");
                    }
                });

                reculer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insertTask(2,"Reculer");
                    }
                });

                tournerGauche.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insertTask(4,"Tourne à gauche");
                    }
                });

                tournerDroite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insertTask(3,"Tourne à droite");
                    }
                });

                frein.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insertTask(5,"Freiner");
                    }
                });
            }
        }.start();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initText(View view) {
        this.text = (TextView) view.findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                while(true){
                    final String message = viewModel.insertTask("Réception du message");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<String> startMessage = Arrays.asList("start","go","3","2","1");
                            if(startMessage.contains(message)) {
                                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
                            } else {
                                text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                            }
                            text.setText(message);
                        }
                    });
                }
            }
        }.start();
    }

}