package com.example.coronaviruse.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coronaviruse.DoctorActivity;
import com.example.coronaviruse.FindNearbyHospitals.FindHospitals;
import com.example.coronaviruse.Medicines_Activity;
import com.example.coronaviruse.R;
import com.example.coronaviruse.databinding.ActivityTakeOrUploadBinding;
import com.example.coronaviruse.ml.Model;
import com.google.android.material.navigation.NavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class takeOrUpload extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView user_name ;
    Button take , upload , predict;
    ImageView skin_img ;
    Bitmap bitmap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_or_upload);
        ImageView Nav_btn = findViewById(R.id.nav_btn) ;
        drawerLayout = findViewById(R.id.drawer_layout);
        take = findViewById(R.id.take);
        upload = findViewById(R.id.upload);
        predict = findViewById(R.id.predict);
        skin_img = findViewById(R.id.skin_img);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 100);
        }
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeOrUpload.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 200);

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, true);
                Model model = null;
                try {
                    model = Model.newInstance(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 250, 250, 3}, DataType.FLOAT32);
                TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                tensorImage.load(bitmap);
                ByteBuffer buffer = convertBitmapToByteBuffer(tensorImage.getBitmap());
                //ByteBuffer byteBuffer = tensorImage.getBuffer();
                inputFeature0.loadBuffer(buffer);
                // Runs model inference and gets result.
                Model.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                float[] output_arr = outputFeature0.getFloatArray();
                int type = getMaxIndex(output_arr);
                Intent intent = new Intent(takeOrUpload.this , LoadingResult.class) ;
                intent.putExtra("Class" , type);
                startActivity(intent);
                finish();
            }
        });
        Nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ab_do" , "Click") ;
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        PrepareNavigationDrawer();
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bp) {
        ByteBuffer imgData = ByteBuffer.allocateDirect(Float.BYTES*250*250*3);
        imgData.order(ByteOrder.nativeOrder());
        Bitmap bitmap = Bitmap.createScaledBitmap(bp,250,250,true);
        int [] intValues = new int[250*250];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Convert the image to floating point.
        int pixel = 0;

        for (int i = 0; i < 250; ++i) {
            for (int j = 0; j < 250; ++j) {
                final int val = intValues[pixel++];

                imgData.putFloat(((val>> 16) & 0xFF) / 255.f);
                imgData.putFloat(((val>> 8) & 0xFF) / 255.f);
                imgData.putFloat((val & 0xFF) / 255.f);
            }
        }
        return imgData;
    }

    private int getMaxIndex(float[] output_arr) {
        int index = -1 ;
        float max = Integer.MIN_VALUE ;
        for (int i = 0 ; i < output_arr.length ; i ++) {
            float val = output_arr[i];
            if (val > max) {
                max = val ;
                index = i ;
            }
        }
        return  index ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void PrepareNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout) ;
        NavigationView navigationView = findViewById(R.id.navdrawer);
        navigationView.setItemIconTintList(null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_name = navigationView.getHeaderView(0).findViewById(R.id.navtext);
        user_name.setText(sharedPreferences.getString("name" , "No Name"));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.sign_out) {
                    //mAuth.signOut();
                    startActivity(new Intent(getBaseContext(), Login.class));
                    overridePendingTransition(0 , R.anim.fade_out);
                    finish();
                    return true;
                }
                if (itemId == R.id.hospitals) {
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("hos"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
                if (itemId == R.id.pharmacy) {
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("pharmacy"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
                if (itemId == R.id.Thedoctors) {
                    Log.i("abdo" , "ClickedD");
                    startActivity(new Intent(getBaseContext() , FindHospitals.class).setAction("doctors"));
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }
//                if (itemId == R.id.Protocol) {
//                    Log.e("ab_do" , "med " + Integer.parseInt(medical));
//                    Intent intent = new Intent(getBaseContext() , Medicines_Activity.class);
//                    intent.putExtra("Protocol" , Integer.parseInt(medical));
//                    intent.putExtra("F" , false);
//                    if (Integer.parseInt(Day)>=14) {
//                        Log.e("ab_do" , "AfterRecover");
//                        intent.putExtra("AfterRecover" , true);
//                    }
//                    startActivity(intent);
//                    overridePendingTransition(0 , R.anim.fade_out);
//                    return true;
//                }

//                if (itemId == R.id.world) {
//                    Intent intent = new Intent(getBaseContext() , MapsActivity.class) ;
//                    intent.putExtra("Corona" , true);
//                    startActivity(intent);
//                    overridePendingTransition(0 , R.anim.fade_out);
//                    return true;
//                }

                if (itemId == R.id.Doctor) {
                    Intent intent = new Intent(getBaseContext() , DoctorActivity.class) ;
                    startActivity(intent);
                    overridePendingTransition(0 , R.anim.fade_out);
                    return true;
                }

                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {
            skin_img.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmap = androidGrayScale(bitmap);
        }
        else if (requestCode == 200) {
            bitmap = (Bitmap) data.getExtras().get("data");
            //bitmap = androidGrayScale(bitmap);
            //img.setImageBitmap((Bitmap) data.getExtras().get("data"));
            skin_img.setImageBitmap(bitmap);
        }
        take.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        predict.setVisibility(View.VISIBLE);
    }
}