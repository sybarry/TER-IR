package androidproject.applicationlejosev3.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Utils {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog.Builder alert(Context context, String msg, DialogInterface.OnClickListener ok_listener) {
        return new AlertDialog.Builder(context)
                .setPositiveButton("OK", ok_listener)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .setTitle("Attention")
                .setMessage(msg);
    }

}
