package com.example.coronaviruse.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.coronaviruse.AgeActivity;
import com.example.coronaviruse.R;

public class Male_femaleActivity extends AppCompatActivity {
    ImageView Male , female ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_female);
        Male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , AgeActivity.class) ;
//                intent.putExtra("username" , getIntent().getStringExtra("username") !=null ?  getIntent().getStringExtra("username") : "غير محدد" );
//                intent.putExtra("Age" , getIntent().getIntExtra("Age" , 20));
//                Log.e("ab_do " , "Age2 " + getIntent().getIntExtra("Age" , 20));
//                intent.putExtra("new" , getIntent().getBooleanExtra("new" , false));
                //intent.putExtra("Type" , 1 ) ;
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , QuestionsActivity.class) ;
                intent.putExtra("new" , getIntent().getBooleanExtra("new" , false));
                intent.putExtra("Type" , 0) ;
                intent.putExtra("Age" , getIntent().getIntExtra("Age" , 20));
                intent.putExtra("username" , getIntent().getStringExtra("username") !=null ?  getIntent().getStringExtra("username") : "غير محدد" );
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }
}