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
import com.satya.invandmodule.event_dates.presenter.CalanderPresenter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sivakrishna on 11/10/17.
 */

public class TimePicker extends LinearLayout implements ITimerInterface{
    private RecyclerView hoursRecyclerview , minutesRecyclerview;
    public float firstItemWidthDate;
    public float mFirstItemWidthDate;
    public float paddingDate;
    public float itemWidthDate;
    public int allPixelsDate;
    public int finalWidthDate;
    public float mPaddingDate;
    public float mItemWidthDate;
    public int mAllPixelsDate;
    public int mFinalWidthDate;
    public CalanderPresenter cp;
    public Date finalStartDate;
//    public int selectedHr,selectedMin;
    public Switch amPmSwitch;
    public int presentHr, presentMin;
    public int stHours , stMinutes ;
    public boolean stIsAm;
    public int prHours , prMinutes;
    public TimePickerEvent event;
    public boolean prIsAm;

    private ArrayList<LabelerData> hoursData = new ArrayList<>();
    private ArrayList<LabelerData> minutesData  = new ArrayList<>();

    public Date presentDate , startDate , endDate;
//    public int minuts , hours ;

//    public boolean isAm;
    private TimeAdapter hoursAdapter,minutesAdapter;
    public TimePicker(Context context) {
        super(context);
        initViews(context);
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }



 // true = am , false = pm;
    public boolean isAm(int hours){
        if(hours < 12){
            return true;
        }else {
            return false;
        }

    }

    public void initViews(final Context context) {
        presentDate = new Date() ;
        startDate = new Date();
        int temp = startDate.getMinutes();
//        startDate.setMinutes(temp + 10);
//
        prHours = presentDate.getHours();
        prMinutes = presentDate.getMinutes();
        stHours = startDate.getHours();
        stMinutes = startDate.getMinutes();
        prIsAm = isAm(prHours);
        stIsAm = isAm(stHours);
//        minuts = presentDate.getMinutes();
//        hours = presentDate.getHours();
//        isAm =  giveAmPm(hours);
        presentMin = presentDate.getMinutes();
        presentHr = presentDate.getHours();
//        Log.d("hours",String.valueOf(hours));
//        Log.d("minuts",String.valueOf(minuts));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_picker_view_, this);
        hoursRecyclerview = (RecyclerView) findViewById(R.id.hours_recyclerview);
        minutesRecyclerview = (RecyclerView) findViewById(R.id.minutes_recyclerview);
        amPmSwitch = (Switch)findViewById(R.id.switch1);
        amPmSwitch.setChecked(stIsAm);
        ViewTreeObserver vtoDate = hoursRecyclerview.getViewTreeObserver();
        vtoDate.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                hoursRecyclerview.getViewTreeObserver().removeOnPreDrawListener(this);
                finalWidthDate = hoursRecyclerview.getMeasuredWidth();
                itemWidthDate = getResources().getDimension(R.dimen.item_dob_width) ;
                paddingDate = (finalWidthDate - itemWidthDate) / 2;
                firstItemWidthDate = paddingDate ;
                allPixelsDate = 0;

                final LinearLayoutManager dateLayoutManager = new LinearLayoutManager(context);
                dateLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                hoursRecyclerview.setLayoutManager(dateLayoutManager);
                hoursRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this) {
//                            calculatePositionAndScrollDate(recyclerView);
//                            if(newState == RecyclerView.SCROLL_AXIS_HORIZONTAL){
//                                calculatePositionAndScrollDate(recyclerView);
//                            }
                            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                                calculatePositionAndScrollDate(recyclerView,true);
                            }
                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                        allPixelsDate += dx;
                        Log.d("dx value", String.valueOf(dx));


                    }
                });
                if (hoursData == null)
                    hoursData = new ArrayList<>();
                genLabelerDate();
                hoursAdapter = new TimeAdapter(hoursData, (int) firstItemWidthDate);
                hoursAdapter.setSelecteditem(0,true);
                hoursAdapter.anInterface = TimePicker.this;
                hoursRecyclerview.setAdapter(hoursAdapter);
                scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(stHours)-1,true);

                return true;
            }
        });

        ViewTreeObserver viewTreeObserver = minutesRecyclerview.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                minutesRecyclerview.getViewTreeObserver().removeOnPreDrawListener(this);
                mFinalWidthDate = minutesRecyclerview.getMeasuredWidth();
                mItemWidthDate = getResources().getDimension(R.dimen.item_dob_width) ;
                mPaddingDate = (mFinalWidthDate - mItemWidthDate) / 2;
                mFirstItemWidthDate = mPaddingDate ;
                mAllPixelsDate = 0;

                final LinearLayoutManager dateLayoutManager = new LinearLayoutManager(context);
                dateLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                minutesRecyclerview.setLayoutManager(dateLayoutManager);

                minutesRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this) {
//                            calculatePositionAndScrollDate(recyclerView);
//                            if(newState == RecyclerView.SCROLL_AXIS_HORIZONTAL){
//                                calculatePositionAndScrollDate(recyclerView);
//                            }
                            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                                calculatePositionAndScrollDate(recyclerView,false);
                            }
                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        mAllPixelsDate += dx;
                        Log.d("miutes dx value", String.valueOf(dx));

                    }
                });
                if (minutesData == null)
                    minutesData = new ArrayList<>();
                getMinutesLabelDates();

                minutesAdapter = new TimeAdapter(minutesData, (int) mFirstItemWidthDate);
                minutesAdapter.setSelecteditem(0,false);
                minutesAdapter.anInterface = TimePicker.this;
                minutesRecyclerview.setAdapter(minutesAdapter);
                scrollListToPositionDate(minutesRecyclerview,stMinutes-1,false);

                return true;
            }
        });


        amPmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(compareDates(presentDate,startDate) == 0){

                        amPmSwitch.setChecked(isChecked);
                        stIsAm = isChecked;
//                        isAm = !isChecked;
                        scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(stHours) - 1,true);
                        scrollListToPositionDate(minutesRecyclerview,stMinutes - 1,false);
