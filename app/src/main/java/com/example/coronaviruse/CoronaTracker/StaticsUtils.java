package com.example.coronaviruse.CoronaTracker;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.coronaviruse.R;


public class StaticsUtils {

    private ProgressDialog progressDialog;



    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void DismissDialog() {
        progressDialog.dismiss();
    }


}
