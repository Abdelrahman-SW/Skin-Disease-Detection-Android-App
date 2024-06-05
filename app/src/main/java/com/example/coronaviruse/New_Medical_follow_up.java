package com.example.coronaviruse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.coronaviruse.Activities.QuestionsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class New_Medical_follow_up extends AppCompatActivity {
    Button button ;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    ListenerRegistration listenerRegistration ;
    DocumentReference documentReference ;
    ProgressDialog progressDialog ;
    Activity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__medical_follow_up);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        button = findViewById(R.id.Continue);
        activity = this ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(New_Medical_follow_up.this);
                // TODO: 2/20/2021 Remove The Result of this user and medical-follow profile if have one
                mAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                documentReference = fStore.collection("users").document(userID);
                listenerRegistration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                        String day = documentSnapshot.getString("Day");
                        final String[] username = {"غير محدد"};
                        Log.e("medical", "day " + day);
                        if (day==null) {
                            progressDialog.dismiss();
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
                            return;
                        }
                        int day_int = Integer.parseInt(day);
                        listenerRegistration.remove();
                        DeleteUserData(day_int);
                    }
                });
            }
        });
    }

    private void DeleteUserData(int Day) {
        Map<String, Object> user = new HashMap<>();
        user.put("Result", FieldValue.delete());
        user.put("YesOrNot", FieldValue.delete());
        user.put("LastDay", FieldValue.delete());
        user.put("Day", FieldValue.delete());
        user.put("Day0", FieldValue.delete());
        for (int i = 0 ; i<Day ; i++) {
            user.put("Day" + i, FieldValue.delete());
        }
        // for Loop ;
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(GetPendingIntentOfAlarm(getBaseContext() , new Intent(getBaseContext(), MyBroadCast.class)  , 100));
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private static PendingIntent GetPendingIntentOfAlarm(Context context , Intent intent , int RequestCode) {
        return PendingIntent.getBroadcast(context.getApplicationContext(),
                RequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        try {
            progressDialog.show();
        }catch (Exception e) {
            finish();
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}

