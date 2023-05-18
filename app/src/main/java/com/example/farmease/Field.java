package com.example.farmease;

import com.example.farmease.MainFunction.Cities;
import com.example.farmease.MainFunction.City;

import java.util.ArrayList;

public class Field {
    String fieldName;
    String cityName;
    double latitude;
    double longitude;
    ArrayList<City> cities = new Cities();

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
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityName()
    {
        return cityName;
    }
    public String getFieldName() {
        return fieldName;
    }
}
