package com.satya.invandmodule.time_picker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.satya.invandmodule.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sivakrishna on 16/10/17.
 */

public class CustomTimePicker extends LinearLayout implements ITimerInterface{
    private Date minDate;
    private Date startDate;
    public int hFinalWidthDate;
    public float hItemWidthDate;
    public float hPaddingDate;
    public float hFirstItemWidthDate;
    public int hAllPixelsDate;
    public boolean isAm = true;
    public int mFinalWidthDate;
    public float mItemWidthDate;
    public float mPaddingDate;
    public float mFirstItemWidthDate;
    public int mAllPixelsDate;
    public ArrayList<LabelerData> hoursData;
    public ArrayList<LabelerData> minutesData;
    public int selectedHr , selectedMin;
    public TimeAdapter hoursAdapter , minutesAdapter;
    public RecyclerView hoursRecyclerview , minitesRecyclerview;
    public Switch amPmSwitch;
    public CompoundButton.OnCheckedChangeListener listener;
    private TimePickerEventListener timePickerEventListener;

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
//        update();

    }

    public void setTimePickerEventListener(TimePickerEventListener timePickerEventListener) {
        this.timePickerEventListener = timePickerEventListener;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void update(){
        scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(startDate.getHours() )-1 ,true,true);
        scrollListToPositionDate(minitesRecyclerview,startDate.getMinutes()-1 ,false,true);
//        calculatePositionAndScrollDate(hoursRecyclerview,true);
//        calculatePositionAndScrollDate(minitesRecyclerview,false);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
//        update();
    }

    public CustomTimePicker(Context context) {
        super(context);
        init(context);
    }

    public CustomTimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(final Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_picker_view_,this);
        hoursRecyclerview = (RecyclerView)findViewById(R.id.hours_recyclerview);
        minitesRecyclerview = (RecyclerView)findViewById(R.id.minutes_recyclerview);
        amPmSwitch = (Switch)findViewById(R.id.switch1);
        startDate = new Date();
        minDate = new Date();
        selectedHr = startDate.getHours();
        selectedMin = startDate.getMinutes();


        isAm = getAmPm(startDate.getHours());
        ViewTreeObserver viewTreeObserver = hoursRecyclerview.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                hoursRecyclerview.getViewTreeObserver().removeOnPreDrawListener(this);
                hFinalWidthDate = hoursRecyclerview.getMeasuredWidth();
                hItemWidthDate = getResources().getDimension(R.dimen.item_dob_width) ;
                hPaddingDate = (hFinalWidthDate - hItemWidthDate) / 2;
                hFirstItemWidthDate = hPaddingDate ;
                hAllPixelsDate = 0;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                hoursRecyclerview.setLayoutManager(linearLayoutManager);
                hoursRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this) {

                            if (newState == recyclerView.SCROLL_STATE_IDLE) {
                                calculatePositionAndScrollDate(recyclerView, true);
//                                calculatePositionAndScrollDate(minitesRecyclerview,false);
                            }
                        }


                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        hAllPixelsDate += dx;
                    }
                });

                if(hoursData == null) {
                    hoursData = new ArrayList<LabelerData>();
                }
                genLabelerDate();
                hoursAdapter = new TimeAdapter(hoursData,(int)hFirstItemWidthDate);
                hoursAdapter.setSelecteditem(0,true);
                hoursAdapter.anInterface = CustomTimePicker.this;
                hoursRecyclerview.setAdapter(hoursAdapter);
                scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(startDate.getHours() )-1 ,true,true);

                return true;
            }
        });

        ViewTreeObserver viewTreeObserver1 = minitesRecyclerview.getViewTreeObserver();
        viewTreeObserver1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                minitesRecyclerview.getViewTreeObserver().removeOnPreDrawListener(this);
                mFinalWidthDate = minitesRecyclerview.getMeasuredWidth();
                mItemWidthDate = getResources().getDimension(R.dimen.item_dob_width) ;
                mPaddingDate = (mFinalWidthDate - mItemWidthDate) / 2;
                mFirstItemWidthDate = mPaddingDate ;
                mAllPixelsDate = 0;

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                minitesRecyclerview.setLayoutManager(linearLayoutManager);

                minitesRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this){
                            if(newState == recyclerView.SCROLL_STATE_IDLE){
                                calculatePositionAndScrollDate(recyclerView,false);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        mAllPixelsDate += dx;
                    }
                });
                if(minutesData == null){
                    minutesData = new ArrayList<LabelerData>();
                }
                getMinutesLabelDates();
                minutesAdapter = new TimeAdapter(minutesData,(int)mFirstItemWidthDate);
                minutesAdapter.anInterface = CustomTimePicker.this;
                minitesRecyclerview.setAdapter(minutesAdapter);
                scrollListToPositionDate(minitesRecyclerview,startDate.getMinutes() - 1 ,false,true);

                return true;

            }
        });

        amPmSwitch.setOnCheckedChangeListener(null);
        amPmSwitch.setChecked(getAmPm(startDate.getHours()));
        listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setChecked(b);
            }
        };
        amPmSwitch.setOnCheckedChangeListener(listener);


    }

    private int get24HoursFormat(boolean isAm , int hours){

        if(isAm){
            if(hours == 12){
                return  0;
            }else {
                return hours;
            }
        }else {
            if(hours == 12){
                return hours;
            }else {
                int a = hours + 12;
                return a;

            }
        }



    }


    private int get12HoursFormat(int hours){
        int a = 0;
        if(12 >= hours){
            if(hours == 0){
                a = 12;
            }else {
                a = hours;
            }
        }else {
            a = hours - 12;
        }
        return a;

//        return hours > 12 ? hours - 12 : hours  ;
    }

    private boolean getAmPm(int hours){
        if(hours < 12){
            return true;
        }else {
            return false;
        }
    }


    private void calculatePositionAndScrollDate(RecyclerView recyclerView,boolean isHours) {
        if(isHours){
            int expectedPositionDate = Math.round((hAllPixelsDate + hPaddingDate - hFirstItemWidthDate) / hItemWidthDate);

            if (expectedPositionDate == -1) {
                expectedPositionDate = 0;
            } else if (expectedPositionDate >= recyclerView.getAdapter().getItemCount() - 2) {
                expectedPositionDate--;
            }
            HscrollListToPositionDate(recyclerView, expectedPositionDate, true);
        }else {
            int expectedPositionDate = Math.round((mAllPixelsDate + mPaddingDate - mFirstItemWidthDate) / mItemWidthDate);

            if (expectedPositionDate == -1) {
                expectedPositionDate = 0;
            } else if (expectedPositionDate >= recyclerView.getAdapter().getItemCount() - 2) {
                expectedPositionDate--;
            }
            HscrollListToPositionDate(recyclerView, expectedPositionDate, false);
        }


    }


    private void HscrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate,boolean isHours) {
        if(isHours){
            float targetScrollPosDate = expectedPositionDate * hItemWidthDate + hFirstItemWidthDate - hPaddingDate;
            float missingPxDate = targetScrollPosDate - hAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }

            setDateValue(true,false);


        }else {
            float targetScrollPosDate = expectedPositionDate * mItemWidthDate + mFirstItemWidthDate - mPaddingDate;
            float missingPxDate = targetScrollPosDate - mAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }

            setDateValue(false,false);



        }

    }


    public void setChecked(boolean isChecked){
        if(startDate.getDate() == minDate.getDate()){
            if(isAm){
                amPmSwitch.setOnCheckedChangeListener(null);
                amPmSwitch.setChecked(isChecked);
                isAm = isChecked;
                amPmSwitch.setOnCheckedChangeListener(listener);
                prepareFinalDate();
//                scrollListToPositionDate(hoursRecyclerview,startDate.getDate(),true,false);
            }else {
                amPmSwitch.setOnCheckedChangeListener(null);
//                amPmSwitch.setChecked(!isChecked);
//                isAm = !isChecked;
                isAm = getAmPm(startDate.getHours());
                amPmSwitch.setChecked(isAm);
                scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(startDate.getHours() -1),true,false);
//                isAm = getAmPm(startDate.getHours());
//                amPmSwitch.setChecked(isAm);
                scrollListToPositionDate(hoursRecyclerview,startDate.getMinutes() -1,false,false);
                amPmSwitch.setOnCheckedChangeListener(listener);
//                prepareFinalDate();
            }
//            startDate = new Date();
//            if(!isChecked){
//                amPmSwitch.setChecked(false);
//
//            }else {
//                amPmSwitch.setChecked(true);
//                scrollListToPositionDate(hoursRecyclerview,startDate.getDate(),true,false);
//            }
        }else {
            amPmSwitch.setChecked(isChecked);
            isAm = isChecked;
            prepareFinalDate();

//            scrollListToPositionDate(hoursRecyclerview,startDate.getDate(),true,false);
        }
    }

    private void scrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate,boolean isHours,boolean isFirstTime) {
        if(isHours){
            float targetScrollPosDate = expectedPositionDate * hItemWidthDate + hFirstItemWidthDate - hPaddingDate;
            float missingPxDate = targetScrollPosDate - hAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            setDateValue(true,isFirstTime);
        }else {
            float targetScrollPosDate = expectedPositionDate * mItemWidthDate + mFirstItemWidthDate - mPaddingDate;
            float missingPxDate = targetScrollPosDate - mAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            setDateValue(false,isFirstTime);
        }

    }


    private void setDateValue(boolean isHours, boolean isFirstTime) {
        if (isHours) {
            int expectedPositionDateColor = Math.round((hAllPixelsDate + hPaddingDate - hFirstItemWidthDate) / hItemWidthDate);
            int setColorDate = expectedPositionDateColor + 1;

            //set color here
//            stHours = get24Hours(setColorDate);
            if(!isFirstTime) {
                checkingScrollingEnable(true, setColorDate);
            }else {
                hoursAdapter.setSelecteditem(setColorDate,true);
                selectedHr = setColorDate;
                prepareFinalDate();
            }
//            if(mcheckEnableScrolling(true)){
//                hoursAdapter.setSelecteditem(setColorDate, true);
//                stHours = expectedPositionDateColor +1 ;
//                prepareDate();
//            }

        } else {
            int expectedPositionDateColor = Math.round((mAllPixelsDate + mPaddingDate - mFirstItemWidthDate) / mItemWidthDate);
            int setColorDate = expectedPositionDateColor + 1;
            //set color here

            if(!isFirstTime) {
                checkingScrollingEnable(false, setColorDate);
            }else {
                minutesAdapter.setSelecteditem(setColorDate, false);
                selectedMin = setColorDate;
                prepareFinalDate();
            }

//            if(mcheckEnableScrolling(false)){
//                stMinutes = setColorDate;
//                minutesAdapter.setSelecteditem(setColorDate, false);
//                stMinutes = expectedPositionDateColor + 1 ;
//                prepareDate();
//            }

        }
    }

    private void checkingScrollingEnable(boolean isHours,int selectedHour){
        if(isHours){

            if(minDate.getDate() == startDate.getDate()){
                if(minDate.getHours() <= get24HoursFormat(amPmSwitch.isChecked(),selectedHour)){
                    hoursAdapter.setSelecteditem(selectedHour, true);
                    selectedHr = selectedHour;
                    prepareFinalDate();

                }else {
                    amPmSwitch.setOnCheckedChangeListener(null);
                    amPmSwitch.setChecked(getAmPm(startDate.getHours()));
                    amPmSwitch.setOnCheckedChangeListener(listener);
                    mScrollListToPositionDate(hoursRecyclerview,get12HoursFormat(startDate.getHours() ) -1,true,false);
                    hoursAdapter.setSelecteditem(get12HoursFormat(startDate.getHours() ) -1, true);
                    selectedHr = get12HoursFormat(startDate.getHours() ) -1;
                }
            }else {
                hoursAdapter.setSelecteditem(selectedHour, true);
                selectedHr = selectedHour;
                prepareFinalDate();
            }



//            if(startDate.getDate() == startDate.getDate()){
//                int i = get24HoursFormat(amPmSwitch.isChecked(),selectedHour);
//                if(minDate.getHours() <= i){
//                    hoursAdapter.setSelecteditem(selectedHour, true);
//                    selectedHr = selectedHour;
//                    prepareFinalDate();
//                }else {
//                    amPmSwitch.setOnCheckedChangeListener(null);
//                    amPmSwitch.setChecked(getAmPm(startDate.getHours()));
//                    amPmSwitch.setOnCheckedChangeListener(listener);
//                    mScrollListToPositionDate(hoursRecyclerview,get12HoursFormat(startDate.getHours() ) -1,true,false);
//
//                }
//            }else if(minDate.before(startDate)){
//                hoursAdapter.setSelecteditem(selectedHour, true);
//                selectedHr = selectedHour;
//                prepareFinalDate();
//            }

        }else {

            if(minDate.getDate() == startDate.getDate()){
                if(minDate.getHours() < get24HoursFormat(amPmSwitch.isChecked(),selectedHr)){
                    minutesAdapter.setSelecteditem(selectedHour, false);
                    selectedMin = selectedHour;
                    prepareFinalDate();
                }else if(minDate.getHours() == get24HoursFormat(amPmSwitch.isChecked(),selectedHr)){
                    if(minDate.getMinutes() <= selectedHour){
                        minutesAdapter.setSelecteditem(selectedHour, false);
                        selectedMin = selectedHour;
                        prepareFinalDate();

                    }else {
                        mScrollListToPositionDate(minitesRecyclerview, minDate.getMinutes() - 1, false, false);
                        minutesAdapter.setSelecteditem(minDate.getMinutes() - 1, false);
                        selectedMin = selectedHour;
                    }
                }
            }else {
                minutesAdapter.setSelecteditem(selectedHour,false);
                selectedMin = selectedHour;
                prepareFinalDate();
            }






//            if(minDate.getDate() == startDate.getDate()) {
//
//                    if (startDate.getHours() <= get24HoursFormat(amPmSwitch.isChecked(), selectedHr)) {
//                        minutesAdapter.setSelecteditem(selectedHour, false);
//                        selectedMin = selectedHour;
//                        prepareFinalDate();
//                    } else {
//                        if (startDate.getMinutes() <= selectedHour) {
//                            minutesAdapter.setSelecteditem(selectedHour, false);
//                            selectedMin = selectedHour;
//                            prepareFinalDate();
//                        } else {
//                            mScrollListToPositionDate(minitesRecyclerview, startDate.getMinutes() - 1, false, false);
//                        }
//                    }
//
//            }else if(minDate.after(startDate)){
//                minutesAdapter.setSelecteditem(selectedHour,false);
//                selectedMin = selectedHour;
//                prepareFinalDate();
//            }
        }


    }

    private void mScrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate,boolean isHours,boolean isFirstTime){
        if(isHours){
            float targetScrollPosDate = expectedPositionDate * hItemWidthDate + hFirstItemWidthDate - hPaddingDate;
            float missingPxDate = targetScrollPosDate - hAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
//            setDateValue(true);
        }else {
            float targetScrollPosDate = expectedPositionDate * mItemWidthDate + mFirstItemWidthDate - mPaddingDate;
            float missingPxDate = targetScrollPosDate - mAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
//            setDateValue(false);
        }
    }

    private void prepareFinalDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY,get24HoursFormat(amPmSwitch.isChecked(),selectedHr));
        calendar.set(Calendar.MINUTE,selectedMin);
        calendar.set(Calendar.SECOND,0);
