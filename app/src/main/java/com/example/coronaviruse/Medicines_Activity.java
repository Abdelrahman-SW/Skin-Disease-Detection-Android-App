package com.example.coronaviruse;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;
import com.example.coronaviruse.Activities.Main;
import com.example.coronaviruse.databinding.ActivityMedicinesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class Medicines_Activity extends AppCompatActivity {
    ActivityMedicinesBinding binding ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    String userID;
    DocumentReference noteRef;
    List<Medicine> medicines = new ArrayList<>();
    Medicine_adapters medicine_adapters ;
    ProgressDialog progressDialog ;
    int Protocol ;
    boolean AfterRecover ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init();
        ShowDialog(this);
    }
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void Init() {
        binding = ActivityMedicinesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Protocol = getIntent().getIntExtra("Protocol" , 0);
        Toolbar toolbar = findViewById(R.id.Toolbar) ;
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!getIntent().getBooleanExtra("F", false));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    private void SetUpViewPager(int count) {
        medicine_adapters = new Medicine_adapters(this, medicines, binding.getRoot());
        binding.viewPager.setAdapter(medicine_adapters);
        SetListeners(count);
    }

    private void SetListeners(int count) {
        binding.next.setOnClickListener(v -> {
            int pos = binding.viewPager.getCurrentItem(); // current page
            pos++; // Go To next Page
            binding.viewPager.setCurrentItem(pos);
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = binding.viewPager.getCurrentItem(); // current page
                if (pos==0) {
                    finish();
                    return;
                }
                pos--; // Go To next Page
                binding.viewPager.setCurrentItem(pos);
            }
        });
        binding.con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Protocol == 0) {
                    startActivity(new Intent(getBaseContext(), Main.class));
                    finish();
                    progressDialog.dismiss();
                    return;
                }
                if (!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("HasReminder", false)) {
                    startActivity(new Intent(getBaseContext(), Reminder_Activity.class));
                overridePendingTransition(0, R.anim.fade_out);
            }
               else
                   startActivity(new Intent(getBaseContext(), Main.class));
                overridePendingTransition(0 , R.anim.fade_out);
                   finish();
                   progressDialog.dismiss();
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("ab_do"  , "onPageSelected " + position);
                if (position==count) {
                    LoadLastScreen();
                }
                else LoadNormalScreen();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void LoadNormalScreen() {
        binding.next.setVisibility(View.VISIBLE);
        binding.back.setVisibility(View.VISIBLE);
        binding.con.setVisibility(View.GONE);
    }

    private void LoadLastScreen() {
        binding.next.setVisibility(View.GONE);
        binding.back.setVisibility(View.GONE);
        binding.con.setVisibility(View.VISIBLE);
        binding.con.setAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.started_btn_anim));
    }



    @Override
    protected void onStart()
    {
        Log.e("ab_do" , "onStart");
        if (Protocol==1) {
            if (getIntent().getBooleanExtra("AfterRecover", false)) {
                Log.e("ab_do" , "GetAfterRecover");
                GetAfterRecover();
            }
            else {
                Protocol();
                Log.e("ab_do", "Protocol");
            }
        }
        else {
                Log.e("ab_do" , "Vitamins");
                Vitamins();
            }
        super.onStart();
    }

    private void GetAfterRecover() {
        Log.e("ab_do" , "GetAfterRecover");
        noteRef = db.collection("Result").document("After Recover");
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e("ab_do" , "RecoverSuccess");
                    ProtocolMedicines protocolMedicines = documentSnapshot.toObject(ProtocolMedicines.class);
                    protocolMedicines.setDocumentId(documentSnapshot.getId());
                    String documentId = protocolMedicines.getDocumentId();
                    String first = protocolMedicines.getFirst();
                    String second = protocolMedicines.getSecond();
                    Log.e("ab_do" , "f " + first);
                    Log.e("ab_do" , "S " + second);
                    medicines.add(new Medicine(first.substring(0 , 17) , first.substring(17)));
                    medicines.add(new Medicine(second.substring(0 , 9) , second.substring(9)));
                    SetUpViewPager(1);
                    progressDialog.dismiss();
                    Log.e("ab_do" , "Vitmans");
                }
                else {
                    Log.e("ab_do" , "Vitmansnot");
                    Toast.makeText(Medicines_Activity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Vitamins() {
        noteRef = db.collection("Result").document("Negative");
        Log.e("ab_do" , "VitmansA");
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e("ab_do" , "VitmansonSuccess");
                    ProtocolMedicines protocolMedicines = documentSnapshot.toObject(ProtocolMedicines.class);
                    protocolMedicines.setDocumentId(documentSnapshot.getId());
                    String documentId = protocolMedicines.getDocumentId();
                    String first = protocolMedicines.getFirst();
                    String second = protocolMedicines.getSecond();
                    String third = protocolMedicines.getThird();
                    Log.e("ab_do" , "f " + first);
                    Log.e("ab_do" , "S " + second);
                    Log.e("ab_do" , "T " + third);
                    medicines.add(new Medicine(first.substring(0 , 10) , first.substring(10)));
                    medicines.add(new Medicine(second.substring(0 , 8) , second.substring(8)));
                    medicines.add(new Medicine(third.substring(0 , 20) , third.substring(20)));
                    SetUpViewPager(2);
                    progressDialog.dismiss();
                    Log.e("ab_do" , "Vitmans");
                }
                else {
                    Log.e("ab_do" , "Vitmansnot");
                    Toast.makeText(Medicines_Activity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ab_do" , "onFailure");
                Toast.makeText(Medicines_Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Protocol() {
        noteRef = db.collection("Result").document("Positive");
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ProtocolMedicines protocolMedicines = documentSnapshot.toObject(ProtocolMedicines.class);
                    if (protocolMedicines==null) return;
                    protocolMedicines.setDocumentId(documentSnapshot.getId());
                    String First = protocolMedicines.getFirst();
                    String Second = protocolMedicines.getSecond();
                    String Third = protocolMedicines.getThird();
                    String Fourth = protocolMedicines.getFourth();
                    String Fifth = protocolMedicines.getFifth();
                    String Sixth = protocolMedicines.getSixth();
                    String Seven = protocolMedicines.getSeven();
                    String Eight = protocolMedicines.getEight();
                    Log.e("ab_do" , Fourth.substring(14) ) ;
                    medicines.add(new Medicine(First.substring(0,11) , First.substring(11) )) ;
                    medicines.add(new Medicine(Second.substring(0,13) , Second.substring(13) )) ;
                    medicines.add(new Medicine(Third.substring(0,16) , Third.substring(16))) ;
                    medicines.add(new Medicine(Fourth.substring(0,14), Fourth.substring(14) )) ;
                    medicines.add(new Medicine(Fifth.substring(0,15), Fifth.substring(15))) ;
                    medicines.add(new Medicine(Sixth.substring(0,8) , Sixth.substring(8))) ;
                    medicines.add(new Medicine(Seven.substring(0,8), Seven.substring(8))) ;
                    medicines.add(new Medicine(Eight.substring(0,10) , Eight.substring(10))) ;
                    progressDialog.dismiss();
                    SetUpViewPager(7);
                } else {
                    Toast.makeText(Medicines_Activity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Medicines_Activity.this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("abbb" , "Erorr " + e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        int pos = binding.viewPager.getCurrentItem() ;
        if (pos==0) {
            super.onBackPressed();
            finish();
        }
        else {
            pos -- ;
            binding.viewPager.setCurrentItem(pos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}