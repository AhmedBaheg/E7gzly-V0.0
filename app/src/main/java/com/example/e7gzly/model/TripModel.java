package com.example.e7gzly.model;

import java.io.Serializable;
import java.util.List;

public class TripModel implements Serializable {
    private List<StopStationsModel> stop_stations;
    private String train_id;
    private String trip_id;
    private String trip_line;

    public List<StopStationsModel> getStop_stations() {
        return stop_stations;
    }

    public void setStop_stations(List<StopStationsModel> stop_stations) {
        this.stop_stations = stop_stations;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_line() {
        return trip_line;
    }

    public void setTrip_line(String trip_line) {
        this.trip_line = trip_line;
    }

    @Override
    public String toString() {
        return "\nTripModel{" +
                "\nstop_stations=" + stop_stations +
                ", \ntrain_id='" + train_id + '\'' +
                ", \ntrip_id='" + trip_id + '\'' +
                ", \ntrip_line='" + trip_line + '\'' +
                '}';
    }
}
