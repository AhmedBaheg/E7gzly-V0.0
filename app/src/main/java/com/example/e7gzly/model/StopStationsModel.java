package com.example.e7gzly.model;

import java.io.Serializable;

public class StopStationsModel implements Serializable {
    private String arrive_time;
    private String st_id;
    private String st_name;
    private int st_pos;

    public int getSt_pos() {
        return st_pos;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public String getSt_name() {
        return st_name;
    }

    public String getSt_id() {
        return st_id;
    }

    @Override
    public String toString() {
        return "StopStationsModel{" +
                "arrive_time='" + arrive_time + '\'' +
                ", st_id='" + st_id + '\'' +
                ", st_name='" + st_name + '\'' +
                '}';
    }
}
