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
import com.abhi41.socialmediaapp.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationListFragment extends Fragment {

    FragmentNotificationListBinding binding;
    ArrayList<Notification> notificationList;
    NotificationAdapter adapter;

    FirebaseDatabase database;
    public NotificationListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
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
        getListOfDummyNotification();
        getListOfNotification(); //from firebase
        adapter = new NotificationAdapter(notificationList,getContext());
        binding.rvNotification.setAdapter(adapter);
    }

    private void getListOfNotification() {
            database.getReference()
                    .child("notification")
                    .child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            notificationList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Notification notification = dataSnapshot.getValue(Notification.class);
                                notification.setNotificationId(dataSnapshot.getKey());
                                notificationList.add(notification);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }


    private void getListOfDummyNotification(){
        notificationList = new ArrayList<>();
    /*
        notificationList.add(new Notification(R.drawable.rose_byrne,"<b>Rose byrne</b> menstion you in comment : try again",
                "Just Now"));
        notificationList.add(new Notification(R.drawable.natalie,"<b>Natalie</b> menstion you in comment : try again",
                "30 mins ago"));
        notificationList.add(new Notification(R.drawable.kristen,"<b>Kristen</b> menstion you in comment : try again",
                "2 hours"));
        notificationList.add(new Notification(R.drawable.angelina,"<b>Angelina</b> menstion you in comment : try again",
                "3 hours"));
        notificationList.add(new Notification(R.drawable.alexandra,"<b>Alexandra</b> menstion you in comment : try again",
                "6 hours"));
        notificationList.add(new Notification(R.drawable.rebecca,"<b>Rebecca</b> menstion you in comment : try again",
                "9 hours"));
        notificationList.add(new Notification(R.drawable.emma,"<b>Emma</b> menstion you in comment : try again",
                "2 days ago"));
        notificationList.add(new Notification(R.drawable.monica,"<b>Monica</b> menstion you in comment : try again",
                "6 days ago"));
        notificationList.add(new Notification(R.drawable.cruise,"<b>Cruise</b> menstion you in comment : try again",
                "9 days ago"));*/
    }

/*    @Override
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

    }*/

    @Override
    public void onResume() {
        super.onResume();
    }
}