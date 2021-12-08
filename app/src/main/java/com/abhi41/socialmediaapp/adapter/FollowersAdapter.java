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
