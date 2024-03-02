package androidproject.applicationlejosev3.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Device implements Parcelable {
    private final String name;
    private final String macAddress;

    public Device(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    protected Device(Parcel in) {
        name = in.readString();
        macAddress = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(macAddress);
    }
}
