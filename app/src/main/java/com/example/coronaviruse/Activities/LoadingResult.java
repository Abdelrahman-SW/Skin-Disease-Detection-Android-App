package com.example.coronaviruse.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coronaviruse.R;

public class LoadingResult extends AppCompatActivity {
    ImageView wait ;
    LinearLayout wait_root;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_result);
        wait_root = findViewById(R.id.Wait_root) ;
        wait = findViewById(R.id.wait_img) ;
//        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(wait_txt, "textColor", new ArgbEvaluator(), Color.BLACK, Color.WHITE)
//                .setDuration(1000);
//        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.start();
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        wait.setAnimation(animation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), ResultActivity.class) ;
                intent.putExtra("Class" , getIntent().getIntExtra("Class" , 0));
//                intent.putExtra("Type" , getIntent().getIntExtra("Type" , 1));
//                intent.putExtra("Age" , getIntent().getIntExtra("Age" , 1));
//                intent.putExtra("username" , getIntent().getStringExtra("username"));
//                intent.putExtra("Result" , getIntent().getStringExtra("Result"));
//                intent.putStringArrayListExtra("A" , getIntent().getStringArrayListExtra("A"));
                startActivity(intent);
                finish();
            }
        } , 2500) ;
    }
}