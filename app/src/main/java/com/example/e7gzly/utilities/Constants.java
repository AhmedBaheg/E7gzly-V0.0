package com.example.e7gzly.utilities;


import com.google.firebase.auth.FirebaseAuth;

public class Constants {
    public static final String USERS = "User";
    public static final String STATIONS = "stations";

    public static String getUID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
