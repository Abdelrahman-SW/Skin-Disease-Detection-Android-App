package com.example.coronaviruse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorHolder> {
    Context context ;
    List<DoctorItem> Doctors;
    private static ButtonClicked buttonClicked ;
    public DoctorAdapter(Context context, List<DoctorItem> doctors , ButtonClicked buttonClicked) {
        this.context = context;
        Doctors = doctors;
        DoctorAdapter.buttonClicked = buttonClicked ;
    }

    @NonNull
    @Override
    public DoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item , parent , false);
        return new DoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHolder holder, int position) {
         DoctorItem Doctor = Doctors.get(position);
         holder.Doctor_name.setText(Doctor.getName());
         holder.Doctor_Type.setText(Doctor.getType());
         holder.Doctor_img.setImageDrawable(ContextCompat.getDrawable(context , Doctor.getImg()));
         holder.available.setText(Doctor.isAvailable() ? "متاح الأن" : "غير متاح الأن");
         holder.Doctor_Rat.setText(String.valueOf(Doctor.getRating()));
         holder.Doctor_Time.setText(Doctor.getTime());
    }

    @Override
    public int getItemCount() {
        return Doctors.size();
    }

    public static class DoctorHolder extends RecyclerView.ViewHolder {
        TextView Doctor_name ,  Doctor_Type , Doctor_Time , Doctor_Rat  , available ;
        ImageView Doctor_img , whats_app , messenger, call ;
        public DoctorHolder(@NonNull View itemView) {
            super(itemView);
            Doctor_name = itemView.findViewById(R.id.doctor_name);
            Doctor_Type = itemView.findViewById(R.id.doctor_type);
            Doctor_Time = itemView.findViewById(R.id.doctor_time);
            Doctor_Rat = itemView.findViewById(R.id.rating);
            available = itemView.findViewById(R.id.available);
            Doctor_img = itemView.findViewById(R.id.doctor_img);
            whats_app = itemView.findViewById(R.id.whatsapp);
            messenger = itemView.findViewById(R.id.messanger);
            call = itemView.findViewById(R.id.doctor_call);
            whats_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked.onClick(0 , getAdapterPosition());
                }
            });
            messenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked.onClick(1 , getAdapterPosition());
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked.onClick(2 , getAdapterPosition());
                }
            });
        }
    }

    interface ButtonClicked {
        void onClick (int id_btn , int pos);
    }
}
