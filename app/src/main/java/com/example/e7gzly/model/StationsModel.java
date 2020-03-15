package com.example.e7gzly.model;

import androidx.annotation.NonNull;

public class StationsModel {
    private String  st_name;
    private String st_id;

    public StationsModel(String st_id, String st_name) {
        this.st_id = st_id;
        this.st_name = st_name;
    }

    public StationsModel() {
    }

    public String getSt_id() {
        return st_id;
    }

    public String getSt_name() {
        return st_name;
    }

    @NonNull
    @Override
    public String toString() {
        return st_name;
    }
}
