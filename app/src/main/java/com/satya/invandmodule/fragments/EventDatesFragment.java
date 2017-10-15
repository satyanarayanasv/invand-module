package com.satya.invandmodule.fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satya.invandmodule.R;
import com.satya.invandmodule.event_dates.custom_.CustomCalendar;
import com.satya.invandmodule.time_picker.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDatesFragment extends Fragment {
    public CustomCalendar customCalendar;
    public TimePicker timePicker;
    public TextView startDateText , endDateText;
    public TextView startDateTime , endDateTime , addEndDateText , pickEnddate;
    public ImageView addEndDate , clearEndDate;
    public ConstraintLayout startDateContainer , endDateContainer;
    public boolean isEndContainerAdded = false;
    public Date startDate  = null, endDate = null;

    public boolean isStartDateSelected = true;
    private final long DEFAULT_MIN_END_DATE_DIFFERENCE = 60 * 60 * 1000;    // 1 Hour
    private final long DEFAULT_MIN_START_DATE_DIFFERENCE = 15 * 60 * 1000;  // 10 minutes
    public EventDatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_dates, container, false);
        customCalendar = (CustomCalendar)view.findViewById(R.id.custom_calendar);
        timePicker = (TimePicker)view.findViewById(R.id.time_picker);
        startDateText = (TextView)view.findViewById(R.id.start_date);
        endDateText = (TextView)view.findViewById(R.id.end_date);
        startDateTime = (TextView)view.findViewById(R.id.start_date_time);
        endDateTime = (TextView)view.findViewById(R.id.end_date_time);
        addEndDateText = (TextView)view.findViewById(R.id.add_end_date_text);
        pickEnddate = (TextView)view.findViewById(R.id.pick_end_date);
        addEndDate = (ImageView)view.findViewById(R.id.add_end_date);
        clearEndDate = (ImageView)view.findViewById(R.id.clear_end_date);
        startDateContainer = (ConstraintLayout)view.findViewById(R.id.start_date_container);
        endDateContainer = (ConstraintLayout)view.findViewById(R.id.end_date_container);

        startDate = new Date(new Date().getTime() + DEFAULT_MIN_START_DATE_DIFFERENCE);
        endDate = null;

        customCalendar.startDate = startDate;
        timePicker.startDate = startDate;
        setDates();
        startDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDateSelected = true;
//                customCalendar.setCurrentDate(startDate);
                timePicker.startSelecteddate(startDate);
                startDateText.setTextColor(getContext().getResources().getColor(R.color.container_active));
                startDateTime.setTextColor(getContext().getResources().getColor(R.color.container_active));

                endDateText.setTextColor(getContext().getResources().getColor(R.color.container_not_active));
                endDateTime.setTextColor(getContext().getResources().getColor(R.color.container_not_active));

            }
        });
        endDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endDate == null)
                {
                    endDate = new Date(startDate.getTime() +  DEFAULT_MIN_END_DATE_DIFFERENCE) ;

                }
//                customCalendar.setCurrentDate(endDate);
                timePicker.startSelecteddate(endDate);
                isStartDateSelected = false;
//                endDate = startDate;
//                endDate.setHours(startDate.getHours() + 1);

                endDateText.setTextColor(getContext().getResources().getColor(R.color.container_active));
                endDateTime.setTextColor(getContext().getResources().getColor(R.color.container_active));

                startDateText.setTextColor(getContext().getResources().getColor(R.color.container_not_active));
                startDateTime.setTextColor(getContext().getResources().getColor(R.color.container_not_active));

            }
        });
        addEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEndDate();

            }
        });
        clearEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEndDate();

            }
        });
        customCalendar.setEventHandler(new CustomCalendar.EventHandler() {
            @Override
            public void onStartDate(Date startdate) {
                timePicker.startSelecteddate(startdate);

            }
            @Override
            public void onEndDate(Date endDate) {

            }
        });

        timePicker.setTimePickerEvent(new TimePicker.TimePickerEvent() {
            @Override
            public void startDate(Date date) {
                if (isStartDateSelected) {
                    startDate = date;
                }
                else {
                    endDate = date;
                }
//                startDate = date;
//                date.setHours(startDate.getHours() + 1);
//                endDate = startDate;
                updateUIDates();

            }
        });
        return view;
    }

    public void setDates(){

        int temp =  startDate.getMinutes();
//        startDate.setMinutes( temp + 10);
        updateUIDates();

    }
    public void updateUIDates(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        if(isStartDateSelected){
            startDateText.setText(dateFormat.format(startDate));
            startDateTime.setText(timeFormat.format(startDate));
        }else {
            if (endDate != null) {
                endDateText.setText(dateFormat.format(endDate));
                endDateTime.setText(timeFormat.format(endDate));

            }
        }
    }


    public void addEndDate(){
        isEndContainerAdded = true;
        endDate = new Date(startDate.getTime() +  DEFAULT_MIN_END_DATE_DIFFERENCE) ;
        customCalendar.startDate = startDate;
        addEndDate.setVisibility(View.GONE);
        addEndDateText.setVisibility(View.GONE);
        pickEnddate.setVisibility(View.VISIBLE);
        clearEndDate.setVisibility(View.VISIBLE);
        endDateTime.setVisibility(View.VISIBLE);
        endDateText.setVisibility(View.VISIBLE);


    }
    public void clearEndDate(){
        isEndContainerAdded = false;
        endDate = null;
        addEndDate.setVisibility(View.VISIBLE);
        addEndDateText.setVisibility(View.VISIBLE);
        pickEnddate.setVisibility(View.GONE);
        clearEndDate.setVisibility(View.GONE);
        endDateTime.setVisibility(View.GONE);
        endDateText.setVisibility(View.GONE);
    }

}
