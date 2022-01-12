package com.remoteev3.app.ui.main.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.remoteev3.app.R;
import com.remoteev3.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import Exception.MessageException;

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
        avancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.insertTask(1,"Avancer");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            }
        });

        this.reculer = (ImageButton) view.findViewById(R.id.reculer);
        reculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.insertTask(2,"Reculer");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            }
        });

        this.tournerDroite = (ImageButton) view.findViewById(R.id.tourneDroite);
        tournerDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.insertTask(3,"Tourne à droite");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            }
        });

        this.tournerGauche = (ImageButton) view.findViewById(R.id.tourneGauche);
        tournerGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.insertTask(4,"Tourne à gauche");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            }
        });

        this.frein = (Button) view.findViewById(R.id.frein);
        frein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.insertTask(5,"Freiner");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessageException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initText(View view) {
        this.text = (TextView) view.findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                while(true){
                    String message = null;
                    try {
                        message = viewModel.insertTask("Réception du message");
                    } catch (IOException | MessageException e) {
                        e.printStackTrace();
                    }

                    final String finalMessage = message;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(finalMessage);
                        }
                    });
                }
            }
        }.start();
    }

}




















