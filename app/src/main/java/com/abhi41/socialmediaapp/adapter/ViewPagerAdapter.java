package com.abhi41.socialmediaapp.adapter;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.abhi41.socialmediaapp.fragments.NotificationFragment;
import com.abhi41.socialmediaapp.fragments.NotificationListFragment;
import com.abhi41.socialmediaapp.fragments.RequestFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    String [] titles = new String[]{"Notification","Request"};
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NotificationListFragment();
            case 1:
                return new RequestFragment();
            default:
                return new NotificationListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
