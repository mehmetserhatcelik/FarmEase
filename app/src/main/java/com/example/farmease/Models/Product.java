package com.example.farmease.Models;
public class Product {
    String name;
    String plantDate;
    String harvestDate;
    int period;

    public String getName() {
        return name;
    }

    public String getPlantDate() {
        return plantDate;
    }

    public String getHarvestDate() {
        return harvestDate;
    }

    public int getPeriod() {
        return period;
    }
    private int findMonth(String date)
    {
        int temp =0;
        for (int i = 0 ; i < date.length();i++)
        {
            if(date.charAt(i) == '-' )
            {
                if(date.charAt(i+2) == '-')
                {
                    temp= Character.getNumericValue(date.charAt(i+1));
                    break;
                }
                else
                {
                    temp= Integer.parseInt(date.substring(i+1,i+3));
                    break;
                }

            }

        }
        return temp;
    }
    private int findYear(String date)
    {
        return Integer.parseInt(date.substring(0,4));
    }
    private String setMonth(String date, int month)
    {
        String temp ="";
        for (int i = 0 ; i < date.length();i++)
        {
            if(month<=12)
            {
                if(date.charAt(i) == '-')
                {
                    if(date.charAt(i+2) == '-')
                    {

                        temp = date.substring(0,i+1)+month+date.substring(i+2,date.length());
                        break;
                    }
                    else{
                        temp = date.substring(0,i+1)+month+date.substring(i+3,date.length());
                        break;
                    }
                }
            }
            else{
                if(date.charAt(i) == '-')
                {
                    if(date.charAt(i+2) == '-')
                    {

                        temp = (findYear(date)+1)+"-"+(month-12)+date.substring(i+2,date.length());
                        break;
                    }
                    else{
                        temp = (findYear(date)+1)+"-"+(month-12)+date.substring(i+3,date.length());
                        break;
                    }
                }
            }

        }
        return temp;
    }
    public String[] getIrrigations(String plantDate, String harvestDate, int period)
    {
        int n = findMonth(harvestDate)+findYear(harvestDate)*12;
        int m = findMonth(plantDate)+findYear(plantDate)*12;
        String[] temp = new String[n-m-1];
        for (int i = 0; i < n-m-1; i++) {
            temp[i] = setMonth(plantDate,findMonth(plantDate)+i+1);
        }
        return temp;

    }

    public Product(String name, String plantDate, String harvestDate, int period)
    {
        this.name = name;
        this.plantDate = plantDate;
        this.harvestDate = harvestDate;
        this.period = period;
    }


}
