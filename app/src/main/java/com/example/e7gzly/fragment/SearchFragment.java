package com.example.e7gzly.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.activity.Home;
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
public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private View v;

    private String selected_from, selected_to, selected_class;
    private String selected_from_id, selected_to_id;
    private boolean is_checked;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StationAdapter from_adapter, to_adapter;
    private Spinner from_spinner, to_spinner, class_spinner;
    private CheckBox checkBox_class;
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
        class_spinner = v.findViewById(R.id.sp_train_class);
        search_btn = v.findViewById(R.id.btn_search);
        checkBox_class = v.findViewById(R.id.cb_class);
        to_spinner.setEnabled(false);
        class_spinner.setEnabled(false);
        search_btn.setEnabled(false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseReference.keepSynced(true);
        stationsList();
        checkBox_class.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_checked = isChecked;
                if (isChecked) {
                    class_spinner.setEnabled(true);
                    class_spinner.setOnItemSelectedListener(SearchFragment.this);

                }
            }
        });
        search_btn.setOnClickListener(this);

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
        this.stations_list = stations_list;
        from_adapter = new StationAdapter(getContext(), stations_list);
        from_spinner.setAdapter(from_adapter);
        from_spinner.setOnItemSelectedListener(this);
        to_spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_from:
                StationsModel from_click = (StationsModel) from_spinner.getSelectedItem();
                selected_from = from_click.getSt_name();
                selected_from_id = from_click.getSt_id();
                if (position != 0) {

                    to_adapter = new StationAdapter(getContext(), stations_list, position);
                    to_spinner.setAdapter(to_adapter);
                    to_spinner.setEnabled(true);

                } else {
                    to_spinner.setAdapter(null);
                    to_spinner.setEnabled(false);
                }
                break;
            case R.id.sp_to:

                StationsModel to_click = (StationsModel) to_spinner.getSelectedItem();
                if (to_spinner.getSelectedItem() != null && position != 0) {
                    selected_to = to_click.getSt_name();
                    selected_to_id = to_click.getSt_id();
                    search_btn.setEnabled(true);
                } else {
                    search_btn.setEnabled(false);
                }
                break;

            case R.id.sp_train_class:
                if (!class_spinner.getSelectedItem().equals("اختر نوع القطار")) {
                    selected_class = class_spinner.getSelectedItem().toString();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        ResultFragment fragment;
        if (!is_checked) {
            fragment = ResultFragment.newInstance(selected_from, selected_from_id, selected_to, selected_to_id);
        } else {
            fragment = ResultFragment.newInstance(selected_from, selected_from_id, selected_to, selected_to_id, selected_class);
        }
        if (getActivity() != null) {
            ((Home) getActivity()).loadFragment(fragment);
        }

    }
}
