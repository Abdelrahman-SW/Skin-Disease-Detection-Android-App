package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.coronaviruse.databinding.ActivityIntroToProtocolBinding;

public class IntroToProtocolActivity extends AppCompatActivity {
    ActivityIntroToProtocolBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroToProtocolBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getBaseContext() , Medicines_Activity.class) ;
                intent.putExtra("F" , true);
                intent.putExtra("Protocol" , 1);
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }
}