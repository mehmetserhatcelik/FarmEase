package com.example.farmease.Models;

public class Image {
    String name;
    int imageID;

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }

    public Image(String name, int imageID)
    {
        this.imageID=imageID;
        this.name = name;
    }

}
