package com.example.e7gzly.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.activity.Home;
import com.example.e7gzly.model.PassengerInfo;
import com.example.e7gzly.model.TicketModel;
import com.example.e7gzly.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingFragment extends Fragment {

    private static final String FROM_KAY = "from";
    private static final String FROM_ID_KAY = "from_id";
    private static final String TO_KAY = "to";
    private static final String TO_ID_KAY = "to_id";
    private static final String ARRIVE_KAY = "arrive";
    private static final String LEAVE_KAY = "leave";
    private static final String TRAIN_CLASS_KAY = "class";
    private static final String TRAIN_LINE_KAY = "line";
    private static final String SEATS_KAY = "seats";

    private String from;
    private String from_id;
    private String to;
    private String to_id;
    private String arrive;
    private String leave;
    private String train_class;
    private String train_line;
    private String getDate;
    private String get_seats;
    private int seats;
    private int remaining_seats;
    private int passenger_seat;
    private double price;

    private TextView line;
    private TextView tv_class;
    private TextView tv_from;
    private TextView tv_to;
    private TextView tv_leave;
    private TextView tv_arrive;
    private TextView tv_price;
    private TextView date_picker;
    private Button btn_booking;
    private EditText seats_booked;

    private ArrayList<TicketModel> arrayList = new ArrayList<>();
    private TicketModel ticketModel;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private DatabaseReference databaseReference;

    public static BookingFragment newInstance(String from, String from_id, String to, String to_id, String arrive, String leave, String train_class, String train_line, int seats) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString(FROM_KAY, from);
        args.putString(FROM_ID_KAY, from_id);
        args.putString(TO_KAY, to);
        args.putString(TO_ID_KAY, to_id);
        args.putString(ARRIVE_KAY, arrive);
        args.putString(LEAVE_KAY, leave);
        args.putString(TRAIN_CLASS_KAY, train_class);
        args.putString(TRAIN_LINE_KAY, train_line);
        args.putInt(SEATS_KAY, seats);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        from = getArguments().getString(FROM_KAY);
        from_id = getArguments().getString(FROM_ID_KAY);
        to = getArguments().getString(TO_KAY);
        to_id = getArguments().getString(TO_ID_KAY);
        arrive = getArguments().getString(ARRIVE_KAY);
        leave = getArguments().getString(LEAVE_KAY);
        train_class = getArguments().getString(TRAIN_CLASS_KAY);
        train_line = getArguments().getString(TRAIN_LINE_KAY);
        seats = getArguments().getInt(SEATS_KAY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        ((Home) getActivity()).setActionBarTitle("Booking");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        line = view.findViewById(R.id.line);
        tv_class = view.findViewById(R.id.tv_class);
        tv_from = view.findViewById(R.id.from);
        tv_to = view.findViewById(R.id.to);
        tv_leave = view.findViewById(R.id.leave);
        tv_arrive = view.findViewById(R.id.arrive);
        tv_price = view.findViewById(R.id.tv_price);
        date_picker = view.findViewById(R.id.date_picker);
        btn_booking = view.findViewById(R.id.btn_booking);
        seats_booked = view.findViewById(R.id.seats_booked);

        line.setText("Line direction : " + train_line);
        tv_class.setText("Train class : " + train_class);
        tv_from.setText("From : " + from);
        tv_to.setText("To : " + to);
        tv_arrive.setText("Arrive at : " + arrive);
        tv_leave.setText("Leave at : " + leave);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
//                Log.println(Log.ASSERT , "SEATS" , String.valueOf(seats));
            }
        });

        getPrice();

        return view;
    }

    private void getPrice() {

        final ArrayList<TicketModel> list = new ArrayList<>();

        databaseReference.child(Constants.TICKET_PRICES).child(train_class).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(TicketModel.class));
                }

                for (TicketModel ticket : list) {

                    if (from_id.equalsIgnoreCase(ticket.getStation_1()) && to_id.equalsIgnoreCase(ticket.getStation_2())) {

                        Log.println(Log.ASSERT, "PRICE", String.valueOf(ticket.getPrice()));
                        Log.println(Log.ASSERT, "From", String.valueOf(ticket.getStation_1()));
                        Log.println(Log.ASSERT, "TO", String.valueOf(ticket.getStation_2()));

                        price = ticket.getPrice();
                        tv_price.setText(price + " L.E");

                    } else if (from_id.equalsIgnoreCase(ticket.getStation_2()) && to_id.equalsIgnoreCase(ticket.getStation_1())) {

                        Log.println(Log.ASSERT, "PRICE", String.valueOf(ticket.getPrice()));
                        Log.println(Log.ASSERT, "From", String.valueOf(ticket.getStation_1()));
                        Log.println(Log.ASSERT, "TO", String.valueOf(ticket.getStation_2()));

                        tv_price.setText(ticket.getPrice() + " L.E");

                    }

                }

//                Log.println(Log.ASSERT, "Array", String.valueOf(arrayList.size()));

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
                date_picker.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Disable Past Date
        datePickerDialog.show();

    }

    private void booking() {

        getDate = date_picker.getText().toString();
        get_seats = seats_booked.getText().toString();

        if (TextUtils.isEmpty(getDate)) {
            Toast.makeText(getContext(), "Please Choose The Date", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(get_seats)) {
            Toast.makeText(getContext(), "Please Enter Number Of Seats", Toast.LENGTH_LONG).show();
        } else {

            passenger_seat = Integer.parseInt(get_seats);

            databaseReference.child("Test").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.hasChild(getDate)) {

                        remaining_seats = seats - passenger_seat;
                        double total_price = price * passenger_seat;

                        PassengerInfo info = new PassengerInfo(train_line, from, to, leave, arrive,
                                train_class, getDate, passenger_seat, total_price);

                        databaseReference.child("Test").child(getDate).child(train_line)
                                .child(Constants.getUID()).child(train_line).setValue(info);

                        databaseReference.child("Test").child(getDate).child(train_line).child("seats").setValue(remaining_seats);

                    } else {

                        databaseReference.child("Test").child(getDate)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.hasChild(train_line)) {

                                            remaining_seats = seats - passenger_seat;
                                            int total_price = (int) (price * passenger_seat);

                                            PassengerInfo info = new PassengerInfo(train_line, from, to, leave, arrive,
                                                    train_class, getDate, passenger_seat, total_price);

                                            databaseReference.child("Test").child(getDate).child(train_line)
                                                    .child(Constants.getUID()).child(train_line).setValue(info);

                                            databaseReference.child("Test").child(getDate).child(train_line).child("seats").setValue(remaining_seats);

                                        } else {

                                            databaseReference.child("Test").child(getDate).child(train_line).orderByChild("seats")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            remaining_seats = snapshot.getValue(Integer.class);

                                                            if (remaining_seats == 0) {
                                                                Toast.makeText(getContext(), "The all train is busy please try search about another train", Toast.LENGTH_LONG).show();
                                                            } else {

                                                                int remaining = remaining_seats - passenger_seat;

//                                                                int total_price = (int) (price * passenger_seat);
//
//                                                                PassengerInfo info = new PassengerInfo(train_line, from, to, leave, arrive,
//                                                                        train_class, getDate, passenger_seat, total_price);
//
//                                                                databaseReference.child("Test").child(getDate).child(train_line)
//                                                                        .child(Constants.getUID()).child(train_line).setValue(info);
//                                                                databaseReference.child("Test").child(getDate).child(train_line).child("seats").setValue(remaining);
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

}
