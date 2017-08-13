package com.newtra.astro.BeanObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sethu on 8/8/17.
 */

public class ChannelExtRef implements Parcelable {
    String system;
    String subSystem;
    String value;

    protected ChannelExtRef(Parcel in) {
        system = in.readString();
        subSystem = in.readString();
        value = in.readString();
    }

    public static final Creator<ChannelExtRef> CREATOR = new Creator<ChannelExtRef>() {
        @Override
        public ChannelExtRef createFromParcel(Parcel in) {
            return new ChannelExtRef(in);
        }

        @Override
        public ChannelExtRef[] newArray(int size) {
            return new ChannelExtRef[size];
        }
    };

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChannelExtRef(String system, String subSystem, String value) {

        this.system = system;
        this.subSystem = subSystem;
        this.value = value;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(system);
        parcel.writeString(subSystem);
        parcel.writeString(value);

    }
}
