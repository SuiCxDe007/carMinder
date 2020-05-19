package com.suicxde.carminder.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {


    private String email;
    private String user_id;
    private String username;
    private String nameuser;
    private String usertp;


    public User(String email, String user_id, String username, String nameuser, String usertp) {
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.nameuser = nameuser;
        this.usertp = usertp;
    }

    public User() {

    }

    protected User(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        usertp = in.readString();
        nameuser = in.readString();
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


    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsertp() {
        return usertp;
    }

    public void setUsertp(String usertp) {
        this.usertp = usertp;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", nameuser='" + nameuser + '\'' +
                ", usertp='" + usertp + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(nameuser);
        dest.writeString(usertp);

    }

}
