package com.abhi41.socialmediaapp.untils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class PrintMessage {

    public static void printToastMessage(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }

    public static void printLogD(String TAG, String message) {
        Log.d(TAG, message);
    }

}
