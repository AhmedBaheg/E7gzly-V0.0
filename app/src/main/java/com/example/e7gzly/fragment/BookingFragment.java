package com.example.e7gzly.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.activity.Home;
import com.example.e7gzly.model.PassengerInfo;
import com.example.e7gzly.model.StopStationsModel;
import com.example.e7gzly.model.TicketModel;
import com.example.e7gzly.model.TrainModel;
import com.example.e7gzly.model.TripModel;
import com.example.e7gzly.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.example.e7gzly.utilities.Utils.CALCULATE_LEAVE_TIME;

public class BookingFragment extends Fragment {

    private static final String TRIP_KAY = "trip";
    private static final String TRAIN_KAY = "train";
    private static final String STATION_FROM_KAY = "station from";
    private static final String STATION_TO_KAY = "station to";

    private TextView line;
    private TextView tv_class;
    private TextView tv_from;
    private TextView tv_to;
    private TextView tv_leave;
    private TextView tv_arrive;
    private TextView tv_price;
    private TextView date_picker;
    private TextView num_of_seats;
    private TextView ava_seats;
    private Button btn_booking;
    private ImageButton btn_add;
    private ImageButton btn_remove;
    private LinearLayout layout;


    private TicketModel ticketModel;
    private TripModel tripModel;
    private TrainModel trainModel;
    private StopStationsModel fromModel;
    private StopStationsModel toModel;


    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private DatabaseReference databaseReference;

    private String date;
    private double price;
    private int seats_available;
    private int booked_seats = 0;

