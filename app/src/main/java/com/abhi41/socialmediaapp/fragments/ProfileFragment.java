package com.abhi41.socialmediaapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhi41.socialmediaapp.adapter.FollowersAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentProfileBinding;
import com.abhi41.socialmediaapp.model.FollowModel;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private List<FollowModel> friendList;
    ActivityResultLauncher<Intent> activityResultCoverImage, activityResultProfileImage, activityResultCrop;

    FollowersAdapter followersAdapter;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private Uri imgUri;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intialization();
        setActivityResult();
    }

    private void intialization() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getListOfUsers();
        getClickListener();
        fetchImageFromFirebase();
        return view;
    }


    private void getClickListener() {
        binding.imgChangeCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(activityResultCoverImage);
            }
        });

        binding.imgVerifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(activityResultProfileImage);
            }
        });
    }


    private void getListOfUsers() {
        friendList = new ArrayList<>();
   /*     friendList.add(new FollowModel(R.drawable.natalie));
        friendList.add(new FollowModel(R.drawable.alexandra));
        friendList.add(new FollowModel(R.drawable.rebecca));
        friendList.add(new FollowModel(R.drawable.rose_byrne));
        friendList.add(new FollowModel(R.drawable.monica));
        friendList.add(new FollowModel(R.drawable.angelina));
        friendList.add(new FollowModel(R.drawable.kristen));*/

        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FollowModel followModel = data.getValue(FollowModel.class);
                    friendList.add(followModel);
                    followersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                PrintMessage.printLogD(TAG, error.getMessage());
            }
        });


        followersAdapter = new FollowersAdapter(friendList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.friendRv.setLayoutManager(manager);
        binding.friendRv.setAdapter(followersAdapter);
    }

    private void getImage(ActivityResultLauncher<Intent> resultLauncher) {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);

    }


    private void setActivityResult() {
        activityResultCoverImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        if (result.getData() != null) {
                            Uri uri = result.getData().getData();
                            imgUri = uri;
                            CropImage.activity(uri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(getContext(), ProfileFragment.this);

                        }
                    } else if (result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                        if (result.getResultCode() == RESULT_OK) {
                            Uri resultUri = cropResult.getUri();
                            setImageCover(resultUri);
                        } else {
                            Uri noCropUri = imgUri;
                            setImageCover(noCropUri);
                        }
                    }
                });

        activityResultProfileImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // There are no request codes
                            if (result.getData() != null) {
                                Uri uri = result.getData().getData();

                                imgUri = uri;
                                CropImage.activity(uri)
                                        .start(getContext(), ProfileFragment.this);

                            }
                        } else if (result.getResultCode() == RESULT_OK && result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                            CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                            if (result.getResultCode() == RESULT_OK) {
                                Uri resultUri = cropResult.getUri();
                                setProfileImage(resultUri);
                            } else {
                                Uri noCropUri = imgUri;
                                setProfileImage(noCropUri);
                            }
                        }
                    }
                });

    }

    private void setImageCover(Uri uri) {
        binding.imgCover.setImageURI(uri);

        //store image in firebase
        final StorageReference reference = storage
                .getReference()
                .child("cover_photo")
                .child(FirebaseAuth.getInstance().getUid());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //   PrintMessage.printToastMessage(getContext(), "Cover Photo Saved");
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null) {
                            database.getReference()
                                    .child("Users")
                                    .child(auth.getUid())
                                    .child("coverPhoto")
                                    .setValue(uri.toString());

                        }
                    }
                });
            }
        });
    }

    private void setProfileImage(Uri uri) {
        binding.imgProfile.setImageURI(uri);

        //store image in firebase
        final StorageReference reference = storage
                .getReference()
                .child("profile_photo")
                .child(FirebaseAuth.getInstance().getUid());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //   PrintMessage.printToastMessage(getContext(), "Cover Photo Saved");
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null) {
                            database.getReference()
                                    .child("Users")
                                    .child(auth.getUid())
                                    .child("profilePhoto")
                                    .setValue(uri.toString());
                        }
                    }
                });
            }
        });
    }

    private void fetchImageFromFirebase() {
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            User user = snapshot.getValue(User.class);
                            Glide.with(getContext().getApplicationContext())
                                    .load(user.getCoverPhoto())
                                    .centerCrop()
                                    .into(binding.imgCover);

                            Glide.with(getContext().getApplicationContext())
                                    .load(user.getProfilePhoto())
                                    .centerCrop()
                                    .into(binding.imgProfile);

                            binding.txtUserName.setText(user.getName());
                            binding.txtProfession.setText(user.getProfession());
                            binding.txtFollowers.setText(String.valueOf(user.getFollowerCount()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}