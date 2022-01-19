package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.content.Intent;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.CommentScreen;
import com.abhi41.socialmediaapp.R;

import com.abhi41.socialmediaapp.databinding.SingleNotificationsSampleBinding;
import com.abhi41.socialmediaapp.model.Notification;
import com.abhi41.socialmediaapp.model.User;
import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<Notification> notificationList;
    private Context context;

    public NotificationAdapter(ArrayList<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_notifications_sample,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification model = notificationList.get(position);
        holder.binding.openNotification.setBackgroundColor(context.getResources().getColor(R.color.grey_very_light));
        String type = model.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getNotificationBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Glide.with(context)
                                .load(user.getProfilePhoto())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.imgProfile);
                            if (type.equals("like"))
                            {
                                holder.binding.txtNotification.setText(
                                        Html.fromHtml("</b>"+user.getName()+ "</b>"+" liked your post")
                                );

                            }else if (type.equals("comment")){
                                holder.binding.txtNotification.setText(
                                        Html.fromHtml("</b>"+user.getName()+ "</b>"+" commented on your post")
                                );

                            }else {
                                holder.binding.txtNotification.setText(
                                        Html.fromHtml("</b>"+user.getName()+ "</b>"+" start following your post")
                                );

                            }

                            holder.binding.txtTime.setText(TimeAgo.using(model.getNotificationAt()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("follow")){
                    holder.binding.openNotification.setBackgroundColor(context.getResources().getColor(R.color.white));
                    Intent intent = new Intent(context, CommentScreen.class);
                    intent.putExtra("postId",model.getPostId());
                    intent.putExtra("postedBy",model.getPostBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (notificationList.size() == 0){
            return 0;
        }
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleNotificationsSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SingleNotificationsSampleBinding.bind(itemView);
        }
    }
}