//                        mcheckEnableScrolling(true);
//                        mcheckEnableScrolling(false);
//                        prepareDate();
//                        checkEnableScrolling(true);
//                        checkEnableScrolling(false);
                    }
                }




        });


    }
    private void setTimes(){

    }

    public int  get12HoursFormat(int hours){
        if(hours < 12){
            if(hours == 00){
                hours = 12;
                return hours;
            }
            return hours;
        }else {
            if(hours == 12){
                return hours;
            }
            return hours - 12;
        }
    }
    public int get12Hours(boolean isAm,int hours){
        int a = 0;
        if(isAm){
            if(hours == 12){
                a = 00;
            }else {
                a  = hours;
            }
        }else {
            if(hours == 12){
                a = hours;
            }else {
                a = hours - 12;
            }
        }
        Log.d("a value", String.valueOf(a));

        return a;

    }
    public int get24Hours(boolean isAm,int hours){
        int a = 0;
        if(isAm){
            if(hours == 12){
                a = 00;
            }else {
                a  = hours;
            }
        }else {
            if(hours == 12){
                a = hours;
            }else {
                a = hours + 12;
            }
        }
        Log.d("a value", String.valueOf(a));

        return a;

    }

    public int getHours(int hours){
        int a = 0;
        if(stIsAm){
            if(hours == 12){
                a = 00;
            }else {
                a  = hours;
            }
        }else {
            if(hours == 12){
                a = hours;
            }else {
                a = a+12;
            }
        }
        Log.d("a value", String.valueOf(a));

        return a;

    }


   public boolean giveAmPm(int hours){
       if(hours < 12){
           return true;
       }else return false;


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

    private void calculatePositionAndScrollDate(RecyclerView recyclerView,boolean isHours) {
        if(isHours){
            int expectedPositionDate = Math.round((allPixelsDate + paddingDate - firstItemWidthDate) / itemWidthDate);

            if (expectedPositionDate == -1) {
                expectedPositionDate = 0;
            } else if (expectedPositionDate >= recyclerView.getAdapter().getItemCount() - 2) {
                expectedPositionDate--;
            }
            scrollListToPositionDate(recyclerView, expectedPositionDate, true);
        }else {
            int expectedPositionDate = Math.round((mAllPixelsDate + mPaddingDate - mFirstItemWidthDate) / mItemWidthDate);

            if (expectedPositionDate == -1) {
                expectedPositionDate = 0;
            } else if (expectedPositionDate >= recyclerView.getAdapter().getItemCount() - 2) {
                expectedPositionDate--;
            }
            scrollListToPositionDate(recyclerView, expectedPositionDate, false);
        }


    }

    private void scrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate,boolean isHours) {
        if(isHours){
            float targetScrollPosDate = expectedPositionDate * itemWidthDate + firstItemWidthDate - paddingDate;
            float missingPxDate = targetScrollPosDate - allPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            setDateValue(true);
        }else {
            float targetScrollPosDate = expectedPositionDate * mItemWidthDate + mFirstItemWidthDate - mPaddingDate;
            float missingPxDate = targetScrollPosDate - mAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            setDateValue(false);
        }

    }
    private void setDateValue(boolean isHours) {
        if(isHours){
            int expectedPositionDateColor = Math.round((allPixelsDate + paddingDate - firstItemWidthDate) / itemWidthDate);
            int setColorDate = expectedPositionDateColor + 1;
            //set color here
            stHours = get24Hours(amPmSwitch.isChecked(),expectedPositionDateColor +1);
            if(mcheckEnableScrolling(true)){
                hoursAdapter.setSelecteditem(setColorDate, true);
                stHours = expectedPositionDateColor +1 ;
                prepareDate();
            }

        }else {
            int expectedPositionDateColor = Math.round((mAllPixelsDate + mPaddingDate - mFirstItemWidthDate) / mItemWidthDate);
            int setColorDate = expectedPositionDateColor + 1;
            //set color here
            stMinutes = setColorDate;
            if(mcheckEnableScrolling(false)){
                minutesAdapter.setSelecteditem(setColorDate, false);
                stMinutes = expectedPositionDateColor + 1 ;
                prepareDate();
            }

        }

    }



    private void mscrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate,boolean isHours) {
        if(isHours){
            float targetScrollPosDate = expectedPositionDate * itemWidthDate + firstItemWidthDate - paddingDate;
            float missingPxDate = targetScrollPosDate - allPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            hoursAdapter.setSelecteditem(getHours(stHours), true);
        }else {
            float targetScrollPosDate = expectedPositionDate * mItemWidthDate + mFirstItemWidthDate - mPaddingDate;
            float missingPxDate = targetScrollPosDate - mAllPixelsDate;
            if (missingPxDate != 0) {
                recyclerView.smoothScrollBy((int) missingPxDate, 0);
            }
            minutesAdapter.setSelecteditem(stMinutes, false);
        }

    }

    private boolean mcheckEnableScrolling(boolean isHours) {
        if (compareDates(presentDate, startDate) == 0) {
            if (!isHours) {
                if (prHours == stHours && stMinutes + 1 < prMinutes) {
                    mscrollListToPositionDate(minutesRecyclerview, prMinutes - 1, false);
                    amPmSwitch.setChecked(prIsAm);
                    return false;
                }
                return true;
            } else {
                if (prHours > stHours) {
                    mscrollListToPositionDate(hoursRecyclerview, get12HoursFormat(prHours) - 1, true);
                    amPmSwitch.setChecked(prIsAm);
                    return false;
                }
                return true;
            }

        }
        return true;
    }
