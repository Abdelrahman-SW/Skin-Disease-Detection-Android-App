package com.example.coronaviruse.FindNearbyHospitals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaviruse.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptersRecycle extends RecyclerView.Adapter<AdaptersRecycle.Holder> {

    List<Hospital> hospitals;
    Context context;
    ClickListener clickListener ;
    public AdaptersRecycle(Context context) {
        hospitals = new ArrayList<>();
        this.context = context;
        clickListener = (ClickListener) context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hos_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Hospital hospital = hospitals.get(position);
        holder.place_img.setImageDrawable(ContextCompat.getDrawable(context , hospital.getPlace_img() ));
        if (hospital.getName() == null || hospital.getName().length() == 0)
            holder.HospitalName.setText("غير محدد");
        else
            holder.HospitalName.setText(hospital.getName());

        if (hospital.getVicinity() == null || hospital.getVicinity().length() == 0)
            holder.vicinity_layout.setVisibility(View.GONE);
        else {
            holder.vicinity_layout.setVisibility(View.VISIBLE);
            holder.vicinity.setText(hospital.getVicinity());
        }

        if (hospital.isIs_open())
            holder.Statues.setText("مفتوح الان");
        else
            holder.Statues.setText("مغلق");
        Log.e("ab_do" , "Rat " + hospital.getRating()) ;
        switch ((int) hospital.getRating()) {
            case 1:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                break;
            case 2:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                break;
            case 3:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                break;
            case 4:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                break;
            case 5:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));


            default:
                holder.star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
                holder.star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
        }


     
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public void SetAdapter(List<Hospital> hospitals) {
        Log.e("ab_do" , "SetAdapter");
        this.hospitals = hospitals ;
        notifyDataSetChanged();
    }

     class Holder extends RecyclerView.ViewHolder {
        TextView HospitalName ,  Statues , vicinity;
        ImageView star1 , star2 , star3 , star4 , star5 ;
        LinearLayout vicinity_layout ;
        ImageView place_img ;
        public Holder(@NonNull View itemView) {
            super(itemView);
            HospitalName = itemView.findViewById(R.id.med1_name);
            Statues = itemView.findViewById(R.id.status);
            vicinity = itemView.findViewById(R.id.vicinity_name);
            star1 = itemView.findViewById(R.id.star_1);
            star2 = itemView.findViewById(R.id.star_2);
            star3 = itemView.findViewById(R.id.star_3);
            star4 = itemView.findViewById(R.id.star_4);
            star5 = itemView.findViewById(R.id.star_5);
            place_img = itemView.findViewById(R.id.place_img);
            vicinity_layout = itemView.findViewById(R.id.vicinity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickOnHospital(hospitals.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ClickListener {
         void onClickOnHospital(Hospital hospital);
    }

}


