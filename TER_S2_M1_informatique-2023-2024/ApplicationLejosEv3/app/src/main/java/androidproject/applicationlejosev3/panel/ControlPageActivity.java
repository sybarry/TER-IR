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

        /** Si la connexion échoue, on affiche un message d'erreur et on ferme l'activité */
        if (!BTConnect.connectToDevice(device)) {
            toast(this, "Impossible de se connecter à cet appareil");
            finish();
        }
        /** Si l'utilisateur refuse d'activer le bluetooth, on affiche un message d'erreur et on ferme l'activité */
        if (!BluetoothService.checkBTPermissions(this, this)) {
            toast(this, "Autorisez le bluetooth pour continuer");
            finish();
        }

        /**
         * On écoute les changements de la barre de vitesse generale (celle qui controle les deux moteurs)
         */
        vitesseGeneral.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            /**
             * On ajuste la valeur de la barre de vitesse pour qu'elle soit un multiple de 50
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
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adjustProgress(seekBar.getProgress());
                rlmarkerGeneral.setVisibility(View.INVISIBLE);

                if (progressChangedValue == 0) {
                    BTConnect.sendCommand(7);
                    currentSpeed = 0;
                } else if (progressChangedValue > currentSpeed)
                    BTConnect.sendCommand(5);
                else if (progressChangedValue < currentSpeed)
                    BTConnect.sendCommand(6);

                seekBar.setProgress(progressChangedValue);
            }
        });

        /** On ecoute les vitesses des moteurs recues par le robot dans un thread separé */
        new Thread(listenToIncommingSpeed()).start();

        /** Commandes pour avancer, reculer, tourner a gauche, tourner a droite, arreter le rebot. */
        btnAvancer.setOnClickListener((view) -> BTConnect.sendCommand(1));
        btnReculer.setOnClickListener((view) -> BTConnect.sendCommand(2));
        btnGauche.setOnClickListener((view) -> BTConnect.sendCommand(3));
        btnDroite.setOnClickListener((view) -> BTConnect.sendCommand(4));
        btnStop.setOnClickListener((view) -> BTConnect.sendCommand(7));
        btnExit.setOnClickListener((view) -> finish());

        /* Si la connexion est perdue, on affiche un message d'erreur et on desactive les boutons de commande. <br>
         * La connexion est retablie si l'utilisateur appuie sur le bouton "Retry"
         * à condition que le robot soit en attente de connexion Bluetooth
         * */
        btnRetry.setOnClickListener((view) -> {
            if (BTConnect.connectToDevice(device)) {
                connectionEstablished();
                new Thread(listenToIncommingSpeed()).start();
            } else connectionCut();
        });

        /** On ecoute les changements des barres de vitesse des moteurs gauche et droit */
        setSeekBarChangeListener(vitesseLeft, rlmarkerG, digitG);
        setSeekBarChangeListener(vitesseRight, rlmarkerD, digitD);
    }


    /**
     * On ecoute les vitesses des moteurs recues par le robot dans un thread separé.
     * Si la connexion est perdue, on affiche un message d'erreur et on desactive les boutons de commande.
     * La connexion est retablie si l'utilisateur appuie sur le bouton "Retry"
     * @return un thread qui ecoute les vitesses des moteurs recues par le robot
     */
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
                        } else vitesseGeneral.setEnabled(false);
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

    /**
     * On ecoute les changements de la barre de vitesse des moteurs gauche et droit, en mettant a jour
     * le marqueur de la barre de vitesse, la digitSpeedView et la valeur de la barre de vitesse
     * Ceci est pour garantir que la valeur de la vitesse soit un multiple de 50
     * @param seekBar la barre de vitesse
     * @param rlmarker le marqueur de la barre de vitesse
     * @param digit la vue de la vitesse
     */
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

    /**
     * Si la connexion est perdue, desactive les boutons de commande et affiche un message d'erreur
     */
    private void connectionCut() {
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

    /**
     * Si la connexion est retablie, active les boutons de commande et affiche un message de succes
     */
    private void connectionEstablished() {
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

    /**
     * On quitte proprement l'activité
     */
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
