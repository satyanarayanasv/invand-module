package com.satya.invandmodule.event_dates.presenter;

import com.satya.invandmodule.event_dates.Day;
import com.satya.invandmodule.event_dates.ICalendarView;
import com.satya.invandmodule.event_dates.Month;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sivakrishna on 10/10/17.
 */

public class CalanderPresenter {
    private ArrayList<Month> months = new ArrayList<>();
    private Date presentdate;
    private int noOfMonths;
    private Date startDate , endDate;
    private Calendar calendar;
    private ICalendarView view;

    public ArrayList<Month> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<Month> months) {
        this.months = months;
    }

    public Date getPresentdate() {
        return presentdate;
    }

    public void setPresentdate(Date presentdate) {
        this.presentdate = presentdate;
    }

    public int getNoOfMonths() {
        return noOfMonths;
    }

    public void setNoOfMonths(int noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setView(ICalendarView view){
        this.view = view;
    }

    public void prepareCalenderMonths(){
        Calendar presentDate  = Calendar.getInstance();
        presentDate.set(Calendar.HOUR_OF_DAY,0);
        presentDate.set(Calendar.MINUTE,0);
        presentDate.set(Calendar.SECOND,0);
        presentDate.set(Calendar.MILLISECOND,0);
        setPresentdate(presentDate.getTime());

        months.clear();
        for (int i = 0; i < 12; i++) {
            this.calendar = Calendar.getInstance();
            this.calendar.add(Calendar.MONTH,i);
            Month localCalendarMonth = new Month();
            localCalendarMonth.setMonthName(new SimpleDateFormat("MMMM").format(this.calendar.getTime()));
            localCalendarMonth.setCuurentCalander(this.calendar.getTime());
            localCalendarMonth.setMonthDays(getDays(this.calendar));
            months.add(localCalendarMonth);
        }
        Calendar departCal= Calendar.getInstance();
        departCal.add(Calendar.DAY_OF_MONTH,0);
        departCal.set(Calendar.HOUR_OF_DAY,0);
        departCal.set(Calendar.MINUTE,0);
        departCal.set(Calendar.SECOND,0);
        departCal.set(Calendar.MILLISECOND,0);

        if(startDate==null){
            startDate=departCal.getTime();
        }
        startDate(startDate,getMonth(startDate));


        setMonths(months);
    }

    public ArrayList<Day> getDays(Calendar calendar){
        ArrayList<Day> days = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);


        Calendar departCal= Calendar.getInstance();


        while (days.size() < 42) {
            Day day=new Day();
            day.setDate(calendar.getTime());
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            day.setInRange(false);
            day.setSelected(false);
            days.add(day);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }
        return days;


    }
    public void dayClicked(Day day , boolean isStartDate){
        Date date = day.getDate();
        if(isStartDate){
            if(date.equals(presentdate) || date.after(presentdate)){
                clearLastDepartDate(startDate,getMonth(startDate));
                startDate = date;
                startDate(startDate,getMonth(startDate));
                view.updateDeparture(startDate);
            }
        }


    }
    public void clearLastDepartDate(Date date, int month){
        for(int i=0;i<months.get(month).getMonthDays().size();i++){
            if(months.get(month).getMonthDays().get(i).getDate().getTime()==date.getTime()){
                months.get(month).getMonthDays().get(i).setSelected(false);
                break;
            }
        }

    }
    public int getMonth(Date date){
        int x=0;
        for (int i = 0; i <months.size() ; i++) {
            if(months.get(i).getMonthName().equalsIgnoreCase(new SimpleDateFormat("MMMM").format(date))){
                x=i;
                break;
            }
        }
        return x;

    }


    public void startDate(Date date, int month){
        Calendar presentDate  = Calendar.getInstance();
//        presentDate.setTime(date);
//        presentDate.set(Calendar.HOUR_OF_DAY,0);
//        presentDate.set(Calendar.MINUTE,0);
//        presentDate.set(Calendar.SECOND,0);
//        Date givenDate = presentDate.getTime();

        for(int i = 0;i < months.get(month).getMonthDays().size(); i ++){
            if(months.get(month).getMonthDays().get(i).getDate().getTime() == date.getTime()){
                months.get(month).getMonthDays().get(i).setSelected(true);
                break;
            }
        }
    }


}
