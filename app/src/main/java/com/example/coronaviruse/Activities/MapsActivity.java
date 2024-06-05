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
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.coronaviruse.CoronaLocations;
import com.example.coronaviruse.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    Geocoder geocoder;
    LocationCallback locationCallback;
    FusedLocationProviderClient locationProviderClient;
    Marker MyLocationMarker;
    int AccessFineLocationPermission = 102;
    String FINE_LOCATION_PERMISSION_First_Time = "FineLocationPermission";
    private WifiManager wifiManager;
    LocationRequest locationRequest;
    int ResolvableApiExceptionConstant = 104;
    Marker SearchMarker;
    ImageView imageView ;
    boolean Corona = false ;
    ProgressDialog progressDialog ;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference ;
    ChildEventListener childEventListener ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Corona = getIntent().getBooleanExtra("Corona" , false);
        if (!Corona) {
            setSupportActionBar(findViewById(R.id.Toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        else {
            findViewById(R.id.Toolbar).setVisibility(View.GONE);
        }

        inti();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        inti_Places();
        CheckForWifi();
        CreateLocationRequest();
        SetUpSettingsRequest();

    }
    private void CreateLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setMaxWaitTime(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void SetUpSettingsRequest() {
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();
        List<LocationRequest> locationRequests = new ArrayList<>();
        locationRequests.add(locationRequest);
        locationRequests.add(new LocationRequest().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
        locationSettingsRequestBuilder.addAllLocationRequests(locationRequests);
        locationSettingsRequestBuilder.setAlwaysShow(true);
        locationSettingsRequestBuilder.setNeedBle(true) ;
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> settingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
        settingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                LocationSettingsResponse response = null;
                try {
                    response = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    LocationSettingsStates locationSettingsStates = response.getLocationSettingsStates();
                    Log.e("ab_do", "isNetworkLocationPresent " + locationSettingsStates.isNetworkLocationPresent());
                    Log.e("ab_do", "isNetworkLocationUsable " + locationSettingsStates.isNetworkLocationUsable());
                    Log.e("ab_do", "isGpsPresent " + locationSettingsStates.isGpsPresent());
                    Log.e("ab_do", "isGpsUsable " + locationSettingsStates.isGpsUsable());
                    Log.e("ab_do", "isLocationPresent " + locationSettingsStates.isLocationPresent());
                    Log.e("ab_do", "isLocationUsable " + locationSettingsStates.isLocationUsable());
                    Log.e("ab_do", "isBlePresent " + locationSettingsStates.isBlePresent());
                    Log.e("ab_do", "isBleUsable " + locationSettingsStates.isBleUsable());
                }
            }
        });
        settingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("ab_do", "onSuccess");
                // start request update ..
                StartRequestUpdate();
            }
        });
        settingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (((ApiException) e).getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    Snackbar.make(findViewById(R.id.root_map) , "There is an error please check your location Settings", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (e instanceof ResolvableApiException) {
                    Log.e("ab_do", "OnFailureListener " + ((ResolvableApiException) e).getStatusCode());
                    try { // show the user dialog to enable location :
                        ((ResolvableApiException) e).startResolutionForResult(MapsActivity.this, ResolvableApiExceptionConstant);
                    } catch (IntentSender.SendIntentException ex) {
                        Log.e("ab_do", ex.toString());
                        Snackbar.make(findViewById(R.id.root_map) , "There is an error please check your location Settings", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void StartRequestUpdate() {
        if (ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    private void StopRequestUpdate() {
        if (locationProviderClient!=null)
            locationProviderClient.removeLocationUpdates(locationCallback) ;
    }
    private void CheckForWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            RequestToOpenTheWifi();
        }
    }
    private void RequestToOpenTheWifi() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("For best Accuracy Please turn on your wifi") ;
        alertDialog.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT < 29) {
                    boolean x = wifiManager.setWifiEnabled(true);
                    Log.e("ab_do" , "The system turn on wifi " + x) ;
                }
                else {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    startActivityForResult(panelIntent , 100);
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("ff" ,"onMapReady" );
        mMap = googleMap;
        mMap.setPadding(0 , 16 , 0 , 0);
        firebaseDatabase = FirebaseDatabase.getInstance() ;
        databaseReference = firebaseDatabase.getReference().child("CoronaLocations");
        //mMap.setOnMapLongClickListener(this);
        //mMap.setOnMarkerDragListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (!Corona)
        mMap.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            AccessAfterGranted();
        }
        else
            RequestFineLocationPermission();
        }

    private void AccessAfterGranted() {
        enableUserLocation();
        ZoomToUserLocation();
        if (Corona) {
            MarkCoronaLocations();
        }
    }

    private void MarkCoronaLocations() {
        ShowDialog(this);
        Log.e("zb_do" , "CoronaLocations") ;
        progressDialog.dismiss();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("zb_do" , "onChildAdded") ;
                CoronaLocations coronaLocations = snapshot.getValue(CoronaLocations.class);
                MarkCovidLocation(coronaLocations);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CoronaLocations coronaLocations = snapshot.getValue(CoronaLocations.class);
                MarkCovidLocation(coronaLocations);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStart() {
        if (childEventListener!=null)
        databaseReference.addChildEventListener(childEventListener);
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (childEventListener!=null)
        databaseReference.removeEventListener(childEventListener);
        super.onPause();
    }

    private void MarkCovidLocation(CoronaLocations coronaLocations) {
        MarkerOptions markerOptions = new MarkerOptions() ;
        LatLng latLng = new LatLng(coronaLocations.getLat() , coronaLocations.getLong());
        markerOptions.position(latLng);
        markerOptions.title(getAddress(latLng));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.coronamap)) ;
        mMap.addMarker(markerOptions) ;
    }

    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    private void ZoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = locationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) return;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getAddress(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                MyLocationMarker = mMap.addMarker(markerOptions);
            }
        }) ;
    }
    private String getAddress(LatLng latLng) {
        Address address = null;
        String title = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() != 0) {
                address = addresses.get(0);
            }
            if (address != null)
                title = address.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }
    private void inti() {
        geocoder = new Geocoder(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("ab_do" , "locationResult == null" ) ;
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location!=null) {
                    SetUserLocation(location);
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (!locationAvailability.isLocationAvailable()) {
                    Log.e("ab_do" , "onNotLocationAvailability") ;
                }
            }
        };
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
                .setTitle("The Permission is needed First")
                .setMessage("Enable the Location Permission First")
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS , Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
    private void SetUserLocation(Location location)  {
        //Log.e("ab_do" , "SetUserLocation\n" + location.toString()) ;
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude()) ;
        String title = getAddress(latLng);
        if (MyLocationMarker == null) {
            // zoom to user location did`nt be called
            MarkerOptions markerOptions = new MarkerOptions() ;
            markerOptions.position(latLng);
            markerOptions.title(title);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            MyLocationMarker = mMap.addMarker(markerOptions) ;
        }
        else {
            //MyLocationMarker.setPosition(latLng);
            //MyLocationMarker.setTitle(title);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ab_do" , "onActivityResult") ;
        if (requestCode == ResolvableApiExceptionConstant) {
            if (resultCode==RESULT_OK) {
                Log.e("ab_do" , "i accept");
                StartRequestUpdate();
            }
            else {
                SetUpSettingsRequest();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==AccessFineLocationPermission) {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                AccessAfterGranted();
            else {
                Log.e("corona" , "Refused");
                new AlertDialog.Builder(this).setMessage("We need the Permission determine your Location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestFineLocationPermission();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        }
    }

    private void inti_Places() {
        Places.initialize(this , "AIzaSyCmzuWYk56uoPYP5r578YFvNVJdGhYClbE");
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteSupportFragment==null) return;
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        List<Place.Field> fields = new ArrayList<>() ;
        fields.add(Place.Field.LAT_LNG);
        autocompleteSupportFragment.setPlaceFields(fields) ;
        autocompleteSupportFragment.setHint("Search");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.e("ab_do" , "onPlaceSelected") ;
                MoveTheMapToSpecificPlace(place.getLatLng());
            }

            @Override
            public void onError(@NonNull Status status) {
                if (!status.getStatus().equals(Status.RESULT_CANCELED))
                    Snackbar.make(findViewById(R.id.root_map) ,status.getStatusMessage()!=null ? status.getStatusMessage() : "There is an error" , Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void MoveTheMapToSpecificPlace(LatLng latLng) {
        if (SearchMarker==null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.search)) ;
            SearchMarker = mMap.addMarker(markerOptions) ;
        }
        else
            SearchMarker.setPosition(latLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 17));
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.e("ab_do" , "OnMap ready");
        if (ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        MyLocationMarker.setPosition(latLng);
        MyLocationMarker.setTitle(getAddress(latLng));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Corona) {
            getMenuInflater().inflate(R.menu.map_menu, menu);
            return true;
        }
        return false ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (Corona) return false ;
        if (item.getItemId() == R.id.Done_Map) {
              markLocation();
              return true ;
        }
        return super.onOptionsItemSelected(item);
    }



    private void markLocation() {
        if (MyLocationMarker==null) return ;
        LatLng latLng = MyLocationMarker.getPosition();
        //Toast.makeText(this, "latitude " + latLng.latitude + " " + "longitude " + latLng.longitude , Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("latitude" , latLng.latitude);
        intent.putExtra("longitude" , latLng.longitude);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString("latitude" , String.valueOf(latLng.latitude)).apply();
        sharedPreferences.edit().putString("longitude" , String.valueOf(latLng.longitude)).apply();
        setResult(RESULT_OK , intent);
        finish();
    }

}

