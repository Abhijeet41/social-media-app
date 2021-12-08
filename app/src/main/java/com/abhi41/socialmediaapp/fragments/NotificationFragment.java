package com.abhi41.socialmediaapp.fragments;

import android.os.Bundle;


import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.abhi41.socialmediaapp.adapter.ViewPagerAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentNotificationBinding;
import com.google.android.material.tabs.TabLayoutMediator;


public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    String [] titles = new String[]{"Notification","Request"};
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        binding.viewPagerNotification.setAdapter(new ViewPagerAdapter(getActivity()));

        new TabLayoutMediator(binding.tabLayout,binding.viewPagerNotification,(tab, position) ->
                tab.setText(titles[position])).attach();

        return binding.getRoot();
    }
}