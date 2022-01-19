package com.abhi41.socialmediaapp.untils;

import android.view.View;
import android.widget.ProgressBar;

import com.abhi41.socialmediaapp.R;
import com.iammert.tileprogressview.TiledProgressView;

public class ProgressDialog {

    public static void showProgressDialog(android.app.ProgressDialog dialog){
            dialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Post Uploading");
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
    }

    public static void hideProgressDialog(android.app.ProgressDialog dialog){
        dialog.dismiss();
    }

}
