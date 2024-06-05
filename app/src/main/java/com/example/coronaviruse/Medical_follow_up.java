package com.example.coronaviruse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.coronaviruse.Activities.QuestionsActivity;
import com.example.coronaviruse.databinding.ActivityMedicalFollowUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class Medical_follow_up extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    ActivityMedicalFollowUpBinding binding ;
    DocumentReference documentReference ;
    String Day , LastDay , medical ;
    ListenerRegistration listenerRegistration ;
    Activity activity ;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicalFollowUpBinding.inflate(getLayoutInflater());
        fAuth = FirebaseAuth.getInstance();
        View view = binding.getRoot();
        setContentView(view);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userId);
        activity = this ;
        // TODO: 2/20/2021 check if the user have a  Medical_follow_up or not to update the UI according to this
        // TODO: 2/20/2021 Retrieve last scorers
        Init();
    }

   private void Init() {
               Intent intent = getIntent() ;
               Day = intent.getStringExtra("Day") ;
               LastDay = intent.getStringExtra("LastDay");
               medical = intent.getStringExtra("medical") ;
                Log.e("medical", "Day " + Day);
                Log.e("medical", "LastDay " + LastDay);
                Log.e("medical", "medical " + medical);
               UpdateUI(medical , Day , LastDay);
            }


    private void UpdateUI(String medical , String day , String Last_day) {
        if (medical.equals("0")) {
            NoActiveMedicalFollowUp();
        }
        else {
            ActiveMedical(day , Last_day);
        }
    }

    private void ActiveMedical(String day , String Last_day) {
        if (day.equals(Last_day)) {
            if (day.equals("14")) {
                binding.title.setText("أنتهت المتابعة اليومية الخاصه بك مع التطبيق نتمني أن تكون في حالة صحية أحسن الان إذا شعرت فأي وقت بتعب يمكنك بدء متابعة جديدة مع التطبيق او إستشارة الطبيب إن لزم الأمر");
                binding.img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_thumbs_up));
                binding.Continue.setText("نتائج المتابعات السابقة");
                binding.Continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), LastRecordsActivity.class);
                        intent.putExtra("Day" , Integer.parseInt(day));
                        startActivity(intent);
                        finish();
                    }
                });
                binding.AfterRecover.setVisibility(View.VISIBLE);
                binding.AfterRecover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         startActivity(new Intent(getBaseContext() , AfterRecover.class));
                         finish();
                    }
                });
                return;
            }
            binding.AfterRecover.setVisibility(View.GONE);
            binding.title.setText("تعال غدا لإستكمال المتابعة اليومية الخاص بك");
            binding.img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_microscope));
            binding.Continue.setText("نتائج المتابعات السابقة");
            binding.Continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), LastRecordsActivity.class);
                    intent.putExtra("Day" , Integer.parseInt(day));
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
//            Intent intent = new Intent(getBaseContext() , QuestionsActivity.class) ;
//            intent.putExtra("new" , false);
//            startActivity(intent);
//            finish();
            Intent intent = new Intent(getBaseContext() , MedicalActivity.class) ;
            intent.putExtra("Day" , day);
            startActivity(intent);
            finish();

        }


    }

    private void AddMedicalListener() {
        listenerRegistration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ActiveMedical(value.getString("Day") , LastDay);
                Log.e("cocococo" , "onEvent " + value.getString("Day"));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("cococo", "onStart");
        if (medical.equals("1"))
        AddMedicalListener();
    }

    private void NoActiveMedicalFollowUp() {
        binding.title.setText("ليس لديك متابعة يومية مع التطبيق يمكنك بدء كشف جديد وبدء المتابعة");
        binding.img.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_question_mark));
        binding.Continue.setText("بدء الكشف");
        binding.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(activity) ;
                final String[] username = {"غير محدد"};
                documentReference.addSnapshotListener(activity , new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        username[0] = value.getString("UserName");
                        Intent intent = new Intent(getBaseContext() , AgeActivity.class) ;
                        intent.putExtra("new" , true);
                        intent.putExtra("username", username[0]);
                        Log.e("ab_do" , "username " + username[0]);
                        //intent.putExtra("Longitude" , getIntent().getDoubleExtra("Longitude" , -1));
                        //intent.putExtra("Latitude" , getIntent().getDoubleExtra("Latitude" , -1));
                        progressDialog.dismiss();
                        startActivity(intent);
                        overridePendingTransition(0 , R.anim.fade_out);
                        finish();
                    }
                });

            }
        });
    }

    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


}