package com.satya.invandmodule.event_dates.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.satya.invandmodule.R;
import com.satya.invandmodule.event_dates.Day;
import com.satya.invandmodule.event_dates.DayAdapterListenerInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sivakrishna on 10/10/17.
 */

public class DayAdapter extends ArrayAdapter<Date> {

    public Context context;
    public ArrayList<Day> dates;
    private LayoutInflater inflater;
    private Date currentCalendar;
    public DayAdapterListenerInterface dayAdapterListnerInterface;
    private Date today =new Date();
    public DayAdapter(@NonNull Context context, ArrayList<Day> days, Date currentCalendar) {
        super(context, R.layout.day_view);
        inflater= LayoutInflater.from(context);
        this.context=context;
        this.dates=days;
        this.currentCalendar = currentCalendar;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        today = calendar.getTime();
    }
    public void setDates(ArrayList<Day> days) {
        this.dates.clear();
        this.dates.addAll(days);
    }
    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Date getItem(int position) {
        return dates.get(position).getDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Date date=getItem(position);
        final Day day=dates.get(position);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.day_view, parent, false);
        final TextView txt = (TextView) convertView.findViewById(R.id.txt);
        final TextView value = (TextView) convertView.findViewById(R.id.value);
        convertView.setTag(position);

        if(currentCalendar.getMonth()==date.getMonth()){
            if(today.getDate()==date.getDate() && today.getMonth()== date.getMonth() && today.getYear()==date.getYear()){

                txt.setTextColor(context.getResources().getColor(R.color.colorAccent));
                txt.setElevation(0);
            }
            else
            if (date.before(today)) {
                txt.setElevation(0);
                txt.setTextColor(context.getResources().getColor(R.color.ligth_white));
            }
            txt.setText(String.valueOf(date.getDate()));
        }
        else {

        }
        if(day.isSelected()){
            if (txt.getText().toString().length() > 0 ) {
                txt.setTextColor(context.getResources().getColor(R.color.white));
                txt.setElevation(5);
                convertView.setBackground(context.getResources().getDrawable(R.drawable.circle_backgroung));
//                convertView.setBackgroundColor(context.getResources().getColor(R.color.selected_date_color));
            }
        }
        if(day.isInRange()){
            if (txt.getText().toString().length() > 0 ) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.in_range_color));
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt.getText().length() > 0){
                    int position = (Integer) v.getTag();
                    dayAdapterListnerInterface.dayClicked( dates.get(position));
                }

            }
        });





        return convertView;
    }
}
