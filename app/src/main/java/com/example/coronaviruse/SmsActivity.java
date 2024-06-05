package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coronaviruse.Activities.Main;

public class SmsActivity extends AppCompatActivity {
    Button SendSms , Skip ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Skip = findViewById(R.id.Skip);
        SendSms = findViewById(R.id.Continue);
        SendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , SendSmsActivity.class));
                overridePendingTransition(0 , R.anim.fade_out);
            }
        });
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , Main.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }
}