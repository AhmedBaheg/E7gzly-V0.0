package com.example.e7gzly.utilities;


import com.google.firebase.auth.FirebaseAuth;

public class Constants {
    public static final String USERS = "User";
    public static final String STATIONS = "stations";
    public static final String TRIP = "trip";
    public static final String STOP_STATIONS = "stop_stations";
    public static final String TRAINS = "train";
    public static final String TRAIN_CLASS = "train_class";
    public static final String TRAIN_ID = "train_id";

    public static String getUID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
