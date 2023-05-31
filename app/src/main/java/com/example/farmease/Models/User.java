package com.example.farmease.Models;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String isEngineer;

    public User(String id, String username, String imageURL,String isEngineer) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.isEngineer = isEngineer;

    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getIsEngineer(){
        return isEngineer;
    }
    public void setIsEngineer(String isEngineer) { this.isEngineer = isEngineer;}
}
