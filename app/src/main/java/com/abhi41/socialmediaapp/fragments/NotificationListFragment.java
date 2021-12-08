package com.abhi41.socialmediaapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.adapter.NotificationAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentNotificationListBinding;
import com.abhi41.socialmediaapp.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;


public class NotificationListFragment extends Fragment {

    FragmentNotificationListBinding binding;
    ArrayList<NotificationModel> notificationList;
    NotificationAdapter adapter;
    public NotificationListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationListBinding.inflate(inflater,container,false);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListOfNotification();
        adapter = new NotificationAdapter(notificationList,getContext());
        binding.rvNotification.setAdapter(adapter);
    }


    private void getListOfNotification(){
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel(R.drawable.rose_byrne,"<b>Rose byrne</b> menstion you in comment : try again",
                "Just Now"));
        notificationList.add(new NotificationModel(R.drawable.natalie,"<b>Natalie</b> menstion you in comment : try again",
                "30 mins ago"));
        notificationList.add(new NotificationModel(R.drawable.kristen,"<b>Kristen</b> menstion you in comment : try again",
                "2 hours"));
        notificationList.add(new NotificationModel(R.drawable.angelina,"<b>Angelina</b> menstion you in comment : try again",
                "3 hours"));
        notificationList.add(new NotificationModel(R.drawable.alexandra,"<b>Alexandra</b> menstion you in comment : try again",
                "6 hours"));
        notificationList.add(new NotificationModel(R.drawable.rebecca,"<b>Rebecca</b> menstion you in comment : try again",
                "9 hours"));
        notificationList.add(new NotificationModel(R.drawable.emma,"<b>Emma</b> menstion you in comment : try again",
                "2 days ago"));
        notificationList.add(new NotificationModel(R.drawable.monica,"<b>Monica</b> menstion you in comment : try again",
                "6 days ago"));
        notificationList.add(new NotificationModel(R.drawable.cruise,"<b>Cruise</b> menstion you in comment : try again",
                "9 days ago"));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (notificationList != null){
            outState.putParcelableArrayList("notificationList",notificationList);
        }
        super.onSaveInstanceState(outState);


    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null){
            notificationList = savedInstanceState.getParcelableArrayList("notificationList");
            adapter = new NotificationAdapter(notificationList,getContext());
            binding.rvNotification.setAdapter(adapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}