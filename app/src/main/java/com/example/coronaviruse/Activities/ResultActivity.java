package com.example.coronaviruse.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;

import com.example.coronaviruse.IntroToProtocolActivity;
import com.example.coronaviruse.Medical_follow_up;
import com.example.coronaviruse.MyBroadCast;
import com.example.coronaviruse.R;
import com.example.coronaviruse.VitmainsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ResultActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE =101 ;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    TextView Res , pdf , header;
    ImageView Res_img ;
    Button Start , No , Ok ;
    LinearLayout No_Covid , Covid ;
    DocumentReference documentReference ;
    Activity activity ;
    ProgressDialog progressDialog ;
    int pageHeight = 1324;
    int pagewidth = 720;
    File file ;
    int Type , age ;
    String Class = "";
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    ImageView Result_img ;
    String [] classes = {"Melanoma" , "Vascular Lesions" , "Basal cell carcinoma" , "pigmented  keratosis lesions" , "benign keratosis lesions" , "Eczema"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        WorkManager.getInstance(ResultActivity.this).cancelAllWorkByTag("Repeat");
        activity = this ;
        Result_img = findViewById(R.id.Result_img);
        Res = findViewById(R.id.Result);
        header = findViewById(R.id.header);
        Start = findViewById(R.id.start) ;
        No = findViewById(R.id.No) ;
        pdf = findViewById(R.id.pdf);
        int type = getIntent().getIntExtra("Class" , 0);
        if (type == 6) {
           header.setText("أنت بخير !");
           Res.setText("يبدو أنك لا تعاني من الإصابة بأي مرض جلدي");
           Result_img.setImageResource(R.drawable.rating);
           Start.setVisibility(View.GONE);
           pdf.setVisibility(View.GONE);
        }
        else {
            Class = classes[getIntent().getIntExtra("Class" , 0)];
            Res.setText(Class);
        }
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.co);
//        scaledbmp = Bitmap.createScaledBitmap(bmp, 130, 130, false);
//        Res = findViewById(R.id.Result) ;
//        pdf = findViewById(R.id.pdf);
//        Res_img = findViewById(R.id.Result_img);
        //Ok = findViewById(R.id.Ok) ;
//        No_Covid = findViewById(R.id.No_Covid) ;
//        Covid = findViewById(R.id.Covid) ;
//        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("HasReminder" , false).apply();
//        GetRes();
//        Ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 2/20/2021 the user has no an active Medical follow-up save that in the database
//                NoMedicalFollowUp();
//            }
//        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , takeOrUpload.class));
                finish();
            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MoreAboutDisease.class);
                intent.putExtra("Class" , getIntent().getIntExtra("Class" , 0));
                startActivity(intent);
            }
        });
