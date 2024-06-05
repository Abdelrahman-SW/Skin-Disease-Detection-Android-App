package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.coronaviruse.Activities.GoodJop;
import com.example.coronaviruse.Activities.Main;
import com.example.coronaviruse.databinding.ActivityReminderBinding;

public class Reminder_Activity extends AppCompatActivity {
    ActivityReminderBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReminderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot() ;
        setContentView(view);
        binding.YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("HasReminder" , true).apply();
                startActivity(new Intent(getBaseContext() , GoodJop.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
        binding.NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , SmsActivity.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });

    }
}