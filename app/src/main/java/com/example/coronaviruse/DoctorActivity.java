package com.example.coronaviruse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoctorActivity extends AppCompatActivity implements DoctorAdapter.ButtonClicked {
    RecyclerView recyclerView ;
    DoctorAdapter doctorAdapter ;
    List<DoctorItem> Doctors ;
    String Tel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = findViewById(R.id.MyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Doctors = new ArrayList<>();
        Doctors.add(new DoctorItem("د. كريم محمد" , "إستشاري أمراض جلدية" , false ,
                4.3f , "متاح من 8 ص حتي 9 م" , 8 , 13 , R.drawable.ic_doctor__2_,
                "https://m.me/amr.elshafay.7", "01141025780"));
        Doctors.add(new DoctorItem("د. سارة علي" , "طبيب أمراض جلدية وتجميل" , false ,
                4.1f , "متاح من 5 م حتي 3 ص" , 17 , 10 , R.drawable.ic_doctor__1_,
                "https://m.me/amr.elshafay.7" , "01124323049" ));
        Doctors.add(new DoctorItem("د. ستفين جورج" , "أخصائي أمراض جلدية" , false ,
                4.7f , "متاح من 3 م حتي 12 ص" , 15 , 9 , R.drawable.ic_doctor__2_,
                "https://m.me/amr.elshafay.7" , "01066358317"));
        Doctors.add(new DoctorItem("د. إيناس أحمد" , "إستشاري أمراض جلدية" , false ,
                4.8f , "متاح من 1 م حتي 11 م" , 13 , 10 , R.drawable.ic_doctor__1_,
                "https://m.me/amr.elshafay.7" , "01141025780"));
        SetIfAvailable(Doctors);
    }

    private void SetIfAvailable(List<DoctorItem> doctors) {
        for (int i = 0 ; i < doctors.size() ; i++) {
            DoctorItem doctorItem = doctors.get(i);
            Calendar CurrentTime = Calendar.getInstance();
            Calendar Lower = Calendar.getInstance();
            Calendar Upper = Calendar.getInstance();
            Lower.set(Calendar.SECOND , 0);
            Lower.set(Calendar.MINUTE , 0);
            Lower.set(Calendar.HOUR_OF_DAY, doctorItem.getFrom());
            Upper.set(Calendar.HOUR_OF_DAY , doctorItem.getFrom());
            Upper.set(Calendar.SECOND , 0);
            Upper.set(Calendar.MINUTE , 0);
            Upper.add(Calendar.HOUR_OF_DAY , doctorItem.getWork_hours());
            if (CurrentTime.after(Lower) && CurrentTime.before(Upper)) {
                Log.e("ab_do" , "In Range " + CurrentTime.get(Calendar.HOUR_OF_DAY));
                doctorItem.setAvailable(true);
            }
            else  {
                Log.e("ab_do" , "Not In Range " + CurrentTime.get(Calendar.HOUR_OF_DAY));
                doctorItem.setAvailable(false);
            }
        }

        doctorAdapter = new DoctorAdapter(this , Doctors , this);
        recyclerView.setAdapter(doctorAdapter);

    }

    @Override
    public void onClick(int id_btn, int pos) {
        if (!Doctors.get(pos).isAvailable()) {
            Snackbar snackbar = Snackbar.make(recyclerView , "عفوا الطبيب غير متاح الأن لا يمكنك التواصل معه" , Snackbar.LENGTH_SHORT);
                    snackbar.setAction("حسنا", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
            snackbar.show();
            return;
        }
        DoctorItem doctorItem = Doctors.get(pos);
        switch (id_btn) {
            case 0 :
                // whats_app
                boolean isInstalled = whatsAvailable();
                if(isInstalled)
                {
                    Intent whats_app = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+"+20"+doctorItem.getTel()+"&text="+"مرحبا"));
                    startActivity(whats_app);
                }
                else
                {
                    Toast.makeText(this,"هناك خطأ ما يرجي إعادة المحاولة",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1 :
                // messanger
                GoToMessangerPage(doctorItem.getMessanger());
                break;
            case 2 :
                // call
                CallButton(doctorItem.getTel());
                break;
        }
    }

    private void GoToMessangerPage(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void CallButton(String tel){
        Tel = tel ;
        if(Tel.trim().length() > 0)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},100);
            }
            else
            {
                String dial = "tel:" + Tel;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                CallButton(Tel);
            }
            else
            {
                Toast.makeText(this,"يرجي إعطاء الإذن أولا",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean whatsAvailable()
    {
        PackageManager packageManager = getPackageManager();
        boolean isInstalled;
        try{
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        }catch (PackageManager.NameNotFoundException e)
        {
            isInstalled = false;
        }
        return isInstalled;
    }
}