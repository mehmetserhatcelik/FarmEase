package com.example.farmease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements RecyclerViewInterface{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    static int pos;

    ArrayList<Field> list;

    public MyAdapter(Context context, ArrayList<Field> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.field_card,parent,false);
        return new MyViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        Field field = list.get(position);
        holder.fieldName.setText(field.getFieldName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemClick(int pos) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView fieldName;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
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
    }
}
