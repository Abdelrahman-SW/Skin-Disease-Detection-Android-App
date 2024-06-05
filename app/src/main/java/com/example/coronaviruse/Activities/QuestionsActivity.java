package com.example.coronaviruse.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.coronaviruse.AgeActivity;
import com.example.coronaviruse.CoronaLocations;
import com.example.coronaviruse.QuestionsAdapter;
import com.example.coronaviruse.MyBroadCast;
import com.example.coronaviruse.NewResultActivity;
import com.example.coronaviruse.R;
import com.example.coronaviruse.QuestionItem;
import com.example.coronaviruse.databinding.ActivityQuestionsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity{
    ActivityQuestionsBinding activityIntroBinding;
    List<QuestionItem> questionItems;
    Button no_btn ;
    Button yes_btn ;
    Button Continue ;
    int pos ;
    int Counter ;
    String Res ;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    Activity activity ;
    DocumentReference documentReference ;
    ProgressDialog progressDialog ;
    ListenerRegistration listenerRegistration ;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference ;
    Double Lat ;
    Double Long ;
    ListenerRegistration listenerRegistration2;
    int age , type ;
    String username ;
    ArrayList<String> strings ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strings = new ArrayList<>();
        if (getIntent().getBooleanExtra("new" , false)) {
            // New Report
            type = getIntent().getIntExtra("Type" , 1);
            age = getIntent().getIntExtra("Age" , 1);
            Log.e("ab_do " , "Age " + age);
            username =  getIntent().getStringExtra("username") !=null ?  getIntent().getStringExtra("username") : "غير محدد" ;
            Log.e("ab_do" , "username " + username);
        }
        Init();
        SetUpViewPager();
        Counter = 0 ;
        activity = this ;
        no_btn = findViewById(R.id.No_btn);
        yes_btn = findViewById(R.id.Yes_btn);
        Continue = findViewById(R.id.Continue);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsAllQuestionsIsAnswered()) {
                    ShowDialog(QuestionsActivity.this);
                    GetCounter();
                    // code
                    if (Counter >= 12) {
                        Res = "مرتفعة جدا";
                    } else if (Counter >= 6) {
                        Res = "متوسطة";
                    } else {
                        Res = "منخفضة";
                    }
                    documentReference = fStore.collection("users").document(userID);
                    if (getIntent().getBooleanExtra("new", false)) {
                        if (Counter>=12) {
                            // save location in the database
                            Log.e("zb_do" , "save_location");
                            GetLocationMethtod(documentReference);
                        }
                        else {
                            FirstTime();
                        }
                    }
                    else {
                        Map<String, Object> user = new HashMap<>();
                        // 900000*96 ;
                        Long Time = System.currentTimeMillis()+(30000) ;
                        Log.e("medical", "not new");
                        listenerRegistration = documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent( DocumentSnapshot documentSnapshot, FirebaseFirestoreException e){
                                String day = documentSnapshot.getString("Day");
                                int LastCounter = Integer.parseInt(day);
                                LastCounter-- ;
                                Long LC = documentSnapshot.getLong("Day"+LastCounter);
                                Log.e("cocococo", "LastCounter I " + LC);
                                Log.e("cocococo", "Curruent I " + Counter);
                                user.put("Day" + day, Counter);
                                user.put("LastDay" , day);
                                user.put("Time" , Time);
                                user.put("Active" , true);
                                listenerRegistration.remove();
                                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        if (Integer.parseInt(day) < 14)
                                        PrepareWork(Time);
                                        Log.e("medical" , "onSuccessUpdate");
                                        Intent intent = new Intent(getBaseContext(), NewResultActivity.class) ;
                                        intent.putExtra("Day" , day);
                                        intent.putExtra("LastCounter" , LC);
                                        intent.putExtra("CurrentCounter" , Counter);
                                        startActivity(intent);
                                        overridePendingTransition(0 , R.anim.fade_out);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(Continue , "يرجي إعادة المحاولة" , Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }

                }
                else {
                    Snackbar.make(Continue , "يرجي الإجابه عل جميع الأسئلة قبل المتابعه" , Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa" , "Pos " + pos) ;
                pos = activityIntroBinding.viewPager.getCurrentItem(); // current page
                questionItems.get(pos).setSelected("No");
                UpdateButtons(pos);
                pos++; // Go To next Page
                activityIntroBinding.viewPager.setCurrentItem(pos);
            }
        });
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = activityIntroBinding.viewPager.getCurrentItem(); // current page
//                if (pos<6) Counter+=2 ;
//                else Counter++ ;
                questionItems.get(pos).setSelected("Yes");
                UpdateButtons(pos);
                pos++; // Go To next Page
                activityIntroBinding.viewPager.setCurrentItem(pos);
            }
        });

    }

    private void GetLocationMethtod(DocumentReference documentReference) {
        listenerRegistration2 = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Long = value.getDouble("Longitude");
                Lat = value.getDouble("Latitude");
                Log.e("zb_do" , "" +Long + ", "  +Lat);
                listenerRegistration2.remove();
                Log.e("zb_do" , "onEvent");
                databaseReference.push().setValue(new CoronaLocations(Long, Lat)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("zb_do" , "" +Long + ", "  +Lat);
                        FirstTime();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e("zb_do" , "onFailDatabase " + e.getMessage());
                        Snackbar.make(Continue, "يرجي إعادة المحاولة  " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void FirstTime() {
        Map<String, Object> user = new HashMap<>();
        user.put("Result", Res);
        //user.put("Counter" , Counter) ;
        user.put("Day", "0");
        user.put("LastDay", "0");
        user.put("Day0", Counter);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("medical", "onSuccessUpdate");
                progressDialog.dismiss();
                Intent intent = new Intent(getBaseContext(), LoadingResult.class) ;
                intent.putExtra("Type" , type);
                intent.putExtra("Age" , age);
                intent.putExtra("Result" , Res);
                intent.putExtra("username" , username);
                intent.putStringArrayListExtra("A" , strings);
                startActivity(intent);
                overridePendingTransition(0, R.anim.fade_out);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(Continue, "يرجي إعادة المحاولة", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void GetCounter() {
        Counter = 0 ;
        for (int i = 0; i < questionItems.size() ; i++) {
            if (questionItems.get(i).getSelected().equals("Yes")) {
                if (i<=5) {
                    Counter+=2 ;
                }
                else {
                    Counter+=1 ;
                }

                switch (i) {
                    case 0 :
                        strings.add("الحمي");
                        break;
                    case 1 :
                        strings.add("السعال الجاف");
                        break;
                    case 2 :
                        strings.add("الإرهاق العام");
                        break;
                    case 3 :
                        strings.add("ألام في الصدر");
                        break;
                    case 4 :
                        strings.add("عدم القدرة علي الحركة والكلام");
                        break;
                    case 5 :
                        strings.add("ضيق التنفس");
                        break;
                    case 6 :
                        strings.add("إالتهاب في الحلق");
                        break;
                    case 7 :
                        strings.add("فقدان حاسة الشم والتذوق");
                        break;
                    case 8 :
                        strings.add("الصداع");
                        break;
                    case 9 :
                        strings.add("طفح جلدي");
                        break;
                    case 10 :
                        strings.add("الإسهال");
                        break;
                }
            }
        }
    }


    public void PrepareWork(Long Time) {
        Log.e("medical", "PrepareWork");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext() , MyBroadCast.class) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (alarmManager != null)
                // setAlarmClock ( AlarmClockInfo (TriggerTime , ShowIntent) , PendingIntent )
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo( Time , null), GetPendingIntentOfAlarm(getApplicationContext() , intent));
        }
        else {
            // before api level 21 use setExact with RTC_WAKEUP :
            alarmManager.setExact(AlarmManager.RTC_WAKEUP , Time  , GetPendingIntentOfAlarm(getApplicationContext() , intent));
        }
    }
    private static PendingIntent GetPendingIntentOfAlarm(Context context, Intent intent) {
        return PendingIntent.getBroadcast(context.getApplicationContext(),
                100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void Init() {
        firebaseDatabase = FirebaseDatabase.getInstance() ;
        databaseReference = firebaseDatabase.getReference().child("CoronaLocations");
        activityIntroBinding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        View view = activityIntroBinding.getRoot();
        setContentView(view);
        SetFullScreen();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void SetFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }


    private void SetUpViewPager() {
        questionItems = getQuestionItems();
        QuestionsAdapter pagerAdapter = new QuestionsAdapter(this, questionItems, activityIntroBinding.getRoot());
        activityIntroBinding.viewPager.setAdapter(pagerAdapter);
        activityIntroBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  Log.e("ab_do"  , "onPageSelected " + position);
                  UpdateButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<QuestionItem> getQuestionItems() {
        List<QuestionItem> questionItems = new ArrayList<QuestionItem>();
        // 2 :
        questionItems.add(new QuestionItem("هل تعاني من الحمي", R.drawable.ic_fever , "None"));
        questionItems.add(new QuestionItem("هل تعاني من السعال الجاف", R.drawable.ic_sore_throat , "None"));
        questionItems.add(new QuestionItem("هل تعاني من الإرهاق والتعب", R.drawable.ic_weakness , "None"));
        questionItems.add(new QuestionItem("هل تعاني من وجع والألام في الصدر", R.drawable.ic_chest_pain_or_pressure , "None"));
        questionItems.add(new QuestionItem("هل تعاني من عدم القدره علي الحركه والكلام", R.drawable.ic_dizzy , "None"));
        questionItems.add(new QuestionItem("هل تعاني من ضيق في التنفس", R.drawable.ic_difficulty_breathing , "None"));
        // 1 :
        questionItems.add(new QuestionItem("هل تعاني من التهاب في الحلق", R.drawable.ic_sore_throat , "None"));
        questionItems.add(new QuestionItem("هل تعاني من فقدان حاستي الشم والتذوق", R.drawable.ic_loss_of_sense_of_taste , "None"));
        questionItems.add(new QuestionItem("هل تعاني من الصداع", R.drawable.ic_headache , "None"));
        questionItems.add(new QuestionItem("هل تعاني من طفح جلدي", R.drawable.ic_skin_rash , "None"));
        questionItems.add(new QuestionItem("هل تعاني من الإسهال", R.drawable.ic_abdominal_pain , "None"));
        return questionItems;
    }


    @Override
    public void onBackPressed() {
        int pos = activityIntroBinding.viewPager.getCurrentItem();
        if (pos == 0)
            super.onBackPressed();
        else {
            pos--;
            activityIntroBinding.viewPager.setCurrentItem(pos);
        }
    }
    public void UpdateButtons(int pos ) {
        QuestionItem introItem = questionItems.get(pos) ;
        switch (introItem.getSelected()) {
            case "None":
                yes_btn.setAlpha(1);
                no_btn.setAlpha(1);
                yes_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.custombuttonalram));
                no_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.custombuttonalram));

                break;
            case "Yes":
                //yes_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_yes));
                //yes_btn.setEnabled(false);
                yes_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.yes_btn));
                no_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.custombuttonalram));
                no_btn.setAlpha(0.5f);
                yes_btn.setAlpha(1);
                //no_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.btnedit));
                break;
            case "No":
                //no_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_yes));
                no_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.yes_btn));
                yes_btn.setBackground(ContextCompat.getDrawable(this , R.drawable.custombuttonalram));
                yes_btn.setAlpha(0.5f);
                no_btn.setAlpha(1);
                //yes_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.btnedit));
                break;
        }
    }
    private boolean IsAllQuestionsIsAnswered () {
        for (QuestionItem x : questionItems) {
            if (x.getSelected().equals("None"))
                return false ;
        }

        return true ;
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

