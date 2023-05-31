package com.example.farmease.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmease.R;
import com.example.farmease.RecyclerViewInterface;

public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder> implements RecyclerViewInterface {
    Context context;
    static int pos;
    public final RecyclerViewInterface recyclerViewInterface;

    public EmptyAdapter(Context context, RecyclerViewInterface recyclerViewInterface)
    {
        this.context = context;
        this.recyclerViewInterface =recyclerViewInterface;
    }

    @NonNull
    @Override
    public EmptyAdapter.EmptyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.add_field_card,parent,false);
        return new EmptyAdapter.EmptyViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptyAdapter.EmptyViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.parseColor("#F5D4C1"));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onItemClick(int pos) {

    }
    public static class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
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
