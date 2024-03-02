package androidproject.applicationlejosev3.panel;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;

import java.util.Arrays;
import java.util.List;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.BluetoothService;
import androidproject.applicationlejosev3.utils.Device;
import androidproject.applicationlejosev3.utils.Utils;
import de.nitri.gauge.Gauge;

public class ControlPageActivity extends AppCompatActivity {
    BluetoothService BTConnect;
    int currentSpeed = 0;
    ImageButton btnGauche, btnDroite;
    Button btnExit, btnStop, btnStatus, btnReculer, btnAvancer;
    Gauge gauge;
    DigitSpeedView digit;
    AppCompatSeekBar vitesse;
    TextView txtStatus;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    connectionCut();
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    connectionActive();
                    break;
            }
        }
    };
    boolean registed = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);
        btnAvancer = findViewById(R.id.buttonA);
        btnReculer = findViewById(R.id.buttonR);
        btnGauche = findViewById(R.id.buttonG);
        btnDroite = findViewById(R.id.buttonD);
        btnExit = findViewById(R.id.buttonQuit);
        btnStop = findViewById(R.id.buttonS);
        gauge = findViewById(R.id.gauge);
        vitesse = findViewById(R.id.seek);
        vitesse.incrementProgressBy(10);
        digit = findViewById(R.id.digitView);
        btnStatus = findViewById(R.id.btnStatus);
        txtStatus = findViewById(R.id.txtStatus);
        String authorize = getString(R.string.bt_disabled);
        String fail = getString(R.string.bt_failed);

        // Toast pour avertir l'utilisateur
        Device device = getIntent().getParcelableExtra("mac");
        BTConnect = new BluetoothService(this, device);

        if (!BTConnect.connectToDevice(device)) {
            Utils.toast(this, fail);
            finish();
        }
        else if (!BluetoothService.checkBTPermissions(this, this)) {
            Utils.toast(this, authorize);
            finish();
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registed = true;

        vitesse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    try {
                        BTConnect.writeMessage(7);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed = 0;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                } else if (i > currentSpeed) {
                    try {
                        BTConnect.writeMessage(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed += 10;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                } else if (i < currentSpeed) {
                    try {
                        BTConnect.writeMessage(6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentSpeed -= 10;
                    gauge.setValue(currentSpeed);
                    digit.updateSpeed(currentSpeed);
                }

                seekBar.setProgress(currentSpeed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnAvancer.setOnClickListener((view) -> {
            if (currentSpeed == 0) {
                currentSpeed = 10;
                digit.updateSpeed(currentSpeed);
                gauge.setValue(currentSpeed);
                vitesse.setProgress(currentSpeed);
            }
            try {
                BTConnect.writeMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnReculer.setOnClickListener((view) -> {
            try {
                BTConnect.writeMessage(2);
                if (currentSpeed == 0) {
                    currentSpeed = 10;
                    digit.updateSpeed(currentSpeed);
                    gauge.setValue(currentSpeed);
                    vitesse.setProgress(currentSpeed);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnGauche.setOnClickListener((view) -> {
            try {
                BTConnect.writeMessage(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnDroite.setOnClickListener((view) -> {
            try {
                BTConnect.writeMessage(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnStop.setOnClickListener((view) -> {
            try {
                BTConnect.writeMessage(7);
                currentSpeed = 0;
                digit.updateSpeed(currentSpeed);
                gauge.setValue(currentSpeed);
                vitesse.setProgress(currentSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        btnExit.setOnClickListener((view) -> finish());
    }

    private void connectionCut() {
        btnStatus.setBackgroundColor(Color.RED);
        txtStatus.setText(R.string.disconnected);
        List<View> views = Arrays.asList(btnAvancer, btnReculer, btnGauche, btnDroite, btnStop, btnExit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            views.forEach(view -> view.setEnabled(false));
    }

    private void connectionActive() {
        btnStatus.setBackgroundColor(Color.GREEN);
        txtStatus.setText(R.string.connected);
        List<View> views = Arrays.asList(btnAvancer, btnReculer, btnGauche, btnDroite, btnStop, btnExit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            views.forEach(view -> view.setEnabled(true));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        BTConnect.disconnect();
        if (registed)
            unregisterReceiver(mReceiver);

        super.onDestroy();
    }

}
