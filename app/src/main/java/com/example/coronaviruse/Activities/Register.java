package com.example.coronaviruse.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coronaviruse.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    Button CreateAccount ;
    TextView Login ;
    EditText Username , Password , Phone , Email ;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Init();
        SetListenersToButtons();
    }

    private void SetListenersToButtons() {
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAcc();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , Login.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }

    private void Init() {
        CreateAccount = findViewById(R.id.CreateAccount);
        Login = findViewById(R.id.Login);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        Phone = findViewById(R.id.PhoneNum);
        Email = findViewById(R.id.email) ;
    }

    private void CreateAcc() {
        String phone = Phone.getText().toString();
        String email = Email.getText().toString();
        if (TextUtils.isEmpty(Username.getText()) || TextUtils.isEmpty(Password.getText())) {
            Snackbar.make(CreateAccount , "يرجي ملأ كل الحقول أولا" , Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.PHONE.matcher(phone).matches() || TextUtils.isEmpty(phone) || phone.length() != 11 || !validNum(phone)) {
            Snackbar.make(CreateAccount , "يرجي إدخال رقم هاتف صحيح" , Snackbar.LENGTH_SHORT).show();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || TextUtils.isEmpty(email) || !validEmail(email)) {
            Snackbar.make(CreateAccount , "يرجي إدخال بريد إالكتروني صحيح" , Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (Password.getText().length()<6) {
            Snackbar.make(CreateAccount , "يجب أن تكون كلمة المرور أطول من 6 حروف" , Snackbar.LENGTH_SHORT).show();
            return;
        }
        SaveAuth(email , Password.getText().toString());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString("name" , Username.getText().toString()).apply();
    }

    private boolean validEmail(String email) {
        return (email.contains(".com") || email.contains(".Com") && (email.contains("yahoo") || (email.contains("hotmail") || ((email.contains("gmail")) || email.contains("Gmail"))))) ;
    }

    private void SaveAuth(String email , String password) {
        ShowDialog(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(getBaseContext() , GetLocation.class);
                intent.putExtra("Phone" , Phone.getText().toString().trim());
                intent.putExtra("Email" , Email.getText().toString().trim());
                intent.putExtra("Username" , Username.getText().toString().trim());
                intent.putExtra("Password" , Password.getText().toString().trim());
                progressDialog.dismiss();
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG","On Failure" + e.getMessage());
//                Snackbar.make(Login , "هناك خطأ ما يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext() , GetLocation.class);
                intent.putExtra("Phone" , Phone.getText().toString().trim());
                intent.putExtra("Email" , Email.getText().toString().trim());
                intent.putExtra("Username" , Username.getText().toString().trim());
                intent.putExtra("Password" , Password.getText().toString().trim());
                progressDialog.dismiss();
                startActivity(intent);
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        });
    }


    private boolean validNum (String phone) {
        return (phone.startsWith("010") || phone.startsWith("011") || phone.startsWith("012") || phone.startsWith("015") ) ;
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