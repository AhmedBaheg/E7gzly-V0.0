package com.example.e7gzly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.e7gzly.R;
import com.example.e7gzly.model.StationsModel;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<StationsModel> {
    int pos = -1;

    public StationAdapter(@NonNull Context context, @NonNull ArrayList<StationsModel> stations) {
        super(context, 0, stations);
    }

    public StationAdapter(@NonNull Context context, @NonNull ArrayList<StationsModel> stations, int pos) {
        super(context, 0, stations);
        this.pos = pos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = initView(position, convertView, parent);
        return view;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.statiom_spinner_raw, parent, false);
        }
//        TextView id = convertView.findViewById(R.id.tv_station_id);
        TextView textViewName = convertView.findViewById(R.id.tv_station_name);

        StationsModel stations = getItem(position);

        if (pos != -1) {
            if (position != pos) {
                if (stations != null) {

//                    id.setVisibility(View.VISIBLE);
                    textViewName.setVisibility(View.VISIBLE);
//                    id.setText(String.valueOf(stations.getSt_id()));
                    textViewName.setText(stations.getSt_name());
                }

            } else {
//                id.setVisibility(View.GONE);
                textViewName.setVisibility(View.GONE);

            }
        } else {
            if (stations != null) {
//                id.setText(String.valueOf(stations.getSt_id()));
                textViewName.setText(stations.getSt_name());
            }
        }
        return convertView;
    }

}
