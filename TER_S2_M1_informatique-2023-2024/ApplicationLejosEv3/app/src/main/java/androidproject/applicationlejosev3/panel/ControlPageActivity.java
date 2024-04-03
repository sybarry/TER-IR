package androidproject.applicationlejosev3.panel;

import static androidproject.applicationlejosev3.utils.Utils.toast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;

import java.io.IOException;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.BluetoothService;
import androidproject.applicationlejosev3.utils.Device;
import eo.view.batterymeter.BatteryMeterView;

public class ControlPageActivity extends AppCompatActivity {
    BluetoothService BTServ;
    int currentSpeed, step = 50;
    ImageButton btnGauche, btnDroite, btnClear;
    Button btnExit, btnStop, btnReculer, btnAvancer, btnRetry;
    DigitSpeedView digitG, digitD;
    RelativeLayout rlmarkerGeneral;
    SeekBar vitesseGeneral;
    BatteryMeterView batteryView;
    TextView txtConnectionStatus, txtProgressGeneral, txtStatus;
    boolean obstacleDetected = false;
    int volt = 0;

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
        vitesseGeneral = findViewById(R.id.seekGeneral);
        digitG = findViewById(R.id.digitGauche);
        digitD = findViewById(R.id.digitDroite);
        txtConnectionStatus = findViewById(R.id.txtConnectionStatus);
        txtStatus = findViewById(R.id.txtStatus);
        btnClear = findViewById(R.id.btnClear);
        rlmarkerGeneral = findViewById(R.id.markerGeneral);
        txtProgressGeneral = rlmarkerGeneral.findViewById(R.id.tvProgress);
        batteryView = findViewById(R.id.batteryMeter);
        digitD.updateSpeed(currentSpeed);
        digitG.updateSpeed(currentSpeed);

        // Toast pour avertir l'utilisateur
        Device device = getIntent().getParcelableExtra("mac");
        BTServ = new BluetoothService(this, device);

        /*
          Si la connexion échoue, on affiche un message d'erreur et on ferme l'activité
          */
        if (!BTServ.connectToDevice(device)) {
            toast(this, "Impossible de se connecter à cet appareil");
            finish();
        }
        /*
         * Si l'utilisateur refuse d'activer le bluetooth, on affiche un message d'erreur et on ferme l'activité
         */
        if (!BluetoothService.checkBTPermissions(this, this)) {
            toast(this, "Autorisez le bluetooth pour continuer");
            finish();
        }

        /*
          On écoute les changements de la barre de vitesse generale (celle qui controle les deux moteurs)
         */
        vitesseGeneral.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            /**
             * On ajuste la valeur de la barre de vitesse pour qu'elle soit un multiple de 5
             * @param progress la valeur de la barre de vitesse
             */
            private void adjustProgress(int progress) {
                if (progress % 50 == 0)
                    progressChangedValue = progress;
                else {
                    int lower = (progress / step) * step;
                    int upper = lower + step;
                    progressChangedValue = (progress - lower < upper - progress) ? lower : upper;
                }
            }