    public static BookingFragment newInstance(TripModel tripModel, TrainModel trainModel, StopStationsModel fromModel, StopStationsModel toModel) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRIP_KAY, tripModel);
        args.putSerializable(TRAIN_KAY, trainModel);
        args.putSerializable(STATION_FROM_KAY, fromModel);
        args.putSerializable(STATION_TO_KAY, toModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tripModel = (TripModel) args.getSerializable(TRIP_KAY);
            trainModel = (TrainModel) args.getSerializable(TRAIN_KAY);
            fromModel = (StopStationsModel) args.getSerializable(STATION_FROM_KAY);
            toModel = (StopStationsModel) args.getSerializable(STATION_TO_KAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

//        ((Home) getActivity()).setActionBarTitle("Booking");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        layout = view.findViewById(R.id.seats_layout);
        line = view.findViewById(R.id.line);
        tv_class = view.findViewById(R.id.tv_class);
        tv_from = view.findViewById(R.id.from);
        tv_to = view.findViewById(R.id.to);
        tv_leave = view.findViewById(R.id.leave);
        tv_arrive = view.findViewById(R.id.arrive);
        tv_price = view.findViewById(R.id.tv_price);
        date_picker = view.findViewById(R.id.date_picker);
        btn_booking = view.findViewById(R.id.btn_booking);
        num_of_seats = view.findViewById(R.id.seats_booked);
        btn_add = view.findViewById(R.id.add_btn);
        btn_remove = view.findViewById(R.id.remove_btn);
        ava_seats = view.findViewById(R.id.tv_ava_tickets);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        line.setText(tripModel.getTrip_line());
        tv_class.setText(trainModel.getTrain_class());
        tv_from.setText(fromModel.getSt_name());
        tv_to.setText(toModel.getSt_name());
        tv_arrive.setText(toModel.getArrive_time());
        tv_leave.setText(CALCULATE_LEAVE_TIME(fromModel.getArrive_time()));


        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        layout.setVisibility(View.GONE);


        date_picker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getNumberOfSeats();
            }
        });

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
            }
        });
        getPrice();


    }

    private void getSeats() {
        num_of_seats.setText(String.valueOf(booked_seats));
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booked_seats == seats_available) {
                    Toast.makeText(getContext(), "no seats available", Toast.LENGTH_SHORT).show();
                } else {
                    booked_seats++;
                    num_of_seats.setText(String.valueOf(booked_seats));
                }
            }
        });
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booked_seats == 0) {
                    Toast.makeText(getContext(), "no seats selected", Toast.LENGTH_SHORT).show();

                } else {
                    booked_seats--;
                    num_of_seats.setText(String.valueOf(booked_seats));

                }
            }
        });

    }

    private void getPrice() {

        databaseReference.child(Constants.TICKET_PRICES).child(trainModel.getTrain_class()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ticketModel = dataSnapshot.getValue(TicketModel.class);
                    if (fromModel.getSt_id().equalsIgnoreCase(ticketModel.getStation_1()) || fromModel.getSt_id().equalsIgnoreCase(ticketModel.getStation_2())) {

                        if (toModel.getSt_id().equalsIgnoreCase(ticketModel.getStation_1()) || toModel.getSt_id().equalsIgnoreCase(ticketModel.getStation_2())) {

                            price = ticketModel.getPrice();
                            tv_price.setText(price + "  EGP");

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showDatePickerDialog() {

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date_picker.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); // Disable Past Date
        datePickerDialog.show();


    }

    private void booking() {
        String date = date_picker.getText().toString();

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(getContext(), "please choose your trip date", Toast.LENGTH_SHORT).show();
        } else if (booked_seats == 0) {
            Toast.makeText(getContext(), "please choose your number of seats", Toast.LENGTH_SHORT).show();
        } else {
            seats_available = Integer.parseInt(ava_seats.getText().toString());
            int available_seats = seats_available - booked_seats;
            databaseReference
                    .child("seats_available")
                    .child(date)
                    .child(tripModel.getTrip_id())
                    .child("available_seats")
                    .setValue(available_seats);
            getNumberOfSeats();
            generateRandomCodeAndSendDataToFragment();

        }
    }

    private void getNumberOfSeats() {

        date = date_picker.getText().toString();

        if (TextUtils.isEmpty(date)) {
            layout.setVisibility(View.GONE);
            Toast.makeText(getContext(), "please choose date", Toast.LENGTH_SHORT).show();
        } else {
            layout.setVisibility(View.VISIBLE);
            databaseReference.child("seats_available").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(date)) {
                        if (snapshot.child(date).hasChild(tripModel.getTrip_id())) {
                            databaseReference
                                    .child("seats_available")
                                    .child(date)
                                    .child(tripModel.getTrip_id())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            seats_available = (int) snapshot.child("available_seats").getValue(Integer.class);
                                            ava_seats.setText(String.valueOf(seats_available));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        } else {
                            seats_available = trainModel.getSeats();

                            databaseReference.child("seats_available")
                                    .child(date)
                                    .child(tripModel.getTrip_id())
                                    .child("available_seats")
                                    .setValue(seats_available);

                            ava_seats.setText(String.valueOf(seats_available));

                        }
                    } else {
                        seats_available = trainModel.getSeats();

                        databaseReference.child("seats_available")
                                .child(date)
                                .child(tripModel.getTrip_id())
                                .child("available_seats")
                                .setValue(seats_available);

                        ava_seats.setText(String.valueOf(seats_available));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.println(Log.ERROR, "DatabaseError", error.getMessage());
                }
            });


        }

        getSeats();

    }

    private void generateRandomCodeAndSendDataToFragment() {

        int numb_seats_passenger = Integer.parseInt(num_of_seats.getText().toString());
        double total_price = numb_seats_passenger * price;

        int start = 10000000;
        int increment = 9999999;
        int generate = (int) (start + (Math.random() * increment));
        Log.println(Log.ASSERT, "GENERATE", String.valueOf(generate));

        Fragment fragment = PayFragment.newInstance(new PassengerInfo(tripModel.getTrip_line(),
                fromModel.getSt_name(),
                toModel.getSt_name(),
                CALCULATE_LEAVE_TIME(fromModel.getArrive_time()),
                toModel.getArrive_time(), trainModel.getTrain_class(),
                date,
                numb_seats_passenger,
                total_price,
                String.valueOf(generate)));

        if (getActivity() != null) {
            ((Home) getActivity()).loadFragment(fragment , "Paying");
        }

    }

}