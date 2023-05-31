package com.example.farmease.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.farmease.Models.Good;
import com.example.farmease.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StockMarketAdapter extends RecyclerView.Adapter<StockMarketAdapter.MyViewHolder>  {



    static ArrayList<Good> list;
    String[] colors = {"#F1F3F9","#FFFFFF"};


    public StockMarketAdapter( ArrayList<Good> list) {

        this.list = list;

    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.row_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
            if(position==0){
            holder.bind(list.get(position),position);}else {
                holder.bind(list.get(position-1),position);
            }


        holder.itemView.setBackgroundColor(Color.parseColor(colors[position%2]));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView price;
        TextView changetxt;
        DecimalFormat df = new DecimalFormat("#.##");
        Timer myTimer;
        TimerTask task;
        Random rand;
        double change;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }

        public void bind(Good good,int position)
        {
            name = itemView.findViewById(R.id.goodName);
            price = itemView.findViewById(R.id.goodPrice);
            changetxt = itemView.findViewById(R.id.change);
            if(position==0)
            {
                changetxt.setText("Change");
                name.setText("Name");
                price.setText("Average (₺)");
            }
            else{

            rand = new Random();
            boolean ispositive = rand.nextBoolean();
            if (ispositive)
                change = rand.nextDouble()+rand.nextInt(5);
            else
                change = -rand.nextDouble()-rand.nextInt(5);


            changetxt.setText("%"+df.format(change));
            if(!changetxt.getText().toString().equals("Change")){
            if(change > 0)
            {
                changetxt.setTextColor(Color.parseColor("#11ad3b"));
            }
            else {
                changetxt.setTextColor(Color.parseColor("#e01919"));
            }
            }
            name.setText(good.getName());
            price.setText(df.format(Double.valueOf(good.getPrice())*(100+change)/100));}
        }
    }
}