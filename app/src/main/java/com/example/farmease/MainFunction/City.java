package com.example.farmease.MainFunction;

import java.util.ArrayList;

public class City {

    private ArrayList<String> goods;

    private String name;

    public City(String name, ArrayList<String> goods)
    {

        this.goods = goods;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGoods() {
        return goods;
    }
}
