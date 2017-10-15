package com.satya.invandmodule.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.satya.invandmodule.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTitleFragment extends Fragment {
    public EditText event_title;


    public EventTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_title, container, false);
        event_title = (EditText) view.findViewById(R.id.event_title_edittext);







        return view;
    }
    public void showMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }

}
