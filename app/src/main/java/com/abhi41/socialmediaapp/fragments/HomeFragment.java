package com.abhi41.socialmediaapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhi41.socialmediaapp.R;
import com.abhi41.socialmediaapp.adapter.DashBoardAdapter;
import com.abhi41.socialmediaapp.adapter.StoryAdapter;
import com.abhi41.socialmediaapp.databinding.FragmentHomeBinding;
import com.abhi41.socialmediaapp.model.DashBoardModel;
import com.abhi41.socialmediaapp.model.StoryModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private List<StoryModel> storyModelList = new ArrayList<>();
    private List<DashBoardModel> dashBoardModelList = new ArrayList<>();

    public HomeFragment() {
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

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        storyModelList.add(new StoryModel(R.drawable.danny,R.drawable.video_player,R.drawable.vegeta,"Danny"));
        storyModelList.add(new StoryModel(R.drawable.vegeta,R.drawable.ic_live,R.drawable.danny,"Danny"));
        storyModelList.add(new StoryModel(R.drawable.danny,R.drawable.video_player,R.drawable.child_image,"Danny"));
        storyModelList.add(new StoryModel(R.drawable.danny,R.drawable.video_player,R.drawable.child_image,"Danny"));
        storyModelList.add(new StoryModel(R.drawable.danny,R.drawable.video_player,R.drawable.child_image,"Danny"));
        storyModelList.add(new StoryModel(R.drawable.danny,R.drawable.video_player,R.drawable.child_image,"Danny"));

        StoryAdapter storyAdapter = new StoryAdapter(storyModelList,getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.storyRv.setLayoutManager(manager);
        //because our recycler view is inside our horizontel scrollview
        binding.storyRv.setNestedScrollingEnabled(false);
        binding.storyRv.setAdapter(storyAdapter);

        dashBoardModelList.add(new DashBoardModel(
                R.drawable.profile_user,R.drawable.danny,R.drawable.ic_outline_bookmark_border_24,
                "Denis kane","Travler","464","12","15"
                ));

        dashBoardModelList.add(new DashBoardModel(
                R.drawable.profile_user,R.drawable.vegeta,R.drawable.ic_outline_bookmark_border_24,
                "vegeta","Travler","250","38","35"
        ));

        dashBoardModelList.add(new DashBoardModel(
                R.drawable.profile_user,R.drawable.cruise,R.drawable.ic_outline_bookmark_border_24,
                "Tom Cruise","Travler","380","50","12"
        ));

        dashBoardModelList.add(new DashBoardModel(
                R.drawable.profile_user,R.drawable.goku,R.drawable.ic_outline_bookmark_border_24,
                "Goku","Travler","1220","250","60"
        ));

        dashBoardModelList.add(new DashBoardModel(
                R.drawable.profile_user,R.drawable.friza,R.drawable.ic_outline_bookmark_border_24,
                "Frize","Travler","5561","650","122"
        ));

        DashBoardAdapter adapter = new DashBoardAdapter(dashBoardModelList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvDashboard.setLayoutManager(layoutManager);
        binding.rvDashboard.setNestedScrollingEnabled(false);
        binding.rvDashboard.setAdapter(adapter);

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}