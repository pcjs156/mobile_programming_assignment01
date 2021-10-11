package com.example.assignment01.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String pw;
    private String name;
    private String tel;
    private String address;

    public User(String id, String pw, String name, String tel, String address) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.tel = tel;
        this.address = address;
    }

    protected User(Parcel in) {
        id = in.readString();
        pw = in.readString();
        name = in.readString();
        tel = in.readString();
        address = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(pw);
        parcel.writeString(name);
        parcel.writeString(tel);
        parcel.writeString(address);
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }
}
