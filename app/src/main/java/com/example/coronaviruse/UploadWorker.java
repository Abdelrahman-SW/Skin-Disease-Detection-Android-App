package com.example.coronaviruse;


import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UploadWorker extends Worker {
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    Activity context ;
    public UploadWorker(
            @NonNull Activity context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.
        NewDay();
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    private void PrepareNextWork() {
        Log.e("medical" , "PrepareNextWork");
        WorkRequest myWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setInitialDelay(2, TimeUnit.MINUTES)
                        .addTag("Repeat")
                        .build();
        WorkManager
                .getInstance(context)
                .enqueue(myWorkRequest);
    }

    private void NewDay() {
        Log.e("medical" , "NewDay");
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        documentReference.addSnapshotListener(context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e){
                String day = documentSnapshot.getString("Day");
                Log.e("medical", "day " + day);
                int day_int = Integer.parseInt(day);
                day_int++;
                user.put("Day", String.valueOf(day_int));
                if (day_int<14)
                    PrepareNextWork();
            }
        });

    }
}
