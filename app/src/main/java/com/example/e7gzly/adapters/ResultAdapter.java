package com.example.e7gzly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return new ResultHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {

        TripModel tripModel = trip_list.get(position);
        TrainModel trainModel = train_list.get(position);
        StopStationsModel from = from_stations_list.get(position);
        StopStationsModel to = to_stations_list.get(position);
        
        if (tripModel != null && trainModel != null && from != null && to != null) {
            holder.line.setText("Line : " + tripModel.getTrip_line());
            holder.train_class.setText("Class : " + trainModel.getTrain_class());
            holder.from.setText("From : " + from.getSt_name());
            holder.to.setText("To : " + to.getSt_name());
            holder.leave.setText("Leave At : " + CALCULATE_LEAVE_TIME(from.getArrive_time()));
            holder.arrive.setText("Arrive At : " + to.getArrive_time());

        }

    }

    @Override
    public int getItemCount() {
        return trip_list.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener ){
        this.clickListener =clickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView line;
        public TextView from;
        public TextView to;
        public TextView leave;
        public TextView arrive;
        public TextView train_class;
        public OnItemClickListener listener;

        public ResultHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            line = itemView.findViewById(R.id.tv_line);
            from = itemView.findViewById(R.id.tv_from);
            to = itemView.findViewById(R.id.tv_to);
            leave = itemView.findViewById(R.id.tv_leave_time);
            arrive = itemView.findViewById(R.id.tv_arrive_time);
            train_class = itemView.findViewById(R.id.tv_train_class);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getAdapterPosition());
        }
    }
}
