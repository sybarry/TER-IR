package androidproject.applicationlejosev3.main;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.ConnectionBluetoothActivity;
import androidproject.applicationlejosev3.connection.MQTTConfigurationActivity;


public class MainActivity extends ListActivity {
    private List<String> activities = new ArrayList<>();
    private Map<String, Class> name2class = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Add Activities to list */
        setup_activities();
        setListAdapter(new ArrayAdapter<>(this, R.layout.activity_main, activities));

        /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());
    }

    private void setup_activities() {
        addActivity("Connexion avec Bluetooth", ConnectionBluetoothActivity.class);
        addActivity("Connexion avec Wifi", ConnectWifiActivity.class);
        addActivity("Parametres MQTT", MQTTConfigurationActivity.class);
    }

    private void addActivity(String name, Class activity) {
        activities.add(name);
        name2class.put(name, activity);
    }

    /* Private Help Entities */
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /* Find selected activity */
            String activity_name = activities.get(position);
            Class activity_class = name2class.get(activity_name);

            /* Start new Activity */
            Intent intent = new Intent(MainActivity.this, activity_class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
