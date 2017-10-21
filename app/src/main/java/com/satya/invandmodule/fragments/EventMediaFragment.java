package com.satya.invandmodule.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satya.invandmodule.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMediaFragment extends Fragment {


    public EventMediaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_media, container, false);




        return view;
    }

}
