package com.example.e7gzly.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzly.R;
import com.example.e7gzly.activity.Home;
import com.example.e7gzly.adapters.ResultAdapter;
import com.example.e7gzly.model.StopStationsModel;
import com.example.e7gzly.model.TrainModel;
import com.example.e7gzly.model.TripModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.e7gzly.utilities.Constants.STOP_STATIONS;
import static com.example.e7gzly.utilities.Constants.TRAINS;
import static com.example.e7gzly.utilities.Constants.TRAIN_CLASS;
import static com.example.e7gzly.utilities.Constants.TRAIN_ID;
import static com.example.e7gzly.utilities.Constants.TRIP;

public class ResultFragment extends Fragment {
    //ARGs
    private static final String FROM_KAY = "from";
    private static final String FROM_ID_KAY = "from_id";
    private static final String TO_KAY = "to";
    private static final String TO_ID_KAY = "to_id";
    private static final String TRAIN_CLASS_KAY = "class";

    private String from;
    private String from_id;
    private String to;
    private String to_id;
    private String train_class;

    private RecyclerView result_rv;
    private ResultAdapter adapter;

    private TripModel tripModel;
    private ArrayList<TripModel> trip_list = new ArrayList();
    private ArrayList<TrainModel> train_list = new ArrayList();
    private ArrayList<StopStationsModel> from_stations_list = new ArrayList();
    private ArrayList<StopStationsModel> to_stations_list = new ArrayList();

    private FirebaseDatabase dp = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = dp.getReference();


    public static ResultFragment newInstance(String from, String from_id, String to, String to_id, String train_class) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(FROM_KAY, from);
        args.putString(FROM_ID_KAY, from_id);
        args.putString(TO_KAY, to);
        args.putString(TO_ID_KAY, to_id);
        args.putString(TRAIN_CLASS_KAY, train_class);
        fragment.setArguments(args);
        return fragment;
    }

    public static ResultFragment newInstance(String from, String from_id, String to, String to_id) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(FROM_KAY, from);
        args.putString(FROM_ID_KAY, from_id);
        args.putString(TO_KAY, to);
        args.putString(TO_ID_KAY, to_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString(FROM_KAY);
            from_id = getArguments().getString(FROM_ID_KAY);
            to = getArguments().getString(TO_KAY);
            to_id = getArguments().getString(TO_ID_KAY);
            train_class = getArguments().getString(TRAIN_CLASS_KAY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        result_rv = view.findViewById(R.id.rv_trips);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Home) getActivity()).setActionBarTitle("Result");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        result_rv.setHasFixedSize(true);
        result_rv.setLayoutManager(linearLayoutManager);
        getTrips();

    }

    private void getTrips() {
        Query query = databaseReference.child(TRAINS);
        ;
        final ArrayList<TrainModel> all_train = new ArrayList();
        final ArrayList<TripModel> trips_before_filtered = new ArrayList();

        if (TextUtils.isEmpty(train_class) || train_class.equals("اختر نوع القطار")) {

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        all_train.add(snapshot.getValue(TrainModel.class));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Loading Error", Toast.LENGTH_SHORT).show();
                    Log.println(Log.ERROR, "Train onCancelled : ", databaseError.getMessage() + "\n" + databaseError.getDetails());
                }
            });


            databaseReference.child(TRIP).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // git available trip has "selected from station"
                    for (DataSnapshot ds_trip : dataSnapshot.getChildren()) {

                        for (DataSnapshot ds_stop_sta : ds_trip.child(STOP_STATIONS).getChildren()) {
                            StopStationsModel from_station = ds_stop_sta.getValue(StopStationsModel.class);

                            if (from_station.getSt_id().equalsIgnoreCase(from_id)) {
                                tripModel = ds_trip.getValue(TripModel.class);

                                for (StopStationsModel to_station : tripModel.getStop_stations()) {

                                    if (to_station.getSt_id().equalsIgnoreCase(to_id)) {

                                        if (from_station.getSt_pos() < to_station.getSt_pos()) {
                                            from_stations_list.add(from_station);
                                            to_stations_list.add(to_station);
                                            trip_list.add(tripModel);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (TripModel trip : trip_list) {
                        for (TrainModel train : all_train) {
                            if (trip.getTrain_id().equalsIgnoreCase(train.getTrain_id())) {
                                train_list.add(train);
                            }
                        }
                    }
                    showResult();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Loading Error", Toast.LENGTH_SHORT).show();
                    Log.println(Log.ERROR, "Trip onCancelled : ", databaseError.getMessage() + "\n" + databaseError.getDetails());
                }
            });
        } else {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child(TRAIN_CLASS).getValue(String.class).equalsIgnoreCase(train_class)) {
                            train_list.add(ds.getValue(TrainModel.class));

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Loading Error", Toast.LENGTH_SHORT).show();
                    Log.println(Log.ERROR, "Train onCancelled : ", error.getMessage() + "\n" + error.getDetails());
                }
            });



            databaseReference.child(TRIP).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot trip_ds : snapshot.getChildren()) {
                        for (TrainModel train : train_list) {
                            if (trip_ds.child(TRAIN_ID).getValue(String.class).equalsIgnoreCase(train.getTrain_id())) {
                                for (DataSnapshot ds_stop_sta : trip_ds.child(STOP_STATIONS).getChildren()) {
                                    StopStationsModel from_station = ds_stop_sta.getValue(StopStationsModel.class);

                                    if (from_station.getSt_id().equalsIgnoreCase(from_id)) {
                                        tripModel = trip_ds.getValue(TripModel.class);

                                        for (StopStationsModel to_station : tripModel.getStop_stations()) {

                                            if (to_station.getSt_id().equalsIgnoreCase(to_id)) {

                                                if (from_station.getSt_pos() < to_station.getSt_pos()) {
                                                    from_stations_list.add(from_station);
                                                    to_stations_list.add(to_station);
                                                    trip_list.add(tripModel);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    showResult();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void showResult() {
        adapter = new ResultAdapter(trip_list, train_list, from_stations_list, to_stations_list, getContext());
        result_rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new ResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "item "+ position + "clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), String.valueOf(trip_list.get(position).getTrip_line()), Toast.LENGTH_SHORT).show();
            }
        });

//        Log.println(Log.ASSERT, "FROM STATION: ", String.valueOf(from_stations_list));
//        Log.println(Log.ASSERT, "TO STATION: ", String.valueOf(to_stations_list));
//        Log.println(Log.ASSERT, "TRIPS: ", String.valueOf(trip_list));
//        Log.println(Log.ASSERT, "TRAINS: ", String.valueOf(train_list));

    }

}
