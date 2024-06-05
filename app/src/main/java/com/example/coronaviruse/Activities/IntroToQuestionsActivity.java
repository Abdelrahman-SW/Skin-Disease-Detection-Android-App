package com.example.coronaviruse.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coronaviruse.AgeActivity;
import com.example.coronaviruse.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class IntroToQuestionsActivity extends AppCompatActivity {
    Button Continue ;
    TextView Not_now ;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    ProgressDialog progressDialog ;
    Activity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_to_main);
        activity = this ;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        Continue = findViewById(R.id.Continue);
        Not_now = findViewById(R.id.not_now);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(activity) ;
                final String[] username = {"غير محدد"};
                documentReference.addSnapshotListener(activity , new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        username[0] = value.getString("UserName");
                        Intent intent = new Intent(getBaseContext() , AgeActivity.class) ;
                        intent.putExtra("new" , true);
                        intent.putExtra("username", username[0]);
                        Log.e("ab_do" , "username " + username[0]);
                        //intent.putExtra("Longitude" , getIntent().getDoubleExtra("Longitude" , -1));
                        //intent.putExtra("Latitude" , getIntent().getDoubleExtra("Latitude" , -1));
                        progressDialog.dismiss();
                        startActivity(intent);
                        overridePendingTransition(0 , R.anim.fade_out);
                        finish();
                    }
                });
            }
        });
        Not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("YesOrNot", "0");
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getBaseContext() , Main.class));
                        overridePendingTransition(0 , R.anim.fade_out);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(Not_now ,"هناك خطأ ما يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}