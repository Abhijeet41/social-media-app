package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.SingleStoryRvDesignBinding;
import com.abhi41.socialmediaapp.model.Story;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.model.UserStories;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<Story> modelArrayList;
    private Context context;

    public StoryAdapter(List<Story> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_story_rv_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        UserStories lastStory = null;
        Story story = modelArrayList.get(position);

        if (story.getUserStories().size() > 0) {


            lastStory = story.getUserStories().get(story.getUserStories().size() - 1);


            Glide.with(context)
                    .load(lastStory.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.imgAddStory);

            Log.d("statusImage", lastStory.getImage());

            holder.binding.statusCircle.setPortionsCount(story.getUserStories().size());


            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);

                            Glide.with(context)
                                    .load(user.getProfilePhoto())
                                    .placeholder(R.drawable.placeholder)
                                    .into(holder.binding.imgStoryType);
                            holder.binding.txtName.setText(user.getName());
                            holder.binding.imgAddStory.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (UserStories stories : story.getUserStories()) {
                                        myStories.add(new MyStory(
                                                stories.getImage()
                                        ));
                                    }

                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(user.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(user.getProfilePhoto()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            PrintMessage.printLogD("error", error.getMessage());
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SingleStoryRvDesignBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleStoryRvDesignBinding.bind(itemView);
        }
    }
}
