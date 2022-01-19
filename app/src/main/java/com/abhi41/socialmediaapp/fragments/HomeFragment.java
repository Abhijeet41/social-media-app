package com.abhi41.socialmediaapp.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhi41.socialmediaapp.adapter.PostAdapter;
import com.abhi41.socialmediaapp.adapter.StoryAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentHomeBinding;
import com.abhi41.socialmediaapp.model.PostModel;
import com.abhi41.socialmediaapp.model.Story;
import com.abhi41.socialmediaapp.model.UserStories;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    private List<Story> storyList = new ArrayList<>();
    private List<PostModel> postModelList = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    PostAdapter postAdapter;
    StoryAdapter storyAdapter;
    ActivityResultLauncher<String> galleyLauncher;

    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.rvDashboard.showShimmerAdapter();
       // DummyList.dummyStoryList(storyList,getContext());


        storyAdapter = new StoryAdapter(storyList,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.storyRv.setLayoutManager(manager);
        //because our recycler view is inside our horizontel scrollview
        binding.storyRv.setNestedScrollingEnabled(false);
        binding.storyRv.setAdapter(storyAdapter);

        //get stoty list from firebase
        getStories();

       // DummyList.dummyPostList(postModelList,getContext());

        postAdapter = new PostAdapter(postModelList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvDashboard.addItemDecoration(new DividerItemDecoration(binding.rvDashboard.getContext(),DividerItemDecoration.VERTICAL));
        binding.rvDashboard.setLayoutManager(layoutManager);
        binding.rvDashboard.setNestedScrollingEnabled(false);

        fetchPosts();

        binding.addStoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleyLauncher.launch("image/*");
            }
        });

        galleyLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        if (result == null){
                            return;
                        }
                        binding.addStoryImg.setImageURI(result);

                        //store image in firebase storage
                        com.abhi41.socialmediaapp.untils.ProgressDialog.showProgressDialog(progressDialog);
                        final StorageReference reference = storage.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime()+"");

                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(@NonNull Uri uri) {

                                        Story story = new Story();
                                        story.setStoryAt(new Date().getTime());
                                        database.getReference()
                                                .child("stories")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void unused) {
                                                UserStories userStory = new UserStories(uri.toString(),story.getStoryAt());

                                                database.getReference()
                                                        .child("stories")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("userStories")
                                                        .push()
                                                        .setValue(userStory).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(@NonNull Void unused) {
                                                        com.abhi41.socialmediaapp.untils.ProgressDialog.hideProgressDialog(progressDialog);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

        return view;
    }

    private void getStories() {

        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){
                    storyList.clear();

                    for (DataSnapshot storySnapshot : snapshot.getChildren()){
                        Story story = new Story();
                        story.setStoryBy(storySnapshot.getKey());
                        story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));
                        List<UserStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren())
                        {
                            UserStories userStory = snapshot1.getValue(UserStories.class);
                            stories.add(userStory);
                        }
                        story.setUserStories(stories);
                        storyList.add(story);
                    }
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD(TAG,error.getMessage());
            }
        });

    }

    private void intialize() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new android.app.ProgressDialog(getContext());

    }

    private void fetchPosts() {
            database.getReference().child("posts")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            postModelList.clear();
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                PostModel post = dataSnapshot.getValue(PostModel.class);
                                post.setPostId(dataSnapshot.getKey());
                                postModelList.add(post);
                            }
                            try {
                                binding.rvDashboard.setAdapter(postAdapter);
                                binding.rvDashboard.hideShimmerAdapter();
                                postAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            PrintMessage.printLogD(TAG,error.getMessage());
                        }
                    });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}