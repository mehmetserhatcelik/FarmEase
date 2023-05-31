package com.example.farmease.Models;

public class Good {
    String price;
    String name;

    public Good(String name, String price)
    {
        this.name = name;
        this.price = price;

    }
    public String getPrice()
    {
        return price;
    }
    public String getName()
    {
        return name;
    }
}
