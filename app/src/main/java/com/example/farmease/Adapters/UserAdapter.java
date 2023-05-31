package com.example.farmease.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmease.AddField;
import com.example.farmease.Field;
import com.example.farmease.FieldInfo;
import com.example.farmease.Fields;
import com.example.farmease.MainActivity;
import com.example.farmease.MessageActivity;
import com.example.farmease.Models.Chatlist;
import com.example.farmease.Models.Notification;
import com.example.farmease.Models.User;
import com.example.farmease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<User> mUsers;

    private String engineer = "0";

    public UserAdapter(Context mContext, List<User> mUsers,String engineer) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.engineer = engineer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(engineer.equals("0")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        }
        else {
            view = LayoutInflater.from(mContext).inflate(R.layout.user_item_for_engineer, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MessageActivity.class);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification").child(fuser.getUid());
                holder.textView.setVisibility(View.INVISIBLE);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
        if (engineer.equals("1")) {
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Fields.class);
                    intent.putExtra("Uid", user.getId());
                    mContext.startActivity(intent);

                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        private TextView textView;
        private ImageView info;
        public ViewHolder(View itemView ) {
            super(itemView);
            info = itemView.findViewById(R.id.infor);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            textView = itemView.findViewById(R.id.notification);
        }
    }

}
