package androidproject.applicationlejosev3.utils;

import static androidproject.applicationlejosev3.utils.Utils.toast;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidproject.applicationlejosev3.connection.MQTTService;

public class AsyncOperation extends AsyncTask<Void, Void, Boolean> {
    private final ProgressBar progressBar;
    private final Context context;
    private final MQTTConfig config;

    public AsyncOperation(ProgressBar progressBar, MQTTConfig config, Context context) {
        this.progressBar = progressBar;
        this.context = context;
        this.config = config;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        long startTime = System.currentTimeMillis();
        Boolean result = MQTTService.checkMQTTConnection(config);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (duration < 500)
            try {
                Thread.sleep(1000 - duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);
        if (!result) toast(context, "Couldn't connect to MQTT broker");
        else toast(context, "Connection successful");
    }
}
