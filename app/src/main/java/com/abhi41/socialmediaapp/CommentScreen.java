package com.abhi41.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abhi41.socialmediaapp.adapter.CommentAdapter;

import com.abhi41.socialmediaapp.databinding.ActivityCommentScreenBinding;
import com.abhi41.socialmediaapp.model.Comment;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentScreen extends AppCompatActivity {
    private static final String TAG = "CommentScreen";
    ActivityCommentScreenBinding binding;
    Intent intent;
    String postId,postedBy;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private List<Comment> commentList;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();

        setSupportActionBar(binding.toolbar2);
        CommentScreen.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        initalization();
        getPostsData();
        clickListener();
        getComments();
    }

    private void initalization() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        commentList = new ArrayList<>();
    }

    private void getPostsData() {
        //get the post details from firebase
        database.getReference()
                .child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostModel post = snapshot.getValue(PostModel.class);

                Glide.with(getApplicationContext())
                        .load(post.getPostImage())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imgPostImage);

                binding.textDescription.setText(post.getPostDescription());
                binding.txtLike.setText(String.valueOf(post.getPostLike()));
                binding.txtComment.setText(String.valueOf(post.getCommentCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD(TAG,error.getMessage());
            }
        });

        //get the users detail from firebase
        database.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext())
                        .load(user.getProfilePhoto())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imgProfile);
                binding.txtName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD(TAG,error.getMessage());
            }
        });

    }


    private void clickListener() {
        binding.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Comment comment = new Comment();
                comment.setCommentBody(binding.edtComment.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());
                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push() // push to create a new node
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {

                        //set comment counts

                        database.getReference()
                                .child("posts")
                                .child(postId)
                                .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount = 0;
                                //if snapshot already exist then add count
                                if (snapshot.exists()){
                                    commentCount = snapshot.getValue(Integer.class);
                                }

                                //increment commentcount after adding comment

                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount").setValue(commentCount + 1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                        binding.edtComment.getText().clear();
                                        Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_SHORT).show();

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostId(postId);
                                        notification.setPostBy(postedBy);
                                        notification.setType("comment");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postedBy)
                                                .push()
                                                .setValue(notification);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });

    }

    private void getComments() {

        adapter = new CommentAdapter(CommentScreen.this,commentList);
        binding.rvComment.setAdapter(adapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD(TAG,error.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}