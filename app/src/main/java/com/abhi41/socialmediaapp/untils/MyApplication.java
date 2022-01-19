package com.abhi41.socialmediaapp.untils;

import android.app.Application;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.scottyab.rootbeer.RootBeer;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        check_device_rooted();
    }

    private void check_device_rooted() {
        RootBeer rootBeer = new RootBeer(getApplicationContext());
        if (rootBeer.isRooted()) {
            //we found indication of root
            Toast.makeText(getApplicationContext(), "Root detected", Toast.LENGTH_SHORT).show();
        } else {
            //we didn't find indication of root
        }
    }
}
