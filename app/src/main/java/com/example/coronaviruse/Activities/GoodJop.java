package com.example.coronaviruse.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.coronaviruse.R;
import com.example.coronaviruse.SmsActivity;

public class GoodJop extends AppCompatActivity {
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_jop);
        button = findViewById(R.id.Continue) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , SmsActivity.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }
}