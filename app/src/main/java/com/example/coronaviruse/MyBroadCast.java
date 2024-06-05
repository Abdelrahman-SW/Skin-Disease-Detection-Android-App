package com.example.coronaviruse;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.coronaviruse.Activities.ResultActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class MyBroadCast extends BroadcastReceiver {
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    Context context ;
    ListenerRegistration listenerRegistration ;
    DocumentReference documentReference ;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context ;
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        if (intent.getAction() != null && (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)  || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
        || intent.getAction().equals("com.htc.intent.action.QUICKBOOT_POWERON"))) {
            Log.e("medical", "onReceiveBoot");
            PrepareBoot();
            return;
        }
        Log.e("medical", "onReceive");
        NewDay();
    }

    private void PrepareBoot() {
        listenerRegistration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                String medical = documentSnapshot.getString("YesOrNot");
                String day = documentSnapshot.getString("Day");
                int LastCounter = Integer.parseInt(day);
                Long Time = documentSnapshot.getLong("Time");
                if (medical.equals("1")) {
                    if (Time<System.currentTimeMillis()) {
                        if (documentSnapshot.getBoolean("Active"))
                            if (LastCounter<14)
                            ResultActivity.PrepareWork(Time, context);
                    }
                    else
                    if (LastCounter<14)
                    ResultActivity.PrepareWork(Time, context);
                }
                listenerRegistration.remove();
            }
        });
    }

    private void PrepareNotification(int day) {
        Log.e("cocococo" , "PrepareNotification") ;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.safety_suit);
        NotificationCompat.Builder buildNotification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("Covid", "Covid App", NotificationManager.IMPORTANCE_HIGH);// sound of the notification
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern((new long[]{1000, 500, 1000, 500, 1500, 500, 1500}));
                notificationChannel.setShowBadge(true);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setBypassDnd(true);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationChannel.setDescription("Send A notification for your Medical follow up");
                Log.e("ab_do" , "Should show lights " + notificationChannel.shouldShowLights());
                notificationManager.createNotificationChannel(notificationChannel);
            buildNotification = new NotificationCompat.Builder(context.getApplicationContext(), "Covid");
        }
        else {
            buildNotification = new NotificationCompat.Builder(context , "Covid") ;
            buildNotification.setPriority(NotificationCompat.PRIORITY_MAX);
            buildNotification.setSound(Uri.parse(String.valueOf(Settings.System.DEFAULT_NOTIFICATION_URI)));
            buildNotification.setVibrate((new long[]{1000, 500, 1000, 500, 1500, 500, 1500}));
            buildNotification.setLights(Color.RED, 2000, 500);
        }
        buildNotification.setSmallIcon(R.drawable.res_covid);
        buildNotification.setLargeIcon(largeIcon);
        buildNotification.setContentText("بدأ يوم المتابعة الجديد يمكنك الأن الدخول والمتابعة لمعرفة تطور حالتك الصحية");
        buildNotification.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("بدأ يوم المتابعة الجديد (المتابعة رقم " + day +  " )  يمكنك الأن الدخول والإجابه علي الأسئلة لمعرفة تطور حالتك الصحية")
        );
        buildNotification.setCategory(NotificationCompat.CATEGORY_ALARM);
        buildNotification.setAutoCancel(true);
        String Title = "بدأ يوم المتابعة الجديد !" ;
        buildNotification.setContentTitle(Title);
        buildNotification.setTicker(Title);
        buildNotification.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        buildNotification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Intent intent = new Intent(context, Medical_follow_up.class) ;
        intent.putExtra("Day" , String.valueOf(day));
        int ld = day-1;
        intent.putExtra("LastDay" , String.valueOf(ld));
        intent.putExtra("medical" , "1") ;
        buildNotification.setContentIntent(PendingIntent.getActivity(context , 101 , intent , PendingIntent.FLAG_UPDATE_CURRENT)) ;
        Notification notification = buildNotification.build();
        notificationManager.notify(101 , notification);
        Log.e("cocococo" , "PrepareNotification") ;
    }

    public void NewDay() {
        Log.e("medical", "NewDay");
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        listenerRegistration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                String day = documentSnapshot.getString("Day");
                Log.e("medical", "day " + day);
                int day_int = Integer.parseInt(day);
                day_int++;
                user.put("Day", String.valueOf(day_int));
                user.put("Active" , false);
                int finalDay_int = day_int;
                listenerRegistration.remove();
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("medical", "onSuccess");
                        PrepareNotification(finalDay_int);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("medical", "onFailure " + e.getMessage());
                    }
                });
            }
        });

    }

}