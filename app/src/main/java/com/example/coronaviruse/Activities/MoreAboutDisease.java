package com.example.coronaviruse.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.coronaviruse.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MoreAboutDisease extends AppCompatActivity {
    String Class ;
    TextView header , content ;
    int type ;
    Button back ;
    String Cls , details ;
    String [] classes = {"Melanoma" , "Vascular Lesions" , "Basal cell carcinoma" , "pigmented  keratosis lesions" , "benign keratosis lesions" , "Eczema"};
    String [] Details = {
            "هو نوع من أنواع سرطان الجلد يبدأ في الخلية (الخلايا الميلانية) التي تتحكم في صباغ جلدك" ,
            "هي تشوهات شائعة نسبيًا في الجلد والأنسجة الكامنة ، والمعروفة أكثر باسم الوحمات" ,
            "سرطان الخلايا القاعدية الوحمانية عبارة عن نوع من سرطان الجلد. يبدأ سرطان الخلايا القاعدية الوحمانية في الخلايا القاعدية — نوع من الخلايا الموجودة داخل الجلد وهذه الخلايا تنتج خلايا جديدة للبشرة عوضًا عن الخلايا التي تموت."
            ,
            "هو ورم جلدي غير سرطاني (حميد) ينشأ من الخلايا الكيراتينية في الطبقة الخارجية من الجلد. غالبًا يظهر التقران المثي، مثل بقع الكبد مع تقدم العمر" ,
            "هو ورم جلدي غير سرطاني (حميد) ينشأ من الخلايا الكيراتينية في الطبقة الخارجية من الجلد. غالبًا يظهر التقران المثي، مثل بقع الكبد مع تقدم العمر" ,
            "هو شكل من أشكال التهاب الطبقات العليا من الجلد. المصطلح يشمل الجفاف المتكرر والطفح الجلدي والذي يتميز بالإحمرار، والحكة، وجفاف، والتقشير"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about_disease);
        back = findViewById(R.id.back);
        type = getIntent().getIntExtra("Class" , 0);
        Cls = classes[type];
        details = Details[type];
        header = findViewById(R.id.header);
        content = findViewById(R.id.content);
        header.setText("ماهو مرض " + Cls);
        content.setText(details);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , takeOrUpload.class));
                finish();
            }
        });
    }
}