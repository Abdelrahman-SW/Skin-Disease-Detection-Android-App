package com.example.coronaviruse.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.coronaviruse.CoronaTracker.WorldDataActivity;
import com.example.coronaviruse.DoctorActivity;
import com.example.coronaviruse.FindNearbyHospitals.FindHospitals;
import com.example.coronaviruse.Medical_follow_up;
import com.example.coronaviruse.Medicines_Activity;
import com.example.coronaviruse.New_Medical_follow_up;
import com.example.coronaviruse.R;
import com.example.coronaviruse.UploadWorker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.concurrent.TimeUnit;

public class Main extends AppCompatActivity implements SensorEventListener {
    TextView textView;
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    String userID;
    FirebaseFirestore fStore ;
    String username = "" ;
    TextView user_name ;
    TextView Static ;
    TextView Medical_follow_up , New_Medical_follow_up ;
    FirebaseAuth fAuth;
    String userId;
    Activity activity;
    String medical ;
    String Day ;
    String LastDay ;
    Button Call ;
    ListenerRegistration listenerRegistration ;
    SensorManager sensorManager ;
    Sensor sensor ;
    boolean IsSenorAvailable = false ;
    float Current_X , Current_Y , Current_Z , Last_X , Last_Y , Last_Z , X_diff , Y_diff , Z_diff ;
    boolean ISFirstTimeShake = true;
    float shake_range = 12f ;
    long last_time  = 0 ;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        activity = this ;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) !=null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ;
            IsSenorAvailable = true ;
        }
        else {
            IsSenorAvailable = false ;
        }
        Call = findViewById(R.id.call);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calling();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Medical_follow_up = findViewById(R.id.Medical_follow_up) ;
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getBaseContext() , Login.class));
            finish();
            return;
        }
        else {
            Medical_follow_up.setEnabled(false);
            PrepareNavigationDrawer();
            ExtractUsername();
        }
        ImageView Nav_btn = findViewById(R.id.nav_btn) ;
        Static = findViewById(R.id.Statics);

        New_Medical_follow_up = findViewById(R.id.new_Medical_follow_up);
        Medical_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Log.e("medical", "Day " + Day);
                        Log.e("medical", "LastDay " + LastDay);
                        Intent intent = new Intent(getBaseContext(), Medical_follow_up.class) ;
                        intent.putExtra("Day" , Day);
                        intent.putExtra("LastDay" , LastDay);
                        intent.putExtra("medical" , medical) ;
                        startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                    }
                });

        New_Medical_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , New_Medical_follow_up.class));
                overridePendingTransition(0 , R.anim.fade_out);
            }
        });
        Static.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , WorldDataActivity.class));
            }
        });
        Nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ab_do" , "Click") ;
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });



        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("F", true)) {
            //PrepareWork();
        }
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //int day = sharedPreferences.getInt("Day", 1);
        //UpdateDay(day);

        // after recover
        // sms

    }

    private void Calling() {
            sensorManager.unregisterListener(this,sensor);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
            }
            else
            {
                String dial = "tel:" + 105;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

    @Override
    protected void onStart() {
        super.onStart();
        InitFirebase();
    }

    private void InitFirebase() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        listenerRegistration= documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                Medical_follow_up.setEnabled(true);
                String day = documentSnapshot.getString("Day");
                int LastCounter = Integer.parseInt(day);
                Day = documentSnapshot.getString("Day");
                LastDay = documentSnapshot.getString("LastDay");
                medical = documentSnapshot.getString("YesOrNot");
                Long Time = documentSnapshot.getLong("Time");
                if (medical==null || Day==null || LastDay==null)  {
                    medical = "0" ;
                    return;
                }
                if (medical.equals("1")) {
                    if (Time<System.currentTimeMillis()) {
                        if (documentSnapshot.getBoolean("Active"))
                            if (LastCounter<14)
                            ResultActivity.PrepareWork(Time, activity);
                          }
                    else
                    if (LastCounter<14)
                    ResultActivity.PrepareWork(Time, activity);
                }
            }});
    }

    @Override
    protected void onPause() {
        listenerRegistration.remove();
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    private void ExtractUsername() {
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null && value.getString("UserName") !=null)
                    username = value.getString("UserName");
                user_name.setText("Abdelrahman");
            }
        });
    }


    private void UpdateDay(int day) {
        switch (day) {
            case 1:
                textView.setText("day 1");
                break;
            case 2:
                textView.setText("day 2");
                break;
            case 3:
                textView.setText("day 3");
                break;
            case 4:
                textView.setText("day 4");
                break;
            case 5:
                textView.setText("day 5");
                break;
            case 6:
                textView.setText("day 6");
                break;
            case 7:
                textView.setText("day 7");
                break;
            default:
                textView.setText("Finished");
                break;
        }
    }

    private void PrepareWork() {
        WorkRequest myWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setInitialDelay(2, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(myWorkRequest);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("F", false).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.closeDrawer(GravityCompat.START);
        if (IsSenorAvailable) {
            sensorManager.registerListener(this , sensor , SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void PrepareNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout) ;
        NavigationView navigationView = findViewById(R.id.navdrawer);
        navigationView.setItemIconTintList(null);
        //user_name = navigationView.getHeaderView(0).findViewById(R.id.navtext);
        Log.e("ab_do" , "tttt" + username);
        //user_name.setText("Abdelrahman");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.sign_out) {
                    mAuth.signOut();
                    startActivity(new Intent(getBaseContext(), Login.class));
                    overridePendingTransition(0 , R.anim.fade_out);
                    finish();
                    return true;
                }
                if (itemId == R.id.hospitals) {
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("hos"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
                if (itemId == R.id.pharmacy) {
                    Log.i("abdo" , "ClickedP");
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("pharmacy"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
                if (itemId == R.id.Thedoctors) {
                    Log.i("abdo" , "ClickedD");
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("doctors"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
//                if (itemId == R.id.Protocol) {
//                    Log.e("ab_do" , "med " + Integer.parseInt(medical));
//                    Intent intent = new Intent(getBaseContext() , Medicines_Activity.class);
//                    intent.putExtra("Protocol" , Integer.parseInt(medical));
//                    intent.putExtra("F" , false);
//                    if (Integer.parseInt(Day)>=14) {
//                        Log.e("ab_do" , "AfterRecover");
//                        intent.putExtra("AfterRecover" , true);
//                    }
//                    startActivity(intent);
//                    overridePendingTransition(0 , R.anim.fade_out);
//                  return true;
//                }

//                if (itemId == R.id.world) {
//                    Intent intent = new Intent(getBaseContext() , MapsActivity.class) ;
//                    intent.putExtra("Corona" , true);
//                    startActivity(intent);
//                    overridePendingTransition(0 , R.anim.fade_out);
//                    return true;
//                }

                if (itemId == R.id.Doctor) {
                    Intent intent = new Intent(getBaseContext() , DoctorActivity.class) ;
                    startActivity(intent);
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            Current_X = event.values[0];
            Current_Y = event.values[1];
            Current_Z = event.values[2];
            if (!ISFirstTimeShake) {
            if (SystemClock.elapsedRealtime() - last_time < 3000)  {
                    return;
            }
            last_time = SystemClock.elapsedRealtime() ;
            X_diff = Math.abs(Current_X-Last_X);
            Y_diff = Math.abs(Current_Y-Last_Y);
            Z_diff = Math.abs(Current_Z-Last_Z);
            if (X_diff>shake_range && Y_diff>shake_range || X_diff>shake_range && Z_diff>shake_range ||
            Y_diff>shake_range && Z_diff>shake_range) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(VibrationEffect.createOneShot(500 , VibrationEffect.DEFAULT_AMPLITUDE));
                else
                    vibrator.vibrate(500);
                Calling();
            }
        }
        Last_X = Current_X ;
        Last_Y = Current_Y ;
        Last_Z = Current_Z ;
        ISFirstTimeShake = false ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}