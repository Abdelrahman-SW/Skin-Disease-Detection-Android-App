package com.example.coronaviruse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class QuestionsAdapter extends PagerAdapter {
    private final Context context ;
    private final List<QuestionItem> questionItems;
    private final ViewGroup Root ;

    public QuestionsAdapter(Context context, List<QuestionItem> questionItems, ViewGroup view) {
        this.context = context;
        this.questionItems = questionItems;
        this.Root = view ;
    }

    @Override
    public int getCount() {
        return questionItems.size() ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("ab_do" , "instantiateItem  " + position) ;
        QuestionItem introItem = questionItems.get(position) ;
        Log.e("ab_do" , "? " + introItem.getSelected());
        View LayoutScreen = LayoutInflater.from(context).inflate(R.layout.questions_screen, Root , false) ;
        TextView Text = LayoutScreen.findViewById(R.id.textView2) ;
        ImageView imageView = LayoutScreen.findViewById(R.id.imageView2) ;
        Text.setText(introItem.getDescription());
        //Text.setTypeface(Typeface.createFromAsset(context.getAssets() ,"fonts/Calistoga-Regular.ttf"));
        imageView.setImageResource(introItem.getImage());
        container.addView(LayoutScreen);
        return LayoutScreen ;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.e("ab_do" , "destroyItem" );
        container.removeView((View) object);
    }

}

