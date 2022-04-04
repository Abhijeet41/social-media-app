package com.abhi41.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abhi41.socialmediaapp.databinding.ActivityMainBinding;

import com.abhi41.socialmediaapp.fragments.AddPostFragment;
import com.abhi41.socialmediaapp.fragments.HomeFragment;
import com.abhi41.socialmediaapp.fragments.NotificationFragment;
import com.abhi41.socialmediaapp.fragments.ProfileFragment;
import com.abhi41.socialmediaapp.fragments.SearchFragment;
import com.abhi41.socialmediaapp.model.JWSRequest;
import com.abhi41.socialmediaapp.untils.Zipper;
import com.abhi41.socialmediaapp.viewmodel.DashBoardViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.rootbeer.RootBeer;

import java.io.File;
import java.security.SecureRandom;
import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private DashBoardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of(this).get(DashBoardViewModel.class);


        setToolBar();
        setBottomNavigation();
      //  check_device_rooted();
        setObserver();

      /*  try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File f = new File(dir, "/"+"Indeed.html");
            String path = f.getAbsolutePath();
            Zipper zipper = new Zipper("password");
            zipper.pack(path,dir);
        } catch (java.util.zip.ZipException e) {
            e.printStackTrace();
        }*/
        
    }

    private void setObserver() {
       // viewModel.

        viewModel.getmCts().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getApplicationContext(), "CTS profile True", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "CTS profile False", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getmIntegrity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getApplicationContext(), "Integrity True", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Integrity False", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setToolBar() {
        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");
    }

    private void setBottomNavigation() {

        openFragment(new HomeFragment(), "HomeFragment", false);

        binding.bottomNavigation.setOnItemSelectedListener(item ->
                {
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new HomeFragment(), "HomeFragment", false);
                            return true;

                        case R.id.menu_add:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new AddPostFragment(), "AddPostFragment", false);
                            return true;

                        case R.id.menu_profile:
                            openFragment(new ProfileFragment(), "ProfileFragment", false);
                            binding.toolbar.setVisibility(View.VISIBLE);

                            return true;

                        case R.id.menu_notification:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new NotificationFragment(), "NotificationFragment", false);
                            return true;

                        case R.id.menu_search:
                            binding.toolbar.setVisibility(View.GONE);
                            openFragment(new SearchFragment(), "SearchFragment", false);
                            return true;


                    }
                    return false;
                }
        );


    }

    private void openFragment(Fragment fragment, String Tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        if (addToBackStack) {
            transaction.addToBackStack(Tag);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:

                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void check_device_rooted() {
        RootBeer rootBeer = new RootBeer(getApplicationContext());
        if (rootBeer.isRooted()) {
            //we found indication of root
            Toast.makeText(getApplicationContext(), "Root detected", Toast.LENGTH_SHORT).show();


        } else {
            //we didn't find indication of root
        }

        SafetyNet.getClient(this).attest(generateNonce(), getString(R.string.api_key) )
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.AttestationResponse response) {
                                // Indicates communication with the service was successful.
                                // Use response.getJwsResult() to get the result data.

                                String result = response.getJwsResult();
                                Log.d(TAG, "onSuccess: " + result);

                                JWSRequest jwsRequest = new JWSRequest();
                                jwsRequest.setSignedAttestation(result);
                                viewModel.apiFetchSafetynet(result,getString(R.string.api_key));
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while communicating with the service.
                        if (e instanceof ApiException) {
                            // An error with the Google Play services API contains some
                            // additional details.
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "onFailure: " + apiException);
                            // You can retrieve the status code using the
                            // apiException.getStatusCode() method.
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }

    private byte[] generateNonce() {
        SecureRandom secureRandom = new SecureRandom();

        byte[] nonce = new byte[512];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);



    }
}