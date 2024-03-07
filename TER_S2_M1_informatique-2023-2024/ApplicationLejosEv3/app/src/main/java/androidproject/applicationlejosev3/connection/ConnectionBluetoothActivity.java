package androidproject.applicationlejosev3.connection;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.panel.ControlPageActivity;
import androidproject.applicationlejosev3.utils.Device;
import androidproject.applicationlejosev3.utils.DeviceAdapter;
import androidproject.applicationlejosev3.utils.Utils;

public class ConnectionBluetoothActivity extends AppCompatActivity {
    BluetoothService BTConnect;
    ListView LV;
    Button btnRefresh;
    EditText editMAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_bluetooth);
        btnRefresh = findViewById(R.id.btnRefresh);
        LV = findViewById(R.id.LV);
        editMAC = findViewById(R.id.editMac);

        /** Verifie si le bluetooth est activé, sinon demande à l'utilisateur de l'activer */
        if (!BluetoothService.checkBTPermissions(this, this))
            finish();

        /** Récupère les appareils appairés et les affiche dans la liste */
        BTConnect = new BluetoothService(this);
        List<Device> devices = getPairedDevices(BTConnect.localAdapter);
        DeviceAdapter adapter = new DeviceAdapter(this, devices);
        LV.setAdapter(adapter);

        /** Permet de se connecter à un appareil en cliquant dessus */
        LV.setOnItemClickListener((parent, item_view, pos, row_id) -> {
            Device device = (Device) parent.getItemAtPosition(pos);
            editMAC.setText(device.getMacAddress());
            Intent it = new Intent(ConnectionBluetoothActivity.this, ControlPageActivity.class);
            it.putExtra("mac", device);
            startActivity(it);
        });

        /** Rafraichit la liste des appareils appairés */
        btnRefresh.setOnClickListener(view -> updateBluetoothDevices(LV, BTConnect.localAdapter));
    }

    /** Récupère les appareils appairés */
    private List<Device> getPairedDevices(BluetoothAdapter BTAdapter) {
        BluetoothService.checkBTPermissions(this, this);
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 2);
            return new ArrayList<>();
        }
        return BTConnect.getPairedBluetoothDevices();
    }

    /** Met à jour la liste des appareils appairés */
    private void updateBluetoothDevices(ListView LV, BluetoothAdapter BTAdapter) {
        List<Device> devices = getPairedDevices(BTAdapter);
        DeviceAdapter adapter = (DeviceAdapter) LV.getAdapter();
        adapter.clear();
        assert devices != null;
        adapter.addAll(devices);
        adapter.notifyDataSetChanged();
    }


    /** Demande à l'utilisateur d'activer le bluetooth */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                updateBluetoothDevices(LV, BTConnect.localAdapter);
            else finish();
        }
    }

    /** Récupère le résultat de la demande d'activation du bluetooth */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Utils.toast(this, "Bluetooth is enabled");
                getPairedDevices(BTConnect.localAdapter);
            } else if (resultCode == RESULT_CANCELED)
                Utils.toast(this, "User refused to enable Bluetooth");
            else Utils.toast(this, "Unknown BT error");
        }
    }

    /** Retourne à la page précédente en fermant efficacement l'activité */
    @Override
    public void onBackPressed() {
        finish();
    }

}