            // On change la valeur de la barre de vitesse generale
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustProgress(progress);
                txtProgressGeneral.setText(String.valueOf(progressChangedValue));
            }

            // On affiche le marqueur de la barre de vitesse generale
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rlmarkerGeneral.setVisibility(View.VISIBLE);
                txtProgressGeneral.setText(String.valueOf(progressChangedValue));
            }

            /** On envoie la commande pour changer la vitesse des moteurs, tout en cachant le marqueur
             * et en ajustant la valeur de la barre de vitesse generale. <br>
             * Si la vitesse est nulle, on envoie la commande pour arreter le robot. <br>
             * Si la vitesse est plus grande que la vitesse actuelle, on envoie la commande pour augmenter la vitesse. <br>
             * Si la vitesse est plus petite que la vitesse actuelle, on envoie la commande pour diminuer la vitesse. <br>
             *
             * @param seekBar la barre de vitesse generale
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adjustProgress(seekBar.getProgress());
                rlmarkerGeneral.setVisibility(View.INVISIBLE);

                if (progressChangedValue == 0) {
                    BTServ.sendCommand(7, null);
                    currentSpeed = 0;
                } else
                    BTServ.sendCommand(5, progressChangedValue / 10);

                seekBar.setProgress(progressChangedValue);
            }
        });

        /* On ecoute les vitesses des moteurs recues par le robot dans un thread separé */
        new Thread(listenToIncommingSpeed()).start();

        /* Commandes pour avancer, reculer, tourner a gauche, tourner a droite, arreter le rebot. */
        btnAvancer.setOnClickListener((view) -> BTServ.sendCommand(1, currentSpeed));
        btnReculer.setOnClickListener((view) -> BTServ.sendCommand(2, currentSpeed));
        btnGauche.setOnClickListener((view) -> BTServ.sendCommand(3, null));
        btnDroite.setOnClickListener((view) -> BTServ.sendCommand(4, null));
        btnStop.setOnClickListener((view) -> BTServ.sendCommand(7, null));
        btnExit.setOnClickListener((view) -> finish());

        /* Si la connexion est perdue, on affiche un message d'erreur et on desactive les boutons de commande. <br>
         * La connexion est retablie si l'utilisateur appuie sur le bouton "Retry"
         * à condition que le robot soit en attente de connexion Bluetooth
         * */
        btnRetry.setOnClickListener((view) -> {
            if (BTServ.connectToDevice(device)) {
                connectionEstablished();
                new Thread(listenToIncommingSpeed()).start();
            } else connectionCut();
        });
        btnClear.setOnClickListener((view) -> txtStatus.setText(""));
        batteryView.setOnClickListener((view) -> toast(this, volt + "V"));
    }


    /**
     * On ecoute les vitesses des moteurs recues par le robot dans un thread separé.
     * Si la connexion est perdue, on affiche un message d'erreur et on desactive les boutons de commande.
     * La connexion est retablie si l'utilisateur appuie sur le bouton "Retry"
     * @return un thread qui ecoute les vitesses des moteurs recues par le robot
     */
    @NonNull
    private Runnable listenToIncommingSpeed() {
        return () -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    BTServ.sendCommand(0, null);
                    int[] speedData = BTServ.receivePayload();
                    if (speedData == null)
                        break;
                    runOnUiThread(() -> {
                        if (speedData[0] == speedData[1]) {
                            currentSpeed = speedData[0];
                            vitesseGeneral.setProgress(speedData[0]);
                            vitesseGeneral.setEnabled(true);
                        } else vitesseGeneral.setEnabled(false);
                        digitG.updateSpeed(speedData[0]);
                        digitD.updateSpeed(speedData[1]);
                        reactToObstacle(speedData[3]);
                        batteryView.setChargeLevel(calculateBattery(speedData[4]));
                    });
                } catch (InterruptedException | IOException e) {
                    if (e.getMessage().trim().equals("bt socket closed, read return: -1".trim()))
                        break;
                }
            }
            runOnUiThread(this::connectionCut);
        };
    }

    private void reactToObstacle(int detected) {
        if (detected == 1 && !obstacleDetected) {
            toast(this, "Obstacle détecté");
            obstacleDetected = true;
            BTServ.sendCommand(7, null); // stop the robot
        } else {
            obstacleDetected = false;
        }
    }

    private void printOnUIConsole(int actualStatus, String customMessage){

        switch (actualStatus) {
            case 1:
                txtStatus.append("je suis arreté\n");
                break;
            case 2:
                txtStatus.append("Je vais en avant\n");
                break;
            case 3:
                txtStatus.append("Je vais en arrière\n");
                break;
            case 4:
                txtStatus.append("Je tourne à gauche\n");
                break;
            case 5:
                txtStatus.append("Je tourne à droite\n");
                break;
            case 0:
                txtStatus.append(customMessage + "\n");
                break;
            default:
                break;
        }
    }

    /**
     * Si la connexion est perdue, desactive les boutons de commande et affiche un message d'erreur
     */
    private void connectionCut() {
        txtConnectionStatus.setText("Connexion perdue");
        txtConnectionStatus.setTextColor(getResources().getColor(R.color.red));
        btnAvancer.setEnabled(false);
        btnReculer.setEnabled(false);
        btnGauche.setEnabled(false);
        btnDroite.setEnabled(false);
        btnStop.setEnabled(false);
        vitesseGeneral.setEnabled(false);
        btnRetry.setVisibility(View.VISIBLE);
    }

    /**
     * Si la connexion est retablie, active les boutons de commande et affiche un message de succes
     */
    private void connectionEstablished() {
        txtConnectionStatus.setText("Connexion établie");
        txtConnectionStatus.setTextColor(getResources().getColor(R.color.green));
        btnAvancer.setEnabled(true);
        btnReculer.setEnabled(true);
        btnGauche.setEnabled(true);
        btnDroite.setEnabled(true);
        btnStop.setEnabled(true);
        vitesseGeneral.setEnabled(true);
        btnRetry.setVisibility(View.GONE);
    }

    /**
     * Calcule le pourcentage de la batterie en fonction de la tension
     * @param voltage la tension de la batterie
     * @return le pourcentage correspondant à la tension de la batterie
     */
    private int calculateBattery(int voltage){
        final int VOLTAGE_MIN = 71;
        final int VOLTAGE_MAX = 81;
        if (voltage < VOLTAGE_MIN) return 0;
        else if (voltage > VOLTAGE_MAX) return 100;

        float percentage = ((float) (voltage - VOLTAGE_MIN) / (VOLTAGE_MAX - VOLTAGE_MIN)) * 100;
        return Math.round(percentage);
    }

    /**
     * On quitte proprement l'activité
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        BTServ.disconnect();
        super.onDestroy();
    }

}
