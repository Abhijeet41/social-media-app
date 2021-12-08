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
import com.abhi41.socialmediaapp.model.DashBoardModel;

import java.util.List;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder> {

    private List<DashBoardModel> dashBoardModelList;
    private Context context;

    public DashBoardAdapter(List<DashBoardModel> dashBoardModelList, Context context) {
        this.dashBoardModelList = dashBoardModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_dashboard_rv,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardAdapter.ViewHolder holder, int position) {

        DashBoardModel model = dashBoardModelList.get(position);
        holder.imgProfile.setImageResource(model.getProfile());
        holder.imgPostImage.setImageResource(model.getPostImage());

        holder.txtUserName.setText(model.getName());
        holder.txtAbout.setText(model.getAbout());
        holder.txtLike.setText(model.getLike());
        holder.txtComment.setText(model.getComment());
        holder.txtShare.setText(model.getShare());

    }

    @Override
    public int getItemCount() {
        return dashBoardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProfile,imgPostImage,imgSave;
        private TextView txtUserName,txtAbout,txtLike,txtComment,txtShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgPostImage = itemView.findViewById(R.id.imgPostImage);
            imgSave = itemView.findViewById(R.id.imgSave);

            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtAbout = itemView.findViewById(R.id.txtAbout);
            txtLike = itemView.findViewById(R.id.txtLike);
            txtComment = itemView.findViewById(R.id.txtComment);
            txtShare = itemView.findViewById(R.id.txtShare);

        }
    }
}
