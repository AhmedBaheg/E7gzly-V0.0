package com.example.e7gzly.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.activity.Home;
import com.example.e7gzly.model.PassengerInfo;
import com.example.e7gzly.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PayFragment extends Fragment {

    private final static String PASSENGER_KEY = "passenger";
    private PassengerInfo info;

    private TextView line_pay;
    private TextView from_pay;
    private TextView to_pay;
    private TextView leave_pay;
    private TextView arrive_pay;
    private TextView seats_pay;
    private TextView price_pay;
    private TextView class_pay;
    private TextView date_pay;
    private TextView code_pay;
    private Button btn_confirm;

    private DatabaseReference databaseReference;

    public static PayFragment newInstance(PassengerInfo passengerInfo) {
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putSerializable(PASSENGER_KEY, passengerInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            info = (PassengerInfo) getArguments().getSerializable(PASSENGER_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pay, container, false);

        ((Home) getActivity()).setActionBarTitle("Paying");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        line_pay = view.findViewById(R.id.line_pay);
        from_pay = view.findViewById(R.id.from_pay);
        to_pay = view.findViewById(R.id.to_pay);
        leave_pay = view.findViewById(R.id.leave_pay);
        arrive_pay = view.findViewById(R.id.arrive_pay);
        seats_pay = view.findViewById(R.id.seats_pay);
        price_pay = view.findViewById(R.id.price_pay);
        class_pay = view.findViewById(R.id.class_pay);
        date_pay = view.findViewById(R.id.date_pay);
        code_pay = view.findViewById(R.id.code_pay);
        btn_confirm = view.findViewById(R.id.btn_confirm);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        line_pay.setText(info.getTrain_line());
        from_pay.setText(info.getFrom());
        to_pay.setText(info.getTo());
        leave_pay.setText(info.getLeave());
        arrive_pay.setText(info.getArrive());
        seats_pay.setText(String.valueOf(info.getPassenger_seats()));
        price_pay.setText(info.getPrice() + " L.E");
        class_pay.setText(info.getTrain_class());
        date_pay.setText(info.getDate());
        code_pay.setText(info.getCode());

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTicketInfoOnDB();
            }
        });

    }

    private void saveTicketInfoOnDB(){

        String key = databaseReference.push().getKey();

        databaseReference.child("Ticket_Info")
                .child(Constants.getUID())
                .child(key)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    ((Home) getActivity()).loadFragment(new SearchFragment());
                }
            }
        });

    }

}