package com.example.e7gzly.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e7gzly.R;
public class MyTicketFragment extends Fragment {


      @Override
      public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_my_ticket , container , false);
      }

}
