package com.abhi41.socialmediaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.socialmediaapp.model.JWSRequest;
import com.abhi41.socialmediaapp.repository.DashBoardRepository;
import com.abhi41.socialmediaapp.untils.Constants;
import com.abhi41.socialmediaapp.untils.VolleySingleTon;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class DashBoardViewModel extends AndroidViewModel {
    private DashBoardRepository dashBoardRepository;
    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        dashBoardRepository = DashBoardRepository.getInstance(getApplication());

    }

    public void apiFetchSafetynet(String result,String apiKey){
         dashBoardRepository.apiFetchSafetynet(result, apiKey);
    }

    public MutableLiveData<Boolean> getmCts(){
        return dashBoardRepository.getmCts();
    }

    public MutableLiveData<Boolean> getmIntegrity(){
        return dashBoardRepository.getmIntegrity();
    }


}
