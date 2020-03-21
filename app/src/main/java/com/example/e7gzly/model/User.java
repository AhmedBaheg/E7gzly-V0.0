package com.example.e7gzly.model;

public class User {

    String fullName, email , imgUrl;

    public User(String fullName, String email , String imgUrl) {
        this.fullName = fullName;
        this.email = email;
        this.imgUrl = imgUrl;
    }

    public User() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}