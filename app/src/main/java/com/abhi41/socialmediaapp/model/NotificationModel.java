package com.abhi41.socialmediaapp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class NotificationModel implements Parcelable {
    private int profile;
    private String strNotification,strTime;

    public NotificationModel(int profile, String strNotification, String strTime) {
        this.profile = profile;
        this.strNotification = strNotification;
        this.strTime = strTime;
    }

    protected NotificationModel(Parcel in) {
        profile = in.readInt();
        strNotification = in.readString();
        strTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(profile);
        dest.writeString(strNotification);
        dest.writeString(strTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getStrNotification() {
        return strNotification;
    }

    public void setStrNotification(String strNotification) {
        this.strNotification = strNotification;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
}
