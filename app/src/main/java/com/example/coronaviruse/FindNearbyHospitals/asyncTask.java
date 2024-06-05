package com.example.coronaviruse.FindNearbyHospitals;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class asyncTask extends AsyncTask<Object, String, String> {


    private String googlePlacesData;
    String url;
    AdaptersRecycle adaptersRecycle ;
    ProgressDialog progressDialog ;
    int place_img ;

    public asyncTask(AdaptersRecycle  adaptersRecycle , ProgressDialog progressDialog , int place_img) {
        super();
        this.progressDialog = progressDialog ;
        this.adaptersRecycle = adaptersRecycle ;
        this.place_img = place_img ;
    }

    @Override
    protected String doInBackground(Object... objects) {
        url = (String) objects[0];
        Log.e("ab_do", "doInBackground " + url);
        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        if (nearbyPlaceList == null) return;
        List<Hospital> hospitals = new ArrayList<>();
        progressDialog.dismiss();
        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                String Rat = googlePlace.get("rating");
                String Is_open = googlePlace.get("open_now");
                String Lat  = googlePlace.get("lat");
                String Long = googlePlace.get("lng");
                if (Lat == null || Long == null || Lat.length()==0 || Long.length()==0) continue;
                Log.d("ab_do", "placeName  " + nearbyPlaceList.size() + " " +placeName);
                Log.d("ab_do", "vicinity " + vicinity);
                Log.d("ab_do", "Rating " + Rat);
                Log.d("ab_do", "Open  " + Is_open);
                Log.d("ab_do", "Long  " + Lat);
                Log.d("ab_do", "Latiude  " + Long);
                Log.d("ab_do", "----------------------------------------------------------------");
                if (Rat == null || Rat.length()==0) Rat = "0" ;
                if (Is_open == null || Is_open.length() == 0) Is_open = "true" ;
                hospitals.add(new Hospital(placeName , vicinity ,  Boolean.parseBoolean(Is_open) , Double.parseDouble(Lat) , Double.parseDouble(Long) , Double.parseDouble(Rat) , place_img));
            Log.d("ab_do", "Finish");
            adaptersRecycle.SetAdapter(hospitals);
            }


    }
}

