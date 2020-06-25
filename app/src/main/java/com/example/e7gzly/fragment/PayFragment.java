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

    private final static String LINE_KEY = "line_key";
    private final static String FROM_KEY = "from_key";
    private final static String TO_KEY = "to_key";
    private final static String LEAVE_KEY = "leave_key";
    private final static String ARRIVE_KEY = "arrive_key";
    private final static String SEATS_KEY = "seats_key";
    private final static String PRICE_KEY = "price_key";
    private final static String CLASS_KEY = "class_key";
    private final static String DATE_KEY = "date_key";
    private final static String CODE_KEY = "code_key";

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

    private String line;
    private String from;
    private String to;
    private String leave;
    private String arrive;
    private int seats;
    private double price;
    private String class_train;
    private String date;
    private String code;

    private DatabaseReference databaseReference;

    public static PayFragment newInstance(String line, String from, String to, String leave, String arrive,
                                          int seats, double price, String class_train, String date, String code){

        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();

        args.putString(LINE_KEY , line);
        args.putString(FROM_KEY , from);
        args.putString(TO_KEY , to);
        args.putString(LEAVE_KEY , leave);
        args.putString(ARRIVE_KEY , arrive);
        args.putInt(SEATS_KEY , seats);
        args.putDouble(PRICE_KEY , price);
        args.putString(CLASS_KEY , class_train);
        args.putString(DATE_KEY , date);
        args.putString(CODE_KEY , code);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){

            line = getArguments().getString(LINE_KEY);
            from = getArguments().getString(FROM_KEY);
            to = getArguments().getString(TO_KEY);
            leave = getArguments().getString(LEAVE_KEY);
            arrive = getArguments().getString(ARRIVE_KEY);
            seats = getArguments().getInt(SEATS_KEY);
            price = getArguments().getDouble(PRICE_KEY);
            class_train = getArguments().getString(CLASS_KEY);
            date = getArguments().getString(DATE_KEY);
            code = getArguments().getString(CODE_KEY);

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

        line_pay.setText(line);
        from_pay.setText(from);
        to_pay.setText(to);
        leave_pay.setText(leave);
        arrive_pay.setText(arrive);
        seats_pay.setText(""+ seats);
        price_pay.setText(price + " L.E");
        class_pay.setText(class_train);
        date_pay.setText(date);
        code_pay.setText(code);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTicketInfoOnDB();
            }
        });

    }

    private void saveTicketInfoOnDB(){

        String key = databaseReference.push().getKey();

        PassengerInfo info = new PassengerInfo(line, from, to, leave, arrive, class_train, date, seats, price, code);

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