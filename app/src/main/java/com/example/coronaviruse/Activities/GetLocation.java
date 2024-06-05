package com.example.coronaviruse.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronaviruse.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GetLocation extends AppCompatActivity {
    Button MarkLocation;
    Button MarkMyLocation;
    TextView Ask;
    FusedLocationProviderClient locationProviderClient;
    int AccessFineLocationPermission = 102;
    String FINE_LOCATION_PERMISSION_First_Time = "FineLocationPermission";
    String Phone , username ;
    FirebaseFirestore fStore;
    String userID;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        MarkLocation = findViewById(R.id.MarkLocation);
        fStore = FirebaseFirestore.getInstance();
        MarkMyLocation = findViewById(R.id.MarkMyLocation) ;
        Ask = findViewById(R.id.Ask);
        Phone = getIntent().getStringExtra("Phone") ;
        username = getIntent().getStringExtra("Username") ;
        MarkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkLocation();
            }
        });
        MarkMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkMyLocation();
            }
        });
        Ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ask();
            }
        });
    }

    private void MarkMyLocation() {
        ShowDialog(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            RequestFineLocationPermission();
        }
        else
        locationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task==null) {
                    Toast.makeText(GetLocation.this, "هناك خطأ ما يرجي التحقق من إعدادات الموقع الخاصص بك وإعادة المحاولة", Toast.LENGTH_SHORT).show();
                    return;
                }
                Location location = task.getResult();
                if (location==null) {
                    Toast.makeText(GetLocation.this, "هناك خطأ ما يرجي التحقق من إعدادات الموقع الخاصص بك وإعادة المحاولة", Toast.LENGTH_SHORT).show();
                    return;
                }

                double Longitude = location.getLongitude();
                double Latitude = location.getLatitude();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString("latitude" , String.valueOf(Latitude)).apply();
                sharedPreferences.edit().putString("longitude" , String.valueOf(Longitude)).apply();
                SaveToDatabase(Longitude , Latitude , Phone , username);
            }
        });
    }

    private void SaveToDatabase(double Longitude , double Latitude  , String Phone , String username) {
        //--------------------------------------------------------------------------------------------------------//
        Intent intent = new Intent(getApplicationContext(), Male_femaleActivity.class);
        intent.putExtra("Longitude" , 0);
        intent.putExtra("Latitude" , 0);
        startActivity(intent);
        overridePendingTransition(0 , R.anim.fade_out);
        finish();
//                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DocumentReference documentReference = fStore.collection("users").document(userID);
//                Map<String,Object> user = new HashMap<>();
//                user.put("UserName",username);
//                user.put("Longitude",Longitude);
//                user.put("Latitude",Latitude);
//                user.put("Phone",Phone);
//                //user.put("Government",government);
//                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressDialog.dismiss();
//                        progressDialog.cancel();
//                        Intent intent = new Intent(getApplicationContext(), IntroToQuestionsActivity.class);
//                        intent.putExtra("Longitude" , Longitude);
//                        intent.putExtra("Latitude" , Latitude);
//                        startActivity(intent);
//                        overridePendingTransition(0 , R.anim.fade_out);
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //Toast.makeText(GetLocation.this, "هناك خطأ ما يرجي إعادة المحاولة", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), Male_femaleActivity.class);
//                        intent.putExtra("Longitude" , 0);
//                        intent.putExtra("Latitude" , 0);
//                        startActivity(intent);
//                        overridePendingTransition(0 , R.anim.fade_out);
//                        finish();
//                    }
//                });
//







        //--------------------------------------------------------------------------------------------------------//
        // After Save To The database Open Next Page
    }

    private void MarkLocation() {
        startActivityForResult(new Intent(getBaseContext() , MapsActivity.class) , 100);
    }

    private void Ask() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setView(R.layout.why_loc_dialog);
        dialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==AccessFineLocationPermission) {
            if (grantResults[0]!=PackageManager.PERMISSION_GRANTED) {

                Log.e("corona", "Refused");
                new AlertDialog.Builder(this).setMessage("يرجي إعطائنا صلاحيات الحصول علي موقعك قبل الإستكمال")
                        .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestFineLocationPermission();
                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else
                MarkMyLocation();
            }
        }
    private void RequestFineLocationPermission() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean FirstTime = sharedPreferences.getBoolean(FINE_LOCATION_PERMISSION_First_Time, true);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && !FirstTime) {
            // this mean the user click don`t remind me again
            ShowGoToSettingsDialog();

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AccessFineLocationPermission);
            Log.e("corona" , "requestPermissions " + FirstTime);
        }
        editor.putBoolean(FINE_LOCATION_PERMISSION_First_Time, false);
        editor.apply();
    }
    private void ShowGoToSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("إذن الموقع مطلوب أولا")
                .setMessage("يرجي إعطائنا صلاحيات الحصول عل موقعك ")
                .setPositiveButton("الذهاب إلي الإعدادات", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS , Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ab_do" , "onActivityResult") ;
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data.getDoubleExtra("longitude" , -1) == -1 || data.getDoubleExtra("latitude" , -1) == -1) {
                    Snackbar.make(Ask ,"يرجي تحديد موقعك أولا" , Snackbar.LENGTH_SHORT).show(); return;
                }
                ShowDialog(GetLocation.this);
                SaveToDatabase(data.getDoubleExtra("longitude" , -1) , data.getDoubleExtra("latitude" , -1) , Phone , username);
            }
            else {
                Snackbar.make(Ask ,"يرجي تحديد موقعك أولا" , Snackbar.LENGTH_SHORT).show();
            }
        }
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