//        Date finalDate = null;
//        finalDate = startDate;
//        finalDate.setHours(get24HoursFormat(amPmSwitch.isChecked(),selectedHr));
//        finalDate.setMinutes(selectedMin);
//        finalDate.setSeconds(0);
        if(timePickerEventListener != null){
            timePickerEventListener.selectedDate(calendar.getTime());
        }
        Log.d("Final date ",calendar.getTime().toString());
    }

    public interface TimePickerEventListener{
        void selectedDate(Date date);
    }

    private void genLabelerDate() {
        for (int i = 0; i < 14; i++) {
            LabelerData labelerDate = new LabelerData();
            labelerDate.setNumber(Integer.toString(i));
            hoursData.add(labelerDate);

            if (i == 0 || i == 13) {
                labelerDate.setType(TimeAdapter.VIEW_TYPE_PADDING);
            } else {
                labelerDate.setType(TimeAdapter.VIEW_TYPE_ITEM);
            }
        }
    }

    private void getMinutesLabelDates(){
        for (int i = -1; i < 61; i++) {
            LabelerData labelerDate = new LabelerData();
            labelerDate.setNumber(Integer.toString(i));
            minutesData.add(labelerDate);

            if (i == -1 || i == 60) {
                labelerDate.setType(TimeAdapter.VIEW_TYPE_PADDING);
            } else {
                labelerDate.setType(TimeAdapter.VIEW_TYPE_ITEM);
            }
        }
    }

    @Override
    public void itemSelectedInAdapter(int pos, boolean isHours) {

    }
}
