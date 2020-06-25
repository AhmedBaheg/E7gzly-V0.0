package com.example.e7gzly.model;

public class PassengerInfo {

    private String train_line;
    private String from;
    private String to;
    private String leave;
    private String arrive;
    private String train_class;
    private String date;
    private String code;
    private int passenger_seats ;
    private double  price;

    public PassengerInfo(String train_line, String from, String to, String leave, String arrive, String train_class, String date, int passenger_seats, double price, String code) {
        this.train_line = train_line;
        this.from = from;
        this.to = to;
        this.leave = leave;
        this.arrive = arrive;
        this.train_class = train_class;
        this.date = date;
        this.passenger_seats = passenger_seats;
        this.price = price;
        this.code = code;
    }

    public PassengerInfo() {
    }

    public String getTrain_line() {
        return train_line;
    }

    public void setTrain_line(String train_line) {
        this.train_line = train_line;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getTrain_class() {
        return train_class;
    }

    public void setTrain_class(String train_class) {
        this.train_class = train_class;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPassenger_seats() {
        return passenger_seats;
    }

    public void setPassenger_seats(int passenger_seats) {
        this.passenger_seats = passenger_seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
