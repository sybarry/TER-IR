package com.remoteev3.app.ui.connection;

import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.remoteev3.app.R;
import com.remoteev3.app.ui.main.MainActivity;
import com.remoteev3.app.viewmodels.ViewModelProviderFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ConnectionActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ConnectionActivity";

    private ConnectionViewModel viewModel;

    private EditText macInput;
    private Button connectionButton;
    private ProgressBar progressBar;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        macInput = findViewById(R.id.mac_input);
        macInput.addTextChangedListener((macTextWatcher));
        connectionButton = findViewById((R.id.connection_button));
        connectionButton.setEnabled(false);
        connectionButton.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this, providerFactory).get(ConnectionViewModel.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connection_button:{
                attemptConnection();
                break;
            }
        }
    }

    private void attemptConnection() {
        if(!macInput.getText().toString().isEmpty()) {
            Boolean connectionState = viewModel.connectWithMac(macInput.getText().toString());
            if (connectionState) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(),"Connection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private TextWatcher macTextWatcher = new TextWatcher() {
        private final Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String macAddress = macInput.getText().toString();
            Matcher matcher = pattern.matcher(macAddress) ;
            connectionButton.setEnabled(matcher.matches());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
