package androidproject.applicationlejosev3.connection;

import static androidproject.applicationlejosev3.utils.Utils.toast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.utils.AsyncOperation;
import androidproject.applicationlejosev3.utils.MQTTConfig;

public class MQTTConfigurationActivity extends AppCompatActivity {
    Button btnSave, btnCheck;
    ProgressBar PB;
    TextInputEditText broker, topic, username, password, clientId, port;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_configuration);
        btnSave = findViewById(R.id.btnSave);
        btnCheck = findViewById(R.id.btnCheck);
        broker = findViewById(R.id.editBroker);
        topic = findViewById(R.id.editTopic);
        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        clientId = findViewById(R.id.editClientID);
        port = findViewById(R.id.editPort);
        PB = findViewById(R.id.progressBar);
        TextInputEditText[] form_fields = {broker, topic, username, password, clientId, port};
        TextInputEditText[] must_not_be_empty_fields = {broker, topic, clientId, port};
        sharedPreferences = getSharedPreferences("MQTT", MODE_PRIVATE);
        loadPreferencesToForm(sharedPreferences, form_fields);

        if (clientId.getText().toString().isEmpty())
            clientId.setText("Android_" + BluetoothService.getNameOfDevice());
        if (port.getText().toString().isEmpty())
            port.setText("1883");

        btnSave.setOnClickListener(view -> {
            checkFields(must_not_be_empty_fields);
            MQTTConfig mqttConfig = new MQTTConfig(
                    broker.getText().toString(),
                    topic.getText().toString(),
                    clientId.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString(),
                    port.getText().toString()
            );
            saveConfig(mqttConfig);
        });

        btnCheck.setOnClickListener(view -> {
            MQTTConfig config = new MQTTConfig(
                    broker.getText().toString(),
                    topic.getText().toString(),
                    clientId.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString(),
                    port.getText().toString());
            new AsyncOperation(PB, config, this).execute();
        });
    }

    private void loadPreferencesToForm(SharedPreferences sharedPreferences, TextInputEditText[] formControls) {
        MQTTConfig config = MQTTService.getConfigFromPreferences(sharedPreferences);

        formControls[0].setText(config.getBroker());
        formControls[1].setText(config.getTopic());
        formControls[2].setText(config.getUsername());
        formControls[3].setText(config.getPassword());
        formControls[4].setText(config.getClientID());
        formControls[5].setText(config.getPort());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void saveConfig(MQTTConfig mqttConfig) {
        MQTTService.saveConfigToPreferences(sharedPreferences, mqttConfig);
        toast(this, "Changes saved");
    }

    private void checkFields(TextInputEditText[] fields) {
        for (TextInputEditText control : fields) {
            if (TextUtils.isEmpty(control.getText())) {
                toast(this, "Field " + control.getHint() + " cannot be empty");
                return;
            }
        }
    }

}
