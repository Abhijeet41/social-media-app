package com.abhi41.socialmediaapp.repository;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.abhi41.socialmediaapp.model.JWS;
import com.abhi41.socialmediaapp.model.JWSRequest;
import com.abhi41.socialmediaapp.untils.Constants;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.abhi41.socialmediaapp.untils.VolleySingleTon;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DashBoardRepository {
    private static final String TAG = "DashBoardRepository";
    public static DashBoardRepository dashBoardRepository;
    private  Context mContext;

    public MutableLiveData<Boolean> mCts = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIntegrity = new MutableLiveData<>();

    public DashBoardRepository(Context context) {
        this.mContext = context;
    }

    public static DashBoardRepository getInstance(Context context){

        if (dashBoardRepository == null){
            dashBoardRepository = new DashBoardRepository(context);

        }
        return dashBoardRepository;
    }

    public MutableLiveData<Boolean> getmCts(){
        return mCts;
    }
    public MutableLiveData<Boolean> getmIntegrity(){
        return mIntegrity;
    }

    public void apiFetchSafetynet(String jwsResult,String apiKey){

        StringRequest request = new StringRequest(
                Request.Method.POST, Constants.getAndroidCheck(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean result = jsonObject.getBoolean("isValidSignature");
                            if (result)
                            {
                                decodeJWS(jwsResult);
                            }else {
                                decodeJWS(jwsResult);
                                PrintMessage.printLogD("dashboardRepo","error");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PrintMessage.printLogD(TAG,error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("key",apiKey);

                return params;
            }
        };
        VolleySingleTon.getInstance(mContext).addToRequestQueue(request);

    }


    private void decodeJWS(String jwsString) {

        byte[] json = Base64.decode(jwsString.split("[.]")[1],Base64.DEFAULT);
        String text = new String(json, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        JWS jws = gson.fromJson(text, JWS.class);

        displayResults(jws.isBasicIntegrity(), jws.isCtsProfileMatch());
    }


    private void displayResults(boolean integrity, boolean cts) {

                if (cts){
                    mCts.setValue(true);
                }else {
                    mCts.setValue(false);
                }

                if (integrity){
                    mIntegrity.setValue(true);
                }else {
                    mIntegrity.setValue(false);
                }
    }

}
