package com.example.coronaviruse.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coronaviruse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button Login ;
    TextView Register ;
    EditText Email , password ;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Login = findViewById(R.id.Login);
        Register = findViewById(R.id.Register);
        Email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , com.example.coronaviruse.Activities.Register.class));
                overridePendingTransition(0 , R.anim.fade_out);
                //finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Login(Email.getText().toString() , password.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Load(currentUser);
        }
    }

    private void Load(FirebaseUser firebaseUser) {
        Intent intent = new Intent(getBaseContext() , Main.class);
        intent.putExtra("User" , firebaseUser);
        startActivity(intent);
        overridePendingTransition(0 , R.anim.fade_out);
        //finish();
    }

    private void User_Login(String email , String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Load(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(Login , "المعلومات الذي أدخلتها غير صحيحه يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }
                });
    }
}