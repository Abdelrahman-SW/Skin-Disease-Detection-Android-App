package com.example.coronaviruse.FindNearbyHospitals;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.coronaviruse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class FindHospitals extends AppCompatActivity implements AdaptersRecycle.ClickListener {
    private static final String PROXIMITY_RADIUS = "40000" ;
    static ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String userID;
    FirebaseFirestore fStore ;
    AdaptersRecycle adaptersRecycle ;
    RecyclerView recyclerView ;
    double Latitude ;
    double Longitude ;
    TextView header_txt ;
    String Place , keyword ;
    int place_img ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hospitals);
//        setSupportActionBar(findViewById(R.id.Toolbar));
//        //setting up the title to actionbar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        header_txt = findViewById(R.id.hos);
        keyword = " " ;
        if (getIntent().getAction().equals("hos")) {
            header_txt.setText("المستشفيات القريبة");
            Place = "hospital" ;
            place_img = R.drawable.ic_ambulance__1_ ;
        }
        else if (getIntent().getAction().equals("pharmacy")) {
            header_txt.setText("الصيدليات القريبة");
            Place = "pharmacy" ;
            place_img = R.drawable.ic_pharmacist ;
        }
        else {
            header_txt.setText("دكاترة الجلدية القريبين");
            Place = "doctor" ;
            place_img = R.drawable.ic_doctor ;
            keyword = "جلدية" ;
        }
        recyclerView = findViewById(R.id.MyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptersRecycle = new AdaptersRecycle(this);
        recyclerView.setAdapter(adaptersRecycle);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Latitude = Double.parseDouble(sharedPreferences.getString("latitude" , ""));
        Longitude = Double.parseDouble(sharedPreferences.getString("longitude" , ""));
        String url = getUrl(Latitude, Longitude);
        ShowDialog(FindHospitals.this);
        asyncTask asyncTask = new asyncTask(adaptersRecycle, progressDialog, place_img);
        asyncTask.execute(url);
    }




    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }




    private String getUrl(double latitude , double longitude)
    {
        StringBuilder PlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        PlaceUrl.append("location=").append(latitude).append(",").append(longitude);
        PlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        PlaceUrl.append("&type=").append(Place);
        if (!keyword.equals(" "))
        PlaceUrl.append("&keyword=").append(keyword);
        PlaceUrl.append("&sensor=true");
        PlaceUrl.append("&key=").append("AIzaSyCmzuWYk56uoPYP5r578YFvNVJdGhYClbE");
        Log.d("ab_do", "url = "+PlaceUrl.toString());

        return PlaceUrl.toString();
    }

    @Override
    public void onClickOnHospital(Hospital hospital) {
        Log.d("ab_do", "placeName  " + hospital.getName());
        Log.d("ab_do", "vicinity " + hospital.getVicinity());
        Log.d("ab_do", "Rating " + hospital.getRating());
        Log.d("ab_do", "Open  " + hospital.isIs_open());
        Log.d("ab_do", "Long  " + hospital.getLatitude());
        Log.d("ab_do", "Latiude  " + hospital.getLongitude());
        String src = Latitude+","+Longitude;
        String dest = hospital.getLatitude()+","+hospital.getLongitude();
        DisplayTrack(src , dest);

    }

    public void DisplayTrack(String sSource, String sDestination) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination));
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }
    }
}