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
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;





public class Medicine_adapters extends PagerAdapter {
    private final Context context ;
    private List<Medicine> medicines ;
    private final ViewGroup Root ;

    public Medicine_adapters(Context context, List<Medicine> medicines , ViewGroup view) {
        this.context = context;
        this.medicines = medicines;
        this.Root = view ;
    }

    @Override
    public int getCount() {
        return medicines.size() ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("ab_do" , "instantiateItem  " + position) ;
        Medicine medicine = medicines.get(position) ;
        View LayoutScreen = LayoutInflater.from(context).inflate(R.layout.medicine_item , Root , false) ;
        TextView med_name = LayoutScreen.findViewById(R.id.med_name) ;
        TextView med_count = LayoutScreen.findViewById(R.id.count) ;
        med_name.setText(medicine.getMed_name());
        med_count.setText(medicine.getMed_details());
        container.addView(LayoutScreen);
        return LayoutScreen ;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.e("ab_do" , "destroyItem" );
        container.removeView((View) object);
    }

    public void SetAdapter(List<Medicine> medicines) {
        this.medicines = medicines ;
        notifyDataSetChanged();
    }

}


