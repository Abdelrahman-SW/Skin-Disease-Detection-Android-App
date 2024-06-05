package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coronaviruse.Activities.QuestionsActivity;

public class MedicalActivity extends AppCompatActivity {
    TextView Count ;
    Button con ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        con = findViewById(R.id.Continue) ;
        Count = findViewById(R.id.count);
        Count.setText( "المتابعة رقم "+ getIntent().getStringExtra("Day"));
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getBaseContext() , QuestionsActivity.class) ;
              intent.putExtra("new" , false);
              startActivity(intent);
               finish();
            }
        });
    }


}