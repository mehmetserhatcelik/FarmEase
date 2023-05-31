package com.example.farmease.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.farmease.Field;
import com.example.farmease.Models.Images;
import com.example.farmease.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;
import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private final String url ="https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "b8767c2106e668c8932a1a3caff25bb6";
    DecimalFormat df = new DecimalFormat("#.##");
    Context context;
    ArrayList<Field> list;
    String country;
    String city;
    double temp;
    int humidity;
    String windSpeed;
    float pressure;
    double feelsLike;
    String clouds;
    String description;

    public WeatherAdapter(Context context, ArrayList<Field> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WeatherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.weather_card,parent,false);
        return new WeatherAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.MyViewHolder holder, int position) {
        Field field = list.get(position);
        holder.fieldName.setText(field.getFieldName());

        country ="TR";
        city= field.getCityName();

        String tempUrl = "";

        if(city.equals(""))
        {
            Toast.makeText(context, "City could not be found.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!country.equals(""))
            {
                tempUrl  =  url + "?q=" + city + "," + country + "&appid="+ appid;
            }
            else
            {
                tempUrl  =  url + "?q=" + city + "&appid="+ appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);


                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectIcon = jsonArray.getJSONObject(0);

                        String icon = "a"+jsonObjectIcon.getString("icon");

                        Images images = new Images();

                        for (int i = 0; i < images.size(); i++) {
                            if(images.get(i).getName().equals(icon))
                                holder.icon.setImageResource(images.get(i).getImageID());
                        }

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        temp = jsonObjectMain.getDouble("temp") - 273.15; // SINCE IT KELVIN

                        humidity = jsonObjectMain.getInt("humidity");

                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        windSpeed = jsonObjectWind.getString("speed");

                        pressure = jsonObjectMain.getInt("pressure");
                        feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;

                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        clouds = jsonObjectClouds.getString("all");


                        holder.temp.setText(df.format(temp)+"°C");
                        holder.windSpeed.setText(windSpeed+"m/s");
                        holder.humidity.setText((humidity+"%"));
                        holder.feelsLike.setText(df.format(feelsLike)+"°C");
                        holder.description.setText(description);
                        holder.cloudiness.setText((clouds+"%"));
                        holder.pressure.setText(pressure+"hPa");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView temp;
        TextView humidity;
        TextView windSpeed;
        TextView fieldName;
        TextView description;
        ImageView icon;
        TextView feelsLike;
        TextView cloudiness;
        TextView pressure;


        public MyViewHolder(@org.checkerframework.checker.nullness.qual.NonNull View itemView)
        {
            super(itemView);

            fieldName = itemView.findViewById(R.id.fieldName);

            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            windSpeed = itemView.findViewById(R.id.windspeed);
            description = itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.icon);
            feelsLike = itemView.findViewById(R.id.feelslike);
            cloudiness = itemView.findViewById(R.id.cloudinness);
            pressure = itemView.findViewById(R.id.pressure);




        }
    }
}
