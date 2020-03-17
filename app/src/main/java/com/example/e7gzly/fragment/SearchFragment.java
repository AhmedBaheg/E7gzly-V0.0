package com.example.e7gzly.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.adapters.StationAdapter;
import com.example.e7gzly.model.StationsModel;
import com.example.e7gzly.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View v;

    private String selected_from, selected_to;
    private String selected_from_id;
    private String selected_to_id;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StationAdapter from_adapter, to_adapter;
    private Spinner from_spinner, to_spinner, date_spinner, time_spinner;
    private Button search_btn;
    private ArrayList<StationsModel> stations_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search, container, false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.keepSynced(true);
        from_spinner = v.findViewById(R.id.sp_from);
        to_spinner = v.findViewById(R.id.sp_to);
        date_spinner = v.findViewById(R.id.sp_train_class);
        search_btn = v.findViewById(R.id.btn_search);
        to_spinner.setEnabled(false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseReference.keepSynced(true);
        stationsList();

    }

    private void stationsList() {
        final ArrayList<StationsModel> stations_list = new ArrayList<>();
        databaseReference.child(Constants.STATIONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StationsModel stations = snapshot.getValue(StationsModel.class);
                    stations_list.add(stations);
                }

                spinnerSelected(stations_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void spinnerSelected(final ArrayList<StationsModel> stations_list) {
        this.stations_list=stations_list;
        from_adapter = new StationAdapter(getContext(), stations_list);

        from_spinner.setAdapter(from_adapter);

        from_spinner.setOnItemSelectedListener(this);
        to_spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_from :
                StationsModel from_click = (StationsModel) from_spinner.getSelectedItem();
                selected_from = from_click.getSt_name();
                selected_from_id = from_click.getSt_id();
                if (position !=0){
                    to_adapter = new StationAdapter(getContext(), stations_list,position);
                    to_spinner.setAdapter(to_adapter);
                    to_spinner.setEnabled(true);
                }else {
                    to_spinner.setAdapter(null);
                    to_spinner.setEnabled(false);
                }
                break;
            case R.id.sp_to :
                StationsModel to_click = (StationsModel) to_spinner.getSelectedItem();
                selected_to = to_click.getSt_name();
                selected_to_id = to_click.getSt_id();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}