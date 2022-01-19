package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.CommentScreen;
import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.SingleDashboardRvBinding;
import com.abhi41.socialmediaapp.model.Notification;
import com.abhi41.socialmediaapp.model.PostModel;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<PostModel> postModelList;
    private Context context;

    public PostAdapter(List<PostModel> postModelList, Context context) {
        this.postModelList = postModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_dashboard_rv, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        PostModel model = postModelList.get(position);
        Glide.with(context)
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.imgPostImage);


        if (model.getPostDescription() == null && model.getPostDescription().equals("")) {
            holder.binding.txtPostDescription.setVisibility(View.GONE);
        } else {
            holder.binding.txtPostDescription.setText(model.getPostDescription());
            holder.binding.txtPostDescription.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context)
                        .load(user.getProfilePhoto())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.imgProfile);

                holder.binding.txtUserName.setText(user.getName());
                holder.binding.txtBio.setText(user.getProfession());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD("error", error.getMessage());
            }
        });

        //fetch and set like
        getLikes(holder, model, position);

        holder.binding.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentScreen.class);
                intent.putExtra("postId",model.getPostId());
                intent.putExtra("postedBy",model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    private void getLikes(ViewHolder holder, PostModel model, int position) {
        //set like
        holder.binding.txtLike.setText(String.valueOf(model.getPostLike()));

        //verify like by user
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getValue() != null){
                            holder.binding.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_fill, 0, 0, 0);
                        }else {
                            holder.binding.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_black_favorite_border_24, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //set comment
        holder.binding.txtComment.setText(String.valueOf(model.getCommentCount()));

        //getLike
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                } else {
                    holder.binding.txtLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(model.getPostId())
                                    .child("likes")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("postLike")
                                            //logic is first get total likes he had then increment by 1
                                            .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void unused) {
                                            //    holder.binding.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_fill, 0, 0, 0);
                                            Notification notification = new Notification();
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setPostId(model.getPostId());
                                            notification.setPostBy(model.getPostedBy());
                                            notification.setType("like");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(model.getPostedBy())
                                                    .push()
                                                    .setValue(notification);
                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleDashboardRvBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleDashboardRvBinding.bind(itemView);

        }
    }
}
