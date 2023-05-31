package com.example.farmease.Models;

public class Notification {
    public String count;
    public String id;

    public Notification(String count, String id) {
        this.count = count;
        this.id = id;
    }

    public Notification() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