//        pdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkPermission()) {
//                    GeneratePdf();
//                } else {
//                    requestPermission();
//                }
//            }
//        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    GeneratePdf();
                } else {
                    Toast.makeText(this, "يرجي إعطاء الإذن قبل تحميل تقرير الكشف الخاص بك", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void GeneratePdf() {
        ShowDialog(this);
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 40, 1100, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        //title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(25f);

        // below line is sued for setting color
        // of our text inside our PDF file.
        //title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextAlign(Paint.Align.RIGHT);
        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        SimpleDateFormat format = new SimpleDateFormat("h:mm  a " , Locale.forLanguageTag("ar"));
        String date = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL , Locale.forLanguageTag("ar")).format(Calendar.getInstance().getTime());
        String Time = format.format(Calendar.getInstance().getTime()) ;
        canvas.drawLine(680 , 160 , 40 , 160 , paint);
        canvas.drawText("الوقت : ", 680, 190, title);
        canvas.drawLine(680 , 200 , 40 , 200 , paint);
        canvas.drawText(Time, 600, 190, title);
        canvas.drawLine(680 , 240 , 40 , 240 , paint);
        canvas.drawText("التاريخ : ", 680, 270, title);
        canvas.drawText(date, 580, 270, title);
        canvas.drawLine(680 , 280 , 40 , 280 , paint);
        canvas.drawLine(680 , 320 , 40 , 320 , paint);
        canvas.drawText("اسم المريض : ", 680, 350, title);
        canvas.drawText(getIntent().getStringExtra("username"), 530, 350, title);
        canvas.drawLine(680 , 360 , 40 , 360 , paint);

        canvas.drawLine(680 , 400 , 40 , 400 , paint);
        canvas.drawText("العمر : ", 680, 430, title);
        canvas.drawText(String.valueOf(getIntent().getIntExtra("Age" , 1)), 600, 430, title);
        canvas.drawLine(680 , 440 , 40 , 440 , paint);

        canvas.drawLine(680 , 480 , 40 , 480 , paint);
        canvas.drawText("النوع : ", 680, 510, title);
        String Type = getIntent().getIntExtra("Type" , 1) == 1 ? "ذكر" : "أنثي" ;
        canvas.drawText(Type, 600, 510, title);
        canvas.drawLine(680 , 520 , 40 , 520 , paint);


        canvas.drawLine(680 , 570, 40 , 570 , paint);
        canvas.drawText("نسبة الإشتباه : ", 680, 600, title);
        canvas.drawText(getIntent().getStringExtra("Result"), 520, 600, title);
        canvas.drawLine(680 , 610 , 40 , 610 , paint);
        canvas.drawText("الأعراض التي تعاني منها : ", 680, 680, title);
        ArrayList<String> strings = getIntent().getStringArrayListExtra("A");
        int c = 0 ;
        for (int i = 0 ; i < strings.size() ; i++) {
            canvas.drawText("- " + strings.get(i), 680, 730+c, title);
            c+=50 ;
        }

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.cardBackground));
        title.setTextSize(25f);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("تقرير الكشف الخاص بك", 360, 80, title);
        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() ,  "تقرير الكشف الخاص بك.pdf");
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));
            // below line is to print toast message
            // on completion of PDF generation.
            progressDialog.dismiss();
            ShowPdfNotification();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
            Log.e("mena" , e.getMessage());
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();

    }

    private void ShowPdfNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.pdfnotifi);
        NotificationCompat.Builder buildNotification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Covid", "Covid App", NotificationManager.IMPORTANCE_HIGH);// sound of the notification
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern((new long[]{1000, 500, 1000, 500, 1500, 500, 1500}));
            notificationChannel.setShowBadge(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setBypassDnd(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setDescription("Send A notification for your Medical follow up");
            Log.e("ab_do" , "Should show lights " + notificationChannel.shouldShowLights());
            notificationManager.createNotificationChannel(notificationChannel);
            buildNotification = new NotificationCompat.Builder(getApplicationContext(), "Covid");
        }
        else {
            buildNotification = new NotificationCompat.Builder(this , "Covid") ;
            buildNotification.setPriority(NotificationCompat.PRIORITY_MAX);
            buildNotification.setSound(Uri.parse(String.valueOf(Settings.System.DEFAULT_NOTIFICATION_URI)));
            buildNotification.setVibrate((new long[]{1000, 500, 1000, 500, 1500, 500, 1500}));
            buildNotification.setLights(Color.RED, 2000, 500);
        }
        buildNotification.setSmallIcon(R.drawable.res_covid);
        buildNotification.setLargeIcon(largeIcon);
        buildNotification.setContentText("تم تحميل تقرير الكشف الخاص بك بنجاح");
        buildNotification.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("تم تحميل تقرير الكشف الخاص بك بنجاح")
        );
        buildNotification.setCategory(NotificationCompat.CATEGORY_ALARM);
        buildNotification.setAutoCancel(true);
        buildNotification.setContentTitle("عمل جيد") ;
        buildNotification.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
        buildNotification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        buildNotification.setContentIntent(PendingIntent.getActivity(this , 101 , intent , PendingIntent.FLAG_CANCEL_CURRENT)) ;
        Notification notification = buildNotification.build();
        notificationManager.notify(500 , notification);
    }

    public static void PrepareWork(Long Time , Context context) {
        Log.e("medical", "PrepareWork");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext() , MyBroadCast.class) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (alarmManager != null)
                // setAlarmClock ( AlarmClockInfo (TriggerTime , ShowIntent) , PendingIntent )
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo( Time , null), GetPendingIntentOfAlarm(context.getApplicationContext() , intent , 100));
        }
        else {
            // before api level 21 use setExact with RTC_WAKEUP :
            alarmManager.setExact(AlarmManager.RTC_WAKEUP , Time  , GetPendingIntentOfAlarm(context.getApplicationContext() , intent , 100));
        }
    }

    public static void CancelAlarm (Context context , int RequestCode) {
        Intent intent = new Intent(context, MyBroadCast.class) ;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(GetPendingIntentOfAlarm( context , intent  , RequestCode));
        Log.e("ab_do" , "request code on cancel alarm : " + RequestCode);
    }


    private static PendingIntent GetPendingIntentOfAlarm(Context context , Intent intent , int RequestCode) {
        return PendingIntent.getBroadcast(context.getApplicationContext(),
                RequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void MedicalFollowUp() {
        //900000*96 ;
        long period = 90000 ;
        long Time = System.currentTimeMillis() + (period);
        Log.e("ab_do" , "Time " + Time);
        Map<String, Object> user = new HashMap<>();
        user.put("YesOrNot", "1");
        user.put("Time" , Time);
        user.put("Active" , true);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CancelAlarm(activity , 100);
                PrepareWork(Time , activity);
                startActivity(new Intent(getBaseContext() , IntroToProtocolActivity.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(Ok ,"هناك خطأ ما يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void NoMedicalFollowUp() {
        Map<String, Object> user = new HashMap<>();
        user.put("YesOrNot", "0");
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getBaseContext() , VitmainsActivity.class));
                overridePendingTransition(0 , R.anim.fade_out);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(Ok ,"هناك خطأ ما يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void GetRes() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Res.setText(value.getString("Result"));
                Log.e("ab_do" , "Res " + Res.getText().toString());
                if (Res.getText().toString().equals("مرتفعة جدا") ) {
                    Log.e("ab_do" , "Higgh") ;
                    Res_img.setImageDrawable(ContextCompat.getDrawable(getBaseContext() , R.drawable.covid_high));
                    Covid.setVisibility(View.VISIBLE);
                    No_Covid.setVisibility(View.GONE);
                }
                else if (Res.getText().toString().equals("متوسطة"))  {
                    Res_img.setImageDrawable(ContextCompat.getDrawable(getBaseContext() , R.drawable.covid_high));
                    Covid.setVisibility(View.VISIBLE);
                    No_Covid.setVisibility(View.GONE);
                }
                else {
                    Log.e("ab_do" , "Low") ;
                    Res_img.setImageDrawable(ContextCompat.getDrawable(getBaseContext() , R.drawable.covid_normal));
                    Covid.setVisibility(View.GONE);
                    No_Covid.setVisibility(View.VISIBLE);
                }
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




}