package com.example.coronaviruse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.coronaviruse.Activities.Male_femaleActivity;
import com.example.coronaviruse.Activities.QuestionsActivity;
import com.example.coronaviruse.Activities.takeOrUpload;
import com.example.coronaviruse.databinding.ActivityAgeBinding;

public class AgeActivity extends AppCompatActivity {
    ActivityAgeBinding binding ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.NumberPicker.setMinValue(1);
        binding.NumberPicker.setMaxValue(100);
        binding.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , takeOrUpload.class) ;
                Log.e("ab_do " , "Age1 " + binding.NumberPicker.getValue());
//                intent.putExtra("Age" , binding.NumberPicker.getValue());
//                intent.putExtra("new" , getIntent().getBooleanExtra("new" , false));
//                intent.putExtra("username" , getIntent().getStringExtra("username") !=null ?  getIntent().getStringExtra("username") : "غير محدد" );
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });

    }
}