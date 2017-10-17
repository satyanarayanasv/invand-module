package com.satya.invandmodule.event_dates.custom_;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.satya.invandmodule.R;
import com.satya.invandmodule.event_dates.Day;
import com.satya.invandmodule.event_dates.DayAdapterListenerInterface;
import com.satya.invandmodule.event_dates.ICalendarView;
import com.satya.invandmodule.event_dates.adapters.MonthAdapterListnerInterface;
import com.satya.invandmodule.event_dates.adapters.MonthsAdapter;
import com.satya.invandmodule.event_dates.presenter.CalanderPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class CustomCalendar extends LinearLayout implements ICalendarView, DayAdapterListenerInterface, MonthAdapterListnerInterface {
    public RecyclerView recyclerView;
    public TextView month_name;
    public CalanderPresenter calanderPresenter;
    public String dateFormat = "MMMM";
    public int noOfMonths = 12;
    public Date startDate , endDate;
    public Context context;
    public EventHandler eventHandler=null;
    public MonthsAdapter monthsAdapter;
    public boolean isStartDate = true;
    public Calendar currentDate = Calendar.getInstance();
    public ArrayList<Calendar> calendars = new ArrayList<>();
    public CustomCalendar(Context context) {
        super(context);
        this.context = context;
    }

    public void setNoOfMonths(int noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initControl(context,attrs);
        invalidate();
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initControl(context,attrs);
        invalidate();
    }

    public void initControl(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_date_view,this);
        recyclerView = (RecyclerView) findViewById(R.id.months_recyclerview);
        month_name = (TextView)findViewById(R.id.month_name);
        loadAtributes(context,attributeSet);
        prepareCalender();
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        calanderPresenter =new CalanderPresenter();
        calanderPresenter.setNoOfMonths(noOfMonths);

        calanderPresenter.setStartDate(startDate);
//        calanderPresenter.setEndDate(endDate);
        calanderPresenter.prepareCalenderMonths();
        calanderPresenter.setView(this);
        monthsAdapter=new MonthsAdapter(calanderPresenter.getMonths(),context);
        recyclerView.setAdapter(monthsAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        monthsAdapter.monthAdapterListnerInterface=this;
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
       }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int ab = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(ab != -1){
                    month_name.setText(calanderPresenter.getMonths().get(ab).getMonthName());
                }


            }
        });


    }
    public void setCurrentDate(Date date) {
        calanderPresenter.setStartDate(startDate);
        calanderPresenter.prepareCalenderMonths();
        monthsAdapter.setMonths(calanderPresenter.getMonths());
        monthsAdapter.notifyDataSetChanged();
    }


    public void loadAtributes(Context context, AttributeSet attributeSet) {
        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomCalendar, 0, 0);
        try {
            // try to load provided date format, and fallback to default otherwise
            if(noOfMonths==0){
                noOfMonths = ta.getInt(R.styleable.CustomCalendar_no_of_months, 12);
            }
        } finally {
            ta.recycle();
        }
    }
    public void prepareCalender(){
        for (int i = 0; i < noOfMonths; i++) {
            currentDate = Calendar.getInstance();
            currentDate.add(Calendar.MONTH, i);
            calendars.add(currentDate);
        }
    }

    @Override
    public void setMessage(String paramString) {

    }

    @Override
    public void updateAll(Date paramDate1, Date paramDate2, boolean paramBoolean) {

    }

    @Override
    public void updateDeparture(Date paramDate) {

        eventHandler.onStartDate(paramDate);
        monthsAdapter.notifyDataSetChanged();

    }

    @Override
    public void dayClicked(Day day) {
        if(calanderPresenter != null){
            calanderPresenter.dayClicked(day,isStartDate);
        }

    }

    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    public interface EventHandler
    {
        void onStartDate(Date startDate);
        void onEndDate(Date endDate);
    }
}
