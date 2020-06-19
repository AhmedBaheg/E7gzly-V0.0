package com.example.e7gzly.model;

public class TrainModel {

    private String from;
    private int seats;
    private String to;
    private String train_class;
    private String train_id;
    private String trip_id;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTrain_class() {
        return train_class;
    }

    public void setTrain_class(String train_class) {
        this.train_class = train_class;
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

    @Override
    public String toString() {
        return "\nTrainModel{" +
                "\nfrom='" + from + '\'' +
                ", \nseats=" + seats +
                ", \nto='" + to + '\'' +
                ", \ntrain_class='" + train_class + '\'' +
                ", \ntrain_id='" + train_id + '\'' +
                ", \ntrip_id='" + trip_id + '\'' +
                '}';
    }
}
