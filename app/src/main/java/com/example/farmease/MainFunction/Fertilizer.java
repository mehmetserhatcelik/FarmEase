package com.example.farmease.MainFunction;

import java.util.ArrayList;

public class Fertilizer {
    ArrayList<String> arr = new ArrayList<>();
    public Fertilizer()
    {}
    public ArrayList<String> getSuggest(double ph, double salt,double lime,double organic, double phosphorus,double potassium,
                                        double nitrogen, double iron, double calcium, double magnesium,double manganese,
                                        double zinc, double boron, double copper)
    {
        if(ph<7){
            if(!isExist("Agrotime Ph Regulator",arr))
                arr.add("Agrotime Ph Regulator");
            if(!isExist("Oligro Solimax",arr))
                arr.add("Oligro Solimax");

        }

        if(salt>0.35)
        {
            if(!isExist("NoSal",arr))
                arr.add("NoSal");
            if(!isExist("StopSal",arr))
                arr.add("StopSal");
            if(!isExist("Özpa AntiSalt",arr))
                arr.add("Özpa AntiSalt");
        }

        if (lime<10)
        {
            if(!isExist("Calcium Carbonate",arr))
                arr.add("Calcium Carbonate");
            if(!isExist("Agricultural Lime",arr))
                arr.add("Agricultural Lime");
        }
        if (organic<2.5)
        {
            if(!isExist("Leonardit",arr))
                arr.add("Leonardit");
        }
        if (phosphorus<7.5)
        {
            if(!isExist("Agrotime Fosfozink",arr))
                arr.add("Potassium Chloride");
            if(!isExist("Gübretaş 20 20 20",arr))
                arr.add("Gübretaş 20 20 20");
            if(!isExist("Gübretaş 10 10 10",arr))
                arr.add("Gübretaş 10 10 10");
        }

        if(potassium<125)
        {

            if(!isExist("Potassium Chloride",arr))
                arr.add("Potassium Chloride");
            if(!isExist("Gübretaş 20 20 20",arr))
                arr.add("Gübretaş 20 20 20");
            if(!isExist("Gübretaş 10 10 10",arr))
                arr.add("Gübretaş 10 10 10");

        }
        if(calcium<2000)
        {
            if(!isExist("Amino Quelant-CA",arr))
                arr.add("Amino Quelant-CA");
            if(!isExist("Gübretaş Bestcalni",arr))
                arr.add("Gübretaş Bestcalni");
            if(!isExist("Amino Plus Ca",arr))
                arr.add("Amino Plus Ca");
        }

        if(magnesium<80)
        {
            if(!isExist("Magnesium Sulfate",arr))
                arr.add("Magnesium Sulfate");

        }if(boron<1.7)
    {
        if(!isExist("Micro Gübretaş",arr))
            arr.add("Micro Gübretaş");
        if(!isExist("Mebor 5",arr))
            arr.add("Mebor 5");
        if(!isExist("Agrotime Fosfozink",arr))
            arr.add("Mebor 2");

    }if(zinc<4)
    {
        if(!isExist("Kompoze",arr))
            arr.add("Kompoze");
        if(!isExist("Micro Gübretaş",arr))
            arr.add("Micro Gübretaş");
        if(!isExist("Electra",arr))
            arr.add("Electra");
    }
        if(manganese<23)
        {
            if(!isExist("Micro Gübretaş",arr))
                arr.add("Micro Gübretaş");
            if(!isExist("Dermin Gübretaş",arr))
                arr.add("Dermin Gübretaş");
            if(!isExist("Agrotime Kombi",arr))
                arr.add("Agrotime Kombi");
        }
        if(iron<18)
    {
        if(!isExist("Micro Gübretaş",arr))
            arr.add("Micro Gübretaş");
        if(!isExist("Dermin Gübretaş",arr))
            arr.add("Dermin Gübretaş");
        if(!isExist("Ferrum Sulfate",arr))
            arr.add("Ferrum Sulfate");

    }
        if(copper<1.10)
    {
        if(!isExist("Micro Gübretaş",arr))
            arr.add("Micro Gübretaş");
        if(!isExist("Dermin Gübretaş",arr))
            arr.add("Dermin Gübretaş");
        if(!isExist("Mebor 2",arr))
            arr.add("Mebor 2");

    }if(nitrogen<0.2)
        {
            if(!isExist("Ammonium Sulphate",arr))
                arr.add("Ammonium Sulphate");
            if(!isExist("Ammonium Nitrate",arr))
                arr.add("Ammonium Nitrate");
        }
        return arr;
    }
    public boolean isExist(String name, ArrayList<String> arr)
    {
        boolean result = false;
        for (int i = 0; i < arr.size(); i++) {
            if(name.equals(arr.get(i)))
            {
                result = true;
            }
        }
        return result;
    }
}
