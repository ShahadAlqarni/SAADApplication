package com.hfad.relevent.red;


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

import com.hfad.relevent.driver;
import com.hfad.SAADApplication.R;
import java.util.List;

public class red_AllDriverinfo extends RecyclerView.Adapter<red_AllDriverinfo.ViewHolder> {
    Context context;
    List<driver> allDriverList;

    public red_AllDriverinfo(Context context , List<driver> driverList) {
        this.context = context;
        this.allDriverList = driverList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (allDriverList != null && allDriverList.size() > 0){
            driver driver_model = allDriverList.get(position);
            holder.mLocation1.setText("Location");
            holder.mLocation1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DriverLocation = driver_model.getLocation();
                    Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(DriverLocation));
                    v.getContext().startActivity(in);
                }

            });
            holder.data1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(context, driverData.class);
                    intent.putExtra("massege", driver_model.getMassege());
                    v.getContext().startActivity(intent);

                }
            });
            holder.done1.setText("------");
        }else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return allDriverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  mLocation1, done1;
        Button  data1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLocation1 = itemView.findViewById(R.id.all_name);
            data1 = itemView.findViewById(R.id.all_data);
            done1 = itemView.findViewById(R.id.all_done);

        }
    }
}


