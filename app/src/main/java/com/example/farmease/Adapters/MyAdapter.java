package com.example.farmease.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.farmease.Field;
import com.example.farmease.R;
import com.example.farmease.RecyclerViewInterface;
import com.example.farmease.WeatherScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements RecyclerViewInterface {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    static int pos;
    ArrayList<Field> list;
     static double latitude;
     static double longitude;
     static String fieldNamestr;
     String city;
     String country;
    double temp;
    String windSpeed;
    int humidity;


    private final String url ="https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "b8767c2106e668c8932a1a3caff25bb6";
    DecimalFormat df = new DecimalFormat("#.##");
    String[] colors = {"#F5D4C1","#DED2F9","#E6FFFD","#98EECC","F5F0BB"};

    public MyAdapter(Context context, ArrayList<Field> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.field_card,parent,false);
        return new MyViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        Field field = list.get(position);
        holder.fieldName.setText(field.getFieldName());
        fieldNamestr = field.getFieldName();
        longitude = field.getLongitude();
        latitude = field.getLatitude();
        city = field.getCityName();
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position%5]));

        holder.bindMapView(new LatLng(longitude,latitude));

        country ="TR";

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

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        temp = jsonObjectMain.getDouble("temp") - 273.15; // SINCE IT KELVIN

                        humidity = jsonObjectMain.getInt("humidity");

                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        windSpeed = jsonObjectWind.getString("speed");

                        holder.temp.setText(df.format(temp)+"°C");
                        holder.windSpeed.setText(windSpeed+"m/s");
                        holder.humidity.setText((humidity+"%"));
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

    @Override
    public void onItemClick(int pos) {

    }





    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView fieldName;
        TextView temp;
        TextView humidity;
        TextView windSpeed;
        MapView mapView;
        GoogleMap googleMap;



        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            windSpeed = itemView.findViewById(R.id.windSpeed);

            mapView = itemView.findViewById(R.id.map3);
            mapView.onCreate(null);
            mapView.getMapAsync(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null)
                    {
                        pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

        public void bindMapView(LatLng location) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    googleMap.addMarker(new MarkerOptions().position(location));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
                }
            });
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
            mapView.onResume();
        }
    }

}
