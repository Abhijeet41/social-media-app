package com.abhi41.socialmediaapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.databinding.FragmentAddBinding;
import com.abhi41.socialmediaapp.model.PostModel;
import com.abhi41.socialmediaapp.model.User;

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

import java.util.Date;


public class AddPostFragment extends Fragment {

    FragmentAddBinding binding;
    String description;
    private Uri uri;
    private ActivityResultLauncher<Intent> acResultPostImg;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;

    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);

        onListener();
        setActivityResult();
        getDataFromFirebase();

        return binding.getRoot();
    }

    private void onListener() {

        binding.edtPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description = binding.edtPostDescription.getText().toString();

                if (!description.isEmpty()) {
                    enableButton();
                } else {
                    disableButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery(acResultPostImg);
            }
        });

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostToServer();
            }
        });

    }

    private void addPostToServer() {

        com.abhi41.socialmediaapp.untils.ProgressDialog.showProgressDialog(progressDialog);

        final StorageReference reference = storage.getReference().child("poste")
                .child(FirebaseAuth.getInstance().getUid())
                .child(new Date().getTime() + "");
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {

                        PostModel postModel = new PostModel();
                        postModel.setPostImage(uri.toString());
                        postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                        postModel.setPostDescription(binding.edtPostDescription.getText().toString());
                        postModel.setPostedAt(new Date().getTime());

                        database.getReference().child("posts")
                                .push()
                                .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                com.abhi41.socialmediaapp.untils.ProgressDialog.hideProgressDialog(progressDialog);

                                binding.edtPostDescription.getText().clear();
                                //binding.imgPost.setImageResource(0);
                                binding.imgPost.setImageResource(android.R.color.transparent);
                                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void getDataFromFirebase() {

        database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            Glide.with(getContext())
                                    .load(user.getProfilePhoto())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.imgProfile);

                            binding.txtName.setText(user.getName());
                            binding.txtProfession.setText(user.getProfession());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void getImageFromGallery(ActivityResultLauncher<Intent> resultLauncher) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    private void setActivityResult() {
        acResultPostImg = registerForActivityResult(new ActivityResultContracts.
                StartActivityForResult(), result -> {

            if (result.getResultCode() == Activity.RESULT_OK) {

                if (result.getData() != null) {
                    uri = result.getData().getData();
                    binding.imgPost.setImageURI(uri);
                    binding.imgPost.setVisibility(View.VISIBLE);
                    enableButton();
                }

            }

        });
    }


    private void enableButton() {
        try {
            binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.follow_btn));

            binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
            binding.btnPost.setEnabled(true);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void disableButton() {
        try {
            binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.follow_active_btn));

            binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.color_grey_dark));
            binding.btnPost.setEnabled(false);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}