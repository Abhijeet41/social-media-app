package com.abhi41.socialmediaapp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<NotificationModel> notificationList;
    private Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationList, Context context) {
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
        NotificationModel model = notificationList.get(position);
        holder.imgProfile.setImageResource(model.getProfile());
        //to mention adapter that we use html tags in notification
        holder.txtNotification.setText(Html.fromHtml(model.getStrNotification()));
        holder.txtTime.setText(model.getStrTime());
    }

    @Override
    public int getItemCount() {
        if (notificationList.size() == 0){
            return 0;
        }
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProfile;
        private TextView txtNotification,txtTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
