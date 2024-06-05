package com.example.coronaviruse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coronaviruse.Activities.LoadingResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewResultActivity extends AppCompatActivity {
    int Current_Counter ;
    Long Last_Counter ;
    String Day ;
    Button button ;
    ImageView img ;
    TextView Report , Txt ;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    Activity activity ;
    DocumentReference documentReference ;
    Map<String, Object> user ;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_result);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        user = new HashMap<>();
        button = findViewById(R.id.Continue);
        img = findViewById(R.id.img);
        Report = findViewById(R.id.Report);
        Txt = findViewById(R.id.txt);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Medical_follow_up.class) ;
                intent.putExtra("Day" , Day);
                intent.putExtra("LastDay" , Day);
                intent.putExtra("medical" , "1") ;
                startActivity(intent);
                finish();
            }
        });
        Current_Counter = getIntent().getIntExtra("CurrentCounter", -1);
        Last_Counter = getIntent().getLongExtra("LastCounter", -1);
        Log.e("cocococo" , "CurrentCounter " + Current_Counter);
        Log.e("cocococo" , "LastCounter " + Last_Counter);
        Day = getIntent().getStringExtra("Day");
        UpdateUI(Day , Current_Counter , Last_Counter) ;
    }

    private void UpdateUI(String day, int current_counter, Long last_counter) {
        Txt.setText("نتيجة اليوم  " + day);
        if (current_counter==last_counter) {
            equal();
        }
        else if (current_counter>last_counter)
            bad();
        else
            good();
        UpdateData();
    }

    private void UpdateData() {
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("medical" , "onSuccessUpdate");
                button.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(button , "يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void good() {
        Report.setText("خبر جيد ! يبدو أن حالتك الصحية تحسنت كتيرا عن اليوم السابق استمر في أخذ العلاج وتعال في موعد المتابعة القادمة");
        img.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_like__1_));
        user.put("Day"+Day+"S" , "1");
    }

    private void bad() {
        Report.setText(" يبدو أن حالتك الصحية ليست علي مايرام ! النتائج تشير أنك تشعر بالتعب أكثر اليوم استمر في أخذ العلاج وإذا شعرت بسوء في حالتك الصحية توجه إلي الطبيب فورا أو أتصل برقم الطوارئ الموجود في الشاشة الرئيسية للتطبيق");
        img.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.covid_high));
        user.put("Day"+Day+"S" , "-1");
    }

    private void equal() {
        Report.setText("حالتك الصحية مستقرة يبدو أنك لازلت تعاني من نفس الأعراض استمر في أخذ العلاج وتعال في موعد المتابعة القادمة وإذا لم تشعر بتحسن يمكنك إستشارة الطبيب");
        img.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_thumb_up));
        user.put("Day"+Day+"S" , "0");
    }


}