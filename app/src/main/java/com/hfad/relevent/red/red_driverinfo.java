package com.hfad.relevent.red;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
//import com.hfad.relevent.db_send;
import com.hfad.relevent.driver;
import com.hfad.SAADApplication.R;
import java.util.List;

public class red_driverinfo extends RecyclerView.Adapter<red_driverinfo.ViewHolder>{

    Context context;
    List<driver> driverList;
    Task<Void> reference;

    public red_driverinfo(Context context , List<driver> driverList) {
        this.context = context;
        this.driverList=driverList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (driverList != null && driverList.size() > 0){
            driver driver_model = driverList.get(position);

            holder.mLocation.setText("Location");
            holder.mLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DriverLocation = driver_model.getLocation();
                    Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(DriverLocation));
                    v.getContext().startActivity(in);
                }

            });



            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    driverList.remove(driver_model);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, driverList.size());
                    String id = driver_model.getId();
                    reference = FirebaseDatabase.getInstance().getReference().child("new_alert").child(id).removeValue();


                }
            });

            holder.data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, driverData.class);
                    intent.putExtra("massege", driver_model.getMassege());
                    v.getContext().startActivity(intent);

                }
            });

        }else {
            return;
        }
    }



    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  mLocation;
        Button done, data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLocation = itemView.findViewById(R.id.name_tv);
            data = itemView.findViewById(R.id.payment_tv);
            done = itemView.findViewById(R.id.payment_tv1);

        }
    }
}
