package com.example.coronaviruse;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;

public class Medical_Adapter extends RecyclerView.Adapter<Medical_Adapter.MedicalViewHolder> {
    Context context ;
    List<Medical_item> medical_itemList = new ArrayList<>();

    public Medical_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medical_item , parent , false);
        return new MedicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalViewHolder holder, int position) {
         Medical_item medical_item = medical_itemList.get(position);
         holder.title.setText("المتابعة رقم  "+ medical_item.getTitle());
         holder.Report.setText(medical_item.getReport());
         Drawable drawable = ContextCompat.getDrawable(context, medical_item.getDrawable_id()) ;
         holder.Report.setCompoundDrawablesWithIntrinsicBounds( drawable , null , null, null);
         holder.Report.setCompoundDrawablePadding(16);
    }

    @Override
    public int getItemCount() {
        return medical_itemList.size();
    }

    public static class MedicalViewHolder extends RecyclerView.ViewHolder {
        TextView title , Report ;
        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Report_title);
            Report = itemView.findViewById(R.id.Report_details);
        }
    }

    public void SetAdapter (List<Medical_item> items) {
        this.medical_itemList = items ;
        notifyDataSetChanged();
    }
}
