package com.example.coronaviruse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class LastRecordsActivity extends AppCompatActivity {
    ProgressDialog progressDialog ;
    int Day ;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    Activity activity ;
    DocumentReference documentReference ;
    RecyclerView recyclerView ;
    Medical_Adapter medical_adapter ;
    List<Medical_item> medical_items ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_records);
        setSupportActionBar(findViewById(R.id.Toolbar));
        recyclerView = findViewById(R.id.MyRecyclerView);
        medical_items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medical_adapter = new Medical_Adapter(this);
        recyclerView.setAdapter(medical_adapter);
        //setting up the title to actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ShowDialog(this);
        Day = getIntent().getIntExtra("Day" , -2);
        Log.e("menaa" , "Day "+ Day);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        GetData();
    }

    private void GetData() {
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                medical_items.clear();
                for (int i = 1; i <= Day; i++) {
                    String report_val = documentSnapshot.getString("Day"+ i +"S") ;
                    Log.e("menaaaa", "Day " + i + "  " + report_val);
                    String Report = " " ;
                    int drawable_id = -1  ;
                    if (report_val.equals("0")) {
                        // nothing new
                        Report = "حالتك الصحية مستقرة" ;
                        drawable_id = R.drawable.ic_minus_horizontal_straight_line ;
                    }
                    else if (report_val.equals("1")) {
                        Report = "حالتك الصحية تحسنت بشكل كبير" ;
                        drawable_id = R.drawable.ic_check ;
                    }
                    else if (report_val.equals("-1")) {
                        Report = "حالتك الصحية غير مستقرة اليوم" ;
                        drawable_id = R.drawable.ic_warning__1_ ;
                    }
                    Log.e("menaaaa", "drawable " + drawable_id);
                    medical_items.add(new Medical_item(String.valueOf(i) , Report , drawable_id));
                }

                medical_adapter.SetAdapter(medical_items);
                progressDialog.dismiss();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}