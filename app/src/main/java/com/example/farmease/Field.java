package com.example.farmease;

import com.example.farmease.MainFunction.Cities;
import com.example.farmease.MainFunction.City;

import java.util.ArrayList;

public class Field {
    String fieldName;
    String cityName;
    double latitude;
    double longitude;
    String uid;
    ArrayList<City> cities = new Cities();

    public Field(String uid,double longitude, double latitude, String cityName, String fieldName)
    {
        this.uid =uid;
        this.latitude =latitude;
        this.longitude = longitude;
        this.cityName = cityName;
        this.fieldName = fieldName;
    }
    public City getCity()
    {
        City result = cities.get(0);
        for (int i = 0; i < cities.size(); i++) {
            if(cities.get(i).getName().equals(cityName))
            {
                result = cities.get(i);
            }
        }
        return result;
    }
    public String getuid()
    {
        return uid;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCityName()
    {
        return cityName;
    }
    public String getFieldName() {
        return fieldName;
    }
}
