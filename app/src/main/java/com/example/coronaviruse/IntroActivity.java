package com.example.coronaviruse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.coronaviruse.Activities.Login;
import com.example.coronaviruse.Activities.takeOrUpload;
import com.example.coronaviruse.databinding.ActivityIntroBinding;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding activityIntroBinding ;
    SharedPreferences sharedPreferences ;
    List<ScreenIntroItem> screenIntroItems ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) ;
        CheckIfShouldIgnoreThisScreen();
        Init();
        SetUpViewPager();
        SetListeners();
    }

    private void Init() {
        activityIntroBinding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = activityIntroBinding.getRoot();
        setContentView(view);
        SetFullScreen();
    }

    private void SetFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        }
        else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    private void CheckIfShouldIgnoreThisScreen() {
        if (!IsFirstTimeOpenTheApp()) {
            startActivity(new Intent(IntroActivity.this , takeOrUpload.class));
            finish();
            overridePendingTransition(0 , R.anim.fade_out);
        }
    }
    private boolean IsFirstTimeOpenTheApp() {
        return sharedPreferences.getBoolean("IsFirstTimeOpenTheApp", true) ;
    }
    private void SetUpViewPager() {
        screenIntroItems = getScreenIntroItems();
        IntroScreenPagerAdapter pagerAdapter = new IntroScreenPagerAdapter(this, screenIntroItems , activityIntroBinding.getRoot());
        activityIntroBinding.viewPager.setAdapter(pagerAdapter);
        activityIntroBinding.tabLayout.setupWithViewPager(activityIntroBinding.viewPager);
    }
    private List<ScreenIntroItem> getScreenIntroItems() {
        List<ScreenIntroItem> screenIntroItems = new ArrayList<>();
        screenIntroItems.add(new ScreenIntroItem("مرحبا بك في التطبيق الخاص بالتعرف علي الامراض الجلدية", R.drawable.skin));
        screenIntroItems.add(new ScreenIntroItem("الامراض الجلدية يجب التعامل معها مبكرا حتي لا تتضاعف ", R.drawable.skin_2));
        screenIntroItems.add(new ScreenIntroItem("يمكنك من خلال التطبيق التعرف علي المرض الجلدي المصاب به ", R.drawable.skin_4));
        screenIntroItems.add(new ScreenIntroItem("عرض بعض النصائح والإرشادات لكيفية التعامل مع المرض الجلدي الخاص بك", R.drawable.skin_3));
        screenIntroItems.add(new ScreenIntroItem("عرض المستشيفات والمراكز الخاصه بالأمراض الجلدية القريبة منك", R.drawable.login_skin));
//        screenIntroItems.add(new ScreenIntroItem("عرض برتوكول العلاج الخاص بنسب الإشتباه المنخفضة بالإصابة بفيروس كورونا والمرخصة من وزارة الصحة ", R.drawable.ic_vitamin));
//        screenIntroItems.add(new ScreenIntroItem("متابعة يومية مع التطبيق لمتابعة حالتك الصحية", R.drawable.ic_microscope));
//        screenIntroItems.add(new ScreenIntroItem("إمكانية التواصل مع الأطباء في مختلف التخصصات بنقرة واحدة", R.drawable.ic_doctor));
//        screenIntroItems.add(new ScreenIntroItem("عرض التقرير اليومي الخاص بمتابعتك اليومية لمعرفة تطور حالتك الصحية ", R.drawable.ic_medical_records));
//        screenIntroItems.add(new ScreenIntroItem("تذكيرك يوميا بموعد أخذ أدويتك ", R.drawable.ic_alarm_bell));
//        screenIntroItems.add(new ScreenIntroItem("إمكانية إرسالة رسالة نصية في حالة إشتباهك بالإصابة لجميع المخالطين لك بنقرة واحدة حتي يتوخوا الحذر ", R.drawable.ic_text_message));
//        screenIntroItems.add(new ScreenIntroItem("عرض إحصائيات الإصابة بفيروس كورونا عبر العالم والدول", R.drawable.ic_statistics));
//        screenIntroItems.add(new ScreenIntroItem("تفقد أماكن الإصابات بفيروس كورونا علي الخريطة", R.drawable.ic_worldwide__1_));
//        screenIntroItems.add(new ScreenIntroItem("عرض المستشفيات القريبة ومسار الوصول إليها بنقرة واحدة", R.drawable.ic_ambulance));
        return screenIntroItems;
    }
    private void SetListeners() {
        activityIntroBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                  //  Log.e("ab_do" , "onPageScrolled") ;
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("ab_do", "onPageSelected " + position);
                if (position == screenIntroItems.size() - 1) {
                    // we reach to the last screen :
                    loadLastScreen();
                }
                else {
                    loadNormalScreen();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        activityIntroBinding.NextButton.setOnClickListener(v -> {
            int pos = activityIntroBinding.viewPager.getCurrentItem(); // current page
            pos++; // Go To next Page
            activityIntroBinding.viewPager.setCurrentItem(pos);
        });
        activityIntroBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStarted();
            }
        });
        activityIntroBinding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStarted();
            }
        });
    }
    private void GetStarted() {
        SaveIntroFinishedPrefs();
        startActivity(new Intent(IntroActivity.this, Login.class));
    }
    private void loadNormalScreen() {
        activityIntroBinding.NextButton.setVisibility(View.VISIBLE);
        activityIntroBinding.tvSkip.setVisibility(View.VISIBLE);
        activityIntroBinding.btnGetStarted.setVisibility(View.GONE);
    }
    private void loadLastScreen() {
        activityIntroBinding.NextButton.setVisibility(View.INVISIBLE);
        activityIntroBinding.tvSkip.setVisibility(View.GONE);
        activityIntroBinding.btnGetStarted.setVisibility(View.VISIBLE);
        activityIntroBinding.btnGetStarted.setAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.started_btn_anim));
    }
    private void SaveIntroFinishedPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putBoolean("IsFirstTimeOpenTheApp", false) ;
        editor.apply();
    }
    @Override
    public void onBackPressed() {
        int pos = activityIntroBinding.viewPager.getCurrentItem() ;
        if (pos==0)
        super.onBackPressed();
        else {
            pos -- ;
            activityIntroBinding.viewPager.setCurrentItem(pos);
        }
    }
}