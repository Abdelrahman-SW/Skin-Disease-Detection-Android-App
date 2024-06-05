package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterRecover extends AppCompatActivity {
    Button Continue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_recover);
        Continue = findViewById(R.id.Continue);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , Medicines_Activity.class);
                intent.putExtra("Protocol" , 0);
                intent.putExtra("AfterRecover" , true);
                intent.putExtra("F" , true);
                startActivity(intent);
            }
        });
    }
}