//    private boolean checkEnableScrolling(boolean isHours) {
//        if (compareDates(presentDate, startDate) == 0) {
//            if (!isHours) {
//                if (hours >= getHours(selectedHr) && selectedMinuts + 1 < minuts) {
//                    mscrollListToPositionDate(minutesRecyclerview, minuts - 1, false);
//
//                    return false;
//                }
//                return true;
//            } else {
//                if (hours > getHours(selectedHours)) {
//                    mscrollListToPositionDate(hoursRecyclerview, getHours(hours) - 1, true);
//                    amPmSwitch.setChecked(prIsAm);
//                    return false;
//                }
//                return true;
//            }
//
//        }
//        return true;
//    }

    private void checkDate(Date presentDate , Date startDate){
        int a = compareDates(startDate , presentDate);
        if(a == 0){
            if(stHours > prHours){
                prepareHoursMinuts(startDate);

            }else {
                startDate = new Date();
                int temp = startDate.getMinutes();
//                startDate.setMinutes(temp + 10);
                stHours = startDate.getHours();
                stMinutes = startDate.getMinutes();
                prepareHoursMinuts(startDate);
            }

//
        }else if(a == 1){
            startDate.setHours(stHours);
            startDate.setMinutes(stMinutes);
            prepareHoursMinuts(startDate);
        }
//        if(startDate.getDate() >= presentDate .getDate()){
//            prepareHoursMinuts(startDate);
//
//        }
    }
    private void  prepareHoursMinuts(Date startDate){
        scrollListToPositionDate(hoursRecyclerview,get12HoursFormat(stHours)-1,true);
        scrollListToPositionDate(minutesRecyclerview,stMinutes-1,false);
        amPmSwitch.setChecked(isAm(stHours));
    }
    @Override
    public void itemSelectedInAdapter(int pos,boolean isHours){
        if(isHours){
            scrollListToPositionDate(hoursRecyclerview,pos-1,true);
        }else {
            scrollListToPositionDate(minutesRecyclerview,pos-1,false);
        }
    }

    // 0 = equal dates , 1 = date1 is higher then date2 , 2 = date1 is lower then date2
    private  int compareDates(Date date1 , Date date2){
        if(date1.getYear() == date2.getYear()){
            if(date1.getMonth() == date2.getMonth()){
                if(date1.getDate() == date2.getDate()){
                    return 0;
                }else if(date1.getDate() > date2.getDate()){
                    return 1;
                }else {
                    return 2;
                }
            }else if(date1.getMonth() > date2.getMonth()){
                return 1;
            }else {
                return 2;
            }
        }else if(date1.getYear() > date2.getYear()){
            return 1;
        }else {
            return 2;
        }


    }

    public void prepareDate(){
        finalStartDate = new Date();
        finalStartDate = startDate;
        finalStartDate.setMinutes(stMinutes);
        finalStartDate.setHours(get24Hours(stIsAm , stMinutes));
        if(event != null){
            event.startDate(finalStartDate);
        }
        Log.d("final date",finalStartDate.toString());




    }
//    public int setHr(boolean isAm,int min){
//        int x  = -1;
//        if(isAm){
//            x = min == 12 ? 00 : min;
//        }else {
//            x = min == 12  ? 12 : min + 12;
//        }
//        return x;
//
//    }


    public void startSelecteddate(Date date) {
        this.startDate = date;
        checkDate(presentDate , startDate);
    }



    public void setTimePickerEvent(TimePickerEvent event){
        this.event = event;
    }

    public interface TimePickerEvent{
        void startDate(Date date);
    }
}
