package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.SingleUserSampleBinding;
import com.abhi41.socialmediaapp.model.FollowModel;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        User user = userList.get(position);

        Glide.with(context)
                .load(user.getProfilePhoto())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.imgProfile);

        holder.binding.txtName.setText(user.getName());
        holder.binding.txtProfession.setText(user.getProfession());

        holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowModel follow = new FollowModel();
                follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                follow.setFollowedAt(new Date().getTime());

                //save data in firebase database
                saveDataToFirebase(user,follow);
            }
        });


    }

    private void saveDataToFirebase(User user, FollowModel follow) {
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(user.getUserId())
                        .child("followerCount")
                        .setValue(user.getFollowerCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        PrintMessage.printToastMessage(context,"You followed");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleUserSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleUserSampleBinding.bind(itemView);
        }
    }
}
