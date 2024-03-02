package androidproject.applicationlejosev3.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidproject.applicationlejosev3.R;

public class DeviceAdapter extends ArrayAdapter<Device> {
    public DeviceAdapter(Context context, List<Device> devices) {
        super(context, 0, devices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Device device = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_device, parent, false);

        TextView tvName = convertView.findViewById(R.id.device_name);
        TextView tvAddress = convertView.findViewById(R.id.device_mac);
        tvName.setText(device.getName());
        tvAddress.setText(device.getMacAddress());

        return convertView;
    }
}
