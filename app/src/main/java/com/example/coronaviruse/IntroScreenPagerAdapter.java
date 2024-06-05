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

public class IntroScreenPagerAdapter extends PagerAdapter {
    private final Context context ;
    private final List<ScreenIntroItem> screenIntroItems ;
    private final ViewGroup Root ;

    public IntroScreenPagerAdapter(Context context, List<ScreenIntroItem> screenIntroItems , ViewGroup view) {
        this.context = context;
        this.screenIntroItems = screenIntroItems;
        this.Root = view ;
    }

    @Override
    public int getCount() {
        return screenIntroItems.size() ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
       return  view == object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("ab_do" , "instantiateItem") ;
        View LayoutScreen = LayoutInflater.from(context).inflate(R.layout.intro_screen, Root , false) ;
        TextView Text = LayoutScreen.findViewById(R.id.textView2) ;
        ImageView imageView = LayoutScreen.findViewById(R.id.imageView2) ;
        Text.setText(screenIntroItems.get(position).getDescription());
        imageView.setImageResource(screenIntroItems.get(position).getImage());
        container.addView(LayoutScreen);
        imageView.clearAnimation();
//

        return LayoutScreen ;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.e("ab_do" , "destroyItem" );
        container.removeView((View) object);
    }
}
