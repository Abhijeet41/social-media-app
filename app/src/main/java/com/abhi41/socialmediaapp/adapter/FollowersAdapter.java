package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.SingleFirendRvDesignBinding;
import com.abhi41.socialmediaapp.model.FollowModel;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    List<FollowModel> friendList;
    private Context context;

    public FollowersAdapter(List<FollowModel> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_firend_rv_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.ViewHolder holder, int position) {
        final FollowModel followModel = friendList.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(followModel.getFollowedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Glide.with(context)
                                .load(user.getProfilePhoto())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.imgProfile);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        PrintMessage.printLogD("error",error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {

        if (friendList.size() == 0){
            return 0;
        }
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleFirendRvDesignBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleFirendRvDesignBinding.bind(itemView);


        }
    }
}
