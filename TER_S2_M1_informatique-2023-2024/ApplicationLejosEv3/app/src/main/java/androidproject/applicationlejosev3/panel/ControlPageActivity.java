package androidproject.applicationlejosev3.panel;

import static androidproject.applicationlejosev3.utils.Utils.toast;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;

import java.io.IOException;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.BluetoothService;
import androidproject.applicationlejosev3.utils.Device;

public class ControlPageActivity extends AppCompatActivity {
    BluetoothService BTConnect;
    int currentSpeed = 50;
    ImageButton btnGauche, btnDroite;
    Button btnExit, btnStop, btnReculer, btnAvancer, btnRetry;
    DigitSpeedView digitG, digitD;
    RelativeLayout rlmarkerG, rlmarkerD, rlmarkerGeneral;
    SeekBar vitesseLeft, vitesseRight, vitesseGeneral;
    TextView txtStatus, txtProgressGeneral;
    int step = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_page);
        btnAvancer = findViewById(R.id.buttonA);
        btnReculer = findViewById(R.id.buttonR);
        btnGauche = findViewById(R.id.buttonG);
        btnDroite = findViewById(R.id.buttonD);
        btnExit = findViewById(R.id.buttonQuit);
        btnStop = findViewById(R.id.buttonS);
        btnRetry = findViewById(R.id.buttonRetry);
        vitesseLeft = findViewById(R.id.seekGauche);
        vitesseRight = findViewById(R.id.seekDroite);
        vitesseGeneral = findViewById(R.id.seekGeneral);
        digitG = findViewById(R.id.digitGauche);
        digitD = findViewById(R.id.digitDroite);
        txtStatus = findViewById(R.id.txtStatus);
        rlmarkerG = findViewById(R.id.markerG);
        rlmarkerD = findViewById(R.id.markerD);
        rlmarkerGeneral = findViewById(R.id.markerGeneral);
        vitesseLeft.incrementProgressBy(step);
        txtProgressGeneral = rlmarkerGeneral.findViewById(R.id.tvProgress);
        digitD.updateSpeed(currentSpeed);
        digitG.updateSpeed(currentSpeed);

        // Toast pour avertir l'utilisateur
        Device device = getIntent().getParcelableExtra("mac");
        BTConnect = new BluetoothService(this, device);

        if (!BTConnect.connectToDevice(device)) {
            toast(this, "Impossible de se connecter à cet appareil");
            finish();
        }
        if (!BluetoothService.checkBTPermissions(this, this)) {
            toast(this, "Autorisez le bluetooth pour continuer");
            finish();
        }


        vitesseGeneral.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            private void adjustProgress(int progress) {
                if (progress % 50 == 0)
                    progressChangedValue = progress;
                else {
                    int lower = (progress / step) * step;
                    int upper = lower + step;
                    progressChangedValue = (progress - lower < upper - progress) ? lower : upper;
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustProgress(progress);
                txtProgressGeneral.setText(String.valueOf(progressChangedValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rlmarkerGeneral.setVisibility(View.VISIBLE);
                txtProgressGeneral.setText(String.valueOf(progressChangedValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adjustProgress(seekBar.getProgress());
                rlmarkerGeneral.setVisibility(View.INVISIBLE);

                if (progressChangedValue == 0) {
                    BTConnect.sendCommand(7);
                    currentSpeed = 0;
                }
                else if (progressChangedValue > currentSpeed)
                    BTConnect.sendCommand(5);
                else if (progressChangedValue < currentSpeed)
                    BTConnect.sendCommand(6);

                seekBar.setProgress(progressChangedValue);
            }
        });

        new Thread(listenToIncommingSpeed()).start();

        btnAvancer.setOnClickListener((view) -> BTConnect.sendCommand(1));
        btnReculer.setOnClickListener((view) -> BTConnect.sendCommand(2));
        btnGauche.setOnClickListener((view) -> BTConnect.sendCommand(3));
        btnDroite.setOnClickListener((view) -> BTConnect.sendCommand(4));
        btnStop.setOnClickListener((view) -> BTConnect.sendCommand(7));
        btnExit.setOnClickListener((view) -> finish());
        btnRetry.setOnClickListener((view) -> {
            if (BTConnect.connectToDevice(device)) {
                connectionEstablished();
                new Thread(listenToIncommingSpeed()).start();
            } else connectionCut();
        });

        setSeekBarChangeListener(vitesseLeft, rlmarkerG, digitG);
        setSeekBarChangeListener(vitesseRight, rlmarkerD, digitD);
    }


    @NonNull
    private Runnable listenToIncommingSpeed() {
        Runnable R = () -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    int[] speedData = BTConnect.receiveSpeed();
                    if (speedData == null)
                        break;
                    runOnUiThread(() -> {
                        vitesseLeft.setProgress(speedData[0]);
                        vitesseRight.setProgress(speedData[1]);
                        if (speedData[0] == speedData[1]) {
                            currentSpeed = speedData[0];
                            vitesseGeneral.setProgress(speedData[0]);
                            vitesseGeneral.setEnabled(true);
                        }
                        else vitesseGeneral.setEnabled(false);
                    });
                } catch (InterruptedException | IOException e) {
                    if (e.getMessage().trim().equals("bt socket closed, read return: -1".trim()))
                        break;
                }
            }
            runOnUiThread(this::connectionCut);
        };
        return R;
    }

    private void setSeekBarChangeListener(SeekBar seekBar, RelativeLayout rlmarker, DigitSpeedView digit) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue;
            TextView txtProgress = rlmarker.findViewById(R.id.tvProgress);

            private void adjustProgress(int progress) {
                if (progress % 50 == 0)
                    progressChangedValue = progress;
                else {
                    int lower = (progress / step) * step;
                    int upper = lower + step;
                    progressChangedValue = (progress - lower < upper - progress) ? lower : upper;
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustProgress(progress);
                txtProgress.setText(String.valueOf(progressChangedValue));
                digit.updateSpeed(progressChangedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rlmarker.setVisibility(View.VISIBLE);
                txtProgress.setText(String.valueOf(progressChangedValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adjustProgress(seekBar.getProgress());
                rlmarker.setVisibility(View.INVISIBLE);
                seekBar.setProgress(progressChangedValue);
            }
        });
    }

    private void connectionCut(){
        txtStatus.setText("Connexion perdue");
        txtStatus.setTextColor(getResources().getColor(R.color.red));
        btnAvancer.setEnabled(false);
        btnReculer.setEnabled(false);
        btnGauche.setEnabled(false);
        btnDroite.setEnabled(false);
        btnStop.setEnabled(false);
        vitesseLeft.setEnabled(false);
        vitesseRight.setEnabled(false);
        vitesseGeneral.setEnabled(false);
        btnRetry.setVisibility(View.VISIBLE);
    }

    private void connectionEstablished(){
        txtStatus.setText("Connexion établie");
        txtStatus.setTextColor(getResources().getColor(R.color.green));
        btnAvancer.setEnabled(true);
        btnReculer.setEnabled(true);
        btnGauche.setEnabled(true);
        btnDroite.setEnabled(true);
        btnStop.setEnabled(true);
        vitesseLeft.setEnabled(true);
        vitesseRight.setEnabled(true);
        vitesseGeneral.setEnabled(true);
        btnRetry.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        BTConnect.disconnect();
        super.onDestroy();
    }

}
