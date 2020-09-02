package com.example.e7gzly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzly.R;
import com.example.e7gzly.model.StopStationsModel;
import com.example.e7gzly.model.TrainModel;
import com.example.e7gzly.model.TripModel;

import java.util.ArrayList;

import static com.example.e7gzly.utilities.Utils.CALCULATE_LEAVE_TIME;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private ArrayList<TripModel> trip_list;
    private ArrayList<TrainModel> train_list;
    private ArrayList<StopStationsModel> from_stations_list;
    private ArrayList<StopStationsModel> to_stations_list;
    private Context context;
    private OnItemClickListener clickListener;

    public ResultAdapter(ArrayList<TripModel> trip_list, ArrayList<TrainModel> train_list, ArrayList<StopStationsModel> from_stations_list, ArrayList<StopStationsModel> to_stations_list, Context context) {
        this.trip_list = trip_list;
        this.train_list = train_list;
        this.from_stations_list = from_stations_list;
        this.to_stations_list = to_stations_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trips_item, parent, false);
        return new ResultHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {

        TripModel tripModel = trip_list.get(position);
        TrainModel trainModel = train_list.get(position);
        StopStationsModel from = from_stations_list.get(position);
        StopStationsModel to = to_stations_list.get(position);

        if (tripModel != null && trainModel != null && from != null && to != null) {
            holder.trip.setText(tripModel.getTrip_id());
            holder.train_class.setText(trainModel.getTrain_class());
            holder.leave.setText(CALCULATE_LEAVE_TIME(from.getArrive_time()));
            holder.arrive.setText(to.getArrive_time());

        }

    }

    @Override
    public int getItemCount() {
        return trip_list.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ResultHolder extends RecyclerView.ViewHolder{
        public TextView trip;
        public TextView from;
        public TextView to;
        public TextView leave;
        public TextView arrive;
        public TextView train_class;
        public Button btn_book_now;
        public OnItemClickListener listener;

        public ResultHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
//            itemView.setOnClickListener(this);
            trip = itemView.findViewById(R.id.tv_trip_num);
            leave = itemView.findViewById(R.id.time_leave);
            arrive = itemView.findViewById(R.id.time_arrive);
            train_class = itemView.findViewById(R.id.tv_class_item);
            btn_book_now = itemView.findViewById(R.id.btn_book_now);
            btn_book_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v , getAdapterPosition());
                }
            });
            this.listener = listener;
        }
//
//        @Override
//        public void onClick(View v) {
//
//            listener.onItemClick(v, getAdapterPosition());
//
//        }
    }
}
