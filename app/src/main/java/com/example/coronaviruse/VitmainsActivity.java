package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coronaviruse.Activities.Main;

public class VitmainsActivity extends AppCompatActivity {
    Button Con ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitmains);
        Con = findViewById(R.id.Continue);
        Con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , Medicines_Activity.class);
                intent.putExtra("Protocol" , 0);
                intent.putExtra("F" , true);
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
            }
        });
    }
}