package com.example.coronaviruse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coronaviruse.Activities.Main;
import com.google.android.material.snackbar.Snackbar;

public class SendSmsActivity extends AppCompatActivity {
    EditText Phone ;
    Button Send ;
    Button Continue ;
    Activity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        Phone = findViewById(R.id.PhoneNum);
        Send = findViewById(R.id.Send);
        Continue = findViewById(R.id.Continue);
        activity = this ;
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(activity , Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                {
                    SendMsg();
                }
                else
                {
                    ActivityCompat.requestPermissions(activity , new String[]{Manifest.permission.SEND_SMS},1);
                }
            }
        });
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , Main.class));
                overridePendingTransition(0 , R.anim.fade_out);
            }
        });
    }

    private void SendMsg() {
        String SMS = "أنا مشتبه في الإصابة بفيروس كورونا وكنت مخالط لك أرجو منك أخذ الإجراءات اللازمة والإطمئنان علي نفسك" ;
        String phoneNo = Phone.getText().toString().trim();
        if (!Patterns.PHONE.matcher(phoneNo).matches() || TextUtils.isEmpty(phoneNo)) {
            Snackbar.make(Send , "يرجي إدخال رقم هاتف صحيح" , Snackbar.LENGTH_SHORT).show();
            return;
        }
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, SMS ,null,null);
            Toast.makeText(this, "تم إرسال الرسالة بنجاح", Toast.LENGTH_SHORT).show();
            Phone.setText("");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "فشل يرجي إعادة المحاولة", Toast.LENGTH_SHORT).show();
        }
    }
}