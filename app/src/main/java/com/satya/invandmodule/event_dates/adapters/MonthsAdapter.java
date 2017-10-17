package com.satya.invandmodule.event_dates.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.satya.invandmodule.R;
import com.satya.invandmodule.event_dates.DayAdapterListenerInterface;
import com.satya.invandmodule.event_dates.Month;

import java.util.ArrayList;


public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.ViewHolder> {
    public ArrayList<Month> calendars = new ArrayList<>();
    public ArrayList<DayAdapter> adaptersList=new ArrayList<>();
    public Context context;
    public MonthAdapterListnerInterface monthAdapterListnerInterface;

    public MonthsAdapter(ArrayList<Month> calendars, Context context) {
        this.calendars.clear();
        this.calendars.addAll(calendars);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.month_view,parent,false);
        return new ViewHolder(view);
    }
    public void setMonths(ArrayList<Month> calendars) {
        this.calendars.clear();
        this.calendars.addAll(calendars);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(adaptersList.size()<=position){
            DayAdapter dayAdapter=new DayAdapter(context,calendars.get(position).getMonthDays(), calendars.get(position).getCuurentCalander());
            adaptersList.add(dayAdapter);
            dayAdapter.dayAdapterListnerInterface=(DayAdapterListenerInterface) monthAdapterListnerInterface;

        }

        holder.monthGridView.setAdapter(adaptersList.get(position));


    }


    @Override
    public int getItemCount() {
        return calendars.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public GridView monthGridView;
        public ViewHolder(View itemView) {
            super(itemView);
            monthGridView = (GridView)itemView.findViewById(R.id.month_grid_view);
        }
    }
}
