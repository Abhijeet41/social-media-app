package com.abhi41.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abhi41.socialmediaapp.databinding.ActivityMainBinding;
import com.abhi41.socialmediaapp.fragments.AddFragment;
import com.abhi41.socialmediaapp.fragments.HomeFragment;
import com.abhi41.socialmediaapp.fragments.NotificationFragment;
import com.abhi41.socialmediaapp.fragments.ProfileFragment;
import com.abhi41.socialmediaapp.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        setToolBar();
        setBottomNavigation();
    }

    private void setToolBar() {
        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");
    }

    private void setBottomNavigation() {

        openFragment(new HomeFragment(),"HomeFragment",false);

        binding.bottomNavigation.setOnItemSelectedListener(item ->
                {
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new HomeFragment(),"HomeFragment",false);
                            return true;

                        case R.id.menu_add:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new AddFragment(),"AddFragment",false);
                            return true;

                        case R.id.menu_profile:
                            openFragment(new ProfileFragment(),"ProfileFragment",false);
                            binding.toolbar.setVisibility(View.VISIBLE);

                            return true;

                        case R.id.menu_notification:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new NotificationFragment(),"NotificationFragment",false);
                            return true;

                        case R.id.menu_search:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new SearchFragment(),"SearchFragment",false);
                            return true;


                    }
                    return false;
                }
        );


    }

    private void openFragment(Fragment fragment,String Tag, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        if (addToBackStack){
            transaction.addToBackStack(Tag);
        }
        transaction.replace(R.id.container,fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }
}