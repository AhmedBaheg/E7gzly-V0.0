package com.example.e7gzly.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzly.R;
import com.example.e7gzly.adapters.TicketAdapter;
import com.example.e7gzly.model.PassengerInfo;
import com.example.e7gzly.utilities.Constants;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyTicketFragment extends Fragment {

      private RecyclerView ticket_recycler;
      private TicketAdapter adapter;
      private FirebaseRecyclerOptions<PassengerInfo> options;

      private DatabaseReference databaseReference;

      @Override
      public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_my_ticket , container , false);

            databaseReference = FirebaseDatabase.getInstance().getReference("Ticket_Info").child(Constants.getUID());
            databaseReference.keepSynced(true);

            ticket_recycler = view.findViewById(R.id.ticket_recycler);
            ticket_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            ticket_recycler.setHasFixedSize(true);

            displayRecyclerView();

            return view;
      }

      private void displayRecyclerView(){

            options = new FirebaseRecyclerOptions.Builder<PassengerInfo>()
                    .setQuery(databaseReference , PassengerInfo.class)
                    .build();

            adapter = new TicketAdapter(options);

            ticket_recycler.setAdapter(adapter);

      }

      @Override
      public void onStart() {
            super.onStart();
            adapter.startListening();
      }

      @Override
      public void onStop() {
            super.onStop();
            adapter.stopListening();
      }
}
