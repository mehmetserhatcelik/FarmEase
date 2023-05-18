package com.example.farmease.MainFunction;

import java.util.ArrayList;

public class LocationalSuggestion {

    private ArrayList<City> cities;
    private String location;

    public LocationalSuggestion(String location)
    {
        cities = new Cities();
        this.location = location;
    }
    public ArrayList<String> suggest()
    {
        City temp = null;
        for(int i = 0 ; i < cities.size() ; i++)
        {
            if(cities.get(i).getName().equals(location))
            {
                temp = cities.get(i);
            }
        }
        return temp.getGoods();
    }
}
