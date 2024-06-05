package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.coronaviruse.Activities.Login;
import com.example.coronaviruse.Activities.Main;

public class SplashActivity extends AppCompatActivity {
    Runnable runnable ;
    Handler handler = new Handler(Looper.getMainLooper()) ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext() , Main.class));
            }
        };
        handler.postDelayed(runnable , 1500);
    }
}