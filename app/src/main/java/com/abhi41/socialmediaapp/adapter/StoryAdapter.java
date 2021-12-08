package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.model.StoryModel;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<StoryModel> modelArrayList;
    private Context context;

    public StoryAdapter(List<StoryModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_story_rv_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
            StoryModel storyModel = modelArrayList.get(position);

            holder.imgStory.setImageResource(storyModel.getStory());
            holder.imgProfile.setImageResource(storyModel.getProfile());
            holder.imgStoryType.setImageResource(storyModel.getStoryType());
            holder.txtName.setText(storyModel.getName());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgStoryType, imgProfile, imgStory;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStoryType = itemView.findViewById(R.id.imgStoryType);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgStory = itemView.findViewById(R.id.imgPostImage);

            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
