package com.abhi41.socialmediaapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.adapter.UserAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentSearchBinding;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private List<User> userList = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private UserAdapter adapter;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intialization();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false);

        getFirebaseUserListData();

        adapter = new UserAdapter(getContext(),userList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvUsers.setLayoutManager(manager);
        binding.rvUsers.setAdapter(adapter);

        return binding.getRoot();
    }

    private void getFirebaseUserListData() {
            database.getReference().child("Users")
                    .addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                User user = dataSnapshot.getValue(User.class);
                                if (user == null){
                                    return;
                                }
                                user.setUserId(dataSnapshot.getKey());

                                //this condition we used because we don't want see own user in follower
                                //screen

                                if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
                                {
                                    userList.add(user);
                                }


                                Log.d(TAG, "userList: "+user.toString());
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            PrintMessage.printToastMessage(getContext(),error.getMessage());
                        }
                    });
    }

    private void intialization() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

}