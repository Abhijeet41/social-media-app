package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.SingleUserSampleBinding;
import com.abhi41.socialmediaapp.model.FollowModel;
import com.abhi41.socialmediaapp.model.Notification;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        //read if user already follow others
         getFollowers(user,holder);


    }

    private void getFollowers(User user, ViewHolder holder) {

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.binding.btnFollow.setBackgroundDrawable(
                            ContextCompat
                                    .getDrawable(context,R.drawable.follow_active_btn)
                    );
                    holder.binding.btnFollow.setText("Following");
                    holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.grey_very_light));
                    holder.binding.btnFollow.setEnabled(false);
                }else {
                    holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FollowModel follow = new FollowModel();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            //save data in firebase database
                            saveDataToFirebase(user,follow,holder);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveDataToFirebase(User user, FollowModel follow, ViewHolder holder) {
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
                        holder.binding.btnFollow.setBackgroundDrawable(
                                ContextCompat
                                        .getDrawable(context,R.drawable.follow_active_btn)
                        );
                        holder.binding.btnFollow.setText("Following");
                        holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.grey_very_light));
                        holder.binding.btnFollow.setEnabled(false);

                        PrintMessage.printToastMessage(context,"You followed");

                        Notification notification = new Notification();
                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                        notification.setNotificationAt(new Date().getTime());
                        notification.setType("follow");

                        FirebaseDatabase.getInstance().getReference()
                                .child("notification")
                                .child(user.getUserId())
                                .push()
                                .setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                PrintMessage.printLogD("notification","success");
                            }
                        });
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